package com.cvv.scm_link.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.constant.TransactionType;
import com.cvv.scm_link.dto.request.OrderItemsRequest;
import com.cvv.scm_link.dto.response.OrderItemDetailResponse;
import com.cvv.scm_link.entity.*;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.OrderItemsMapper;
import com.cvv.scm_link.repository.*;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemsService
        extends BaseServiceImpl<OrderItemsRequest, OrderItemsRequest, OrderItemDetailResponse, OrderItems, String> {

    OrderItemsRepository orderItemsRepository;
    OrderItemsMapper orderItemsMapper;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    InventoryLocationDetailRepository inventoryLocationDetailRepository;
    InventoryLevelRepository inventoryLevelRepository;
    InventoryTransactionRepository inventoryTransactionRepository;
    OrderItemBatchAllocationService orderItemBatchAllocationService;

    public OrderItemsService(
            BaseRepository<OrderItems, String> baseRepository,
            BaseMapper<OrderItems, OrderItemsRequest, OrderItemsRequest, OrderItemDetailResponse> baseMapper,
            OrderItemsRepository orderItemsRepository,
            OrderItemsMapper orderItemsMapper,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            InventoryLocationDetailRepository inventoryLocationDetailRepository,
            InventoryLevelRepository inventoryLevelRepository,
            InventoryTransactionRepository inventoryTransactionRepository,
            OrderItemBatchAllocationService orderItemBatchAllocationService) {
        super(baseRepository, baseMapper);
        this.orderItemsRepository = orderItemsRepository;
        this.orderItemsMapper = orderItemsMapper;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.inventoryLocationDetailRepository = inventoryLocationDetailRepository;
        this.inventoryLevelRepository = inventoryLevelRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.orderItemBatchAllocationService = orderItemBatchAllocationService;
    }

    @Override
    public OrderItemDetailResponse findById(String s) {
        return super.findById(s);
    }

    public List<OrderItems> findAllByOrderId(String orderId) {
        return orderItemsRepository.findAllByOrder_Id(orderId);
    }

    @Transactional(rollbackFor = AppException.class)
    public List<OrderItems> createOrUpdate(List<OrderItemsRequest> items, Order order) {

        Set<String> products =
                items.stream().map(OrderItemsRequest::getProductId).collect(Collectors.toSet());
        List<OrderItems> existingItems = orderItemsRepository.findAllByOrder_Id(order.getId());
        for (OrderItems existingItem : existingItems) {
            if (!products.contains(existingItem.getProduct().getId())) {
                rollbackAndDeleteOrderItem(existingItem);
            }
        }

        List<OrderItems> orderItemsList = new ArrayList<>();
        items.forEach(dto -> {
            InventoryLevel inventoryLevel = inventoryLevelRepository
                    .findByProduct_Id(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LEVEL_NOT_FOUND));

            Optional<OrderItems> existingOrderItem =
                    orderItemsRepository.findByOrder_IdAndProduct_Id(order.getId(), dto.getProductId());
            if (existingOrderItem.isEmpty()) {
                if (dto.getQuantity() > inventoryLevel.getQuantityAvailable()) {
                    throw new AppException(ErrorCode.PRODUCT_EXCEEDS_ALLOWABLE);
                }
                createOrderItem(dto, order, inventoryLevel, orderItemsList);
            } else {
                OrderItems existingItem = existingOrderItem.get();
                int newQuantity = dto.getQuantity();
                int quantityDiff = newQuantity - existingItem.getQuantity();
                if (quantityDiff > 0) {
                    increaseOrderItem(dto, existingOrderItem, inventoryLevel, quantityDiff, existingItem, order);
                } else if (quantityDiff < 0) {
                    decreaseOrderItem(existingOrderItem, inventoryLevel, quantityDiff, order, dto, existingItem);
                }
            }
        });
        return orderItemsList;
    }

    @Override
    public OrderItemDetailResponse update(OrderItemsRequest dto, String s) {
        return super.update(dto, s);
    }

    private void createOrderItem(
            OrderItemsRequest dto, Order order, InventoryLevel inventoryLevel, List<OrderItems> orderItemsList) {
        OrderItems orderItems = orderItemsMapper.toEntity(dto);

        int totalQty = 0;
        int quantityToProcess = dto.getQuantity();

        while (quantityToProcess > 0) {
            InventoryLocationDetail inventoryLocationDetail =
                    inventoryLocationDetailRepository
                            .findByFEFOOrFIFO(dto.getProductId(), PageRequest.of(0, 1))
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            int allocateQty = Math.min(quantityToProcess, inventoryLocationDetail.getQuantity());
            totalQty += allocateQty;
            inventoryLocationDetail.setQuantityAvailable(inventoryLocationDetail.getQuantity() - allocateQty);
            inventoryLocationDetailRepository.save(inventoryLocationDetail);

            Product product = inventoryLocationDetail.getInventoryLevel().getProduct();

            long priceAtOrder = inventoryLocationDetail.getSellPrice();

            orderItems.setProduct(product);
            orderItems.setPriceAtOrder(priceAtOrder);
            orderItems.setOrder(order);
            orderItemsList.add(orderItemsRepository.save(orderItems));

            OrderItemBatchAllocation orderItemBatchAllocation = OrderItemBatchAllocation.builder()
                    .inventoryLocationDetail(inventoryLocationDetail)
                    .orderItem(orderItems)
                    .quantityAllocated(allocateQty)
                    .build();
            orderItemBatchAllocationService.save(orderItemBatchAllocation);

            quantityToProcess -= allocateQty;
        }

        orderItems.setQuantity(totalQty);
        orderItemsRepository.save(orderItems);

        inventoryLevel.setQuantityAvailable(inventoryLevel.getQuantityAvailable() - totalQty);
        inventoryLevel.setQuantityReserved(inventoryLevel.getQuantityReserved() + totalQty);
        inventoryLevelRepository.save(inventoryLevel);

        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .transactionType(TransactionType.RESERVED)
                .inventoryLevel(inventoryLevel)
                .quantityChange(dto.getQuantity())
                .currentQuantity(inventoryLevel.getQuantityOnHand())
                .relateEntityId(order.getId())
                .note("Allocate for order: " + order.getOrderCode())
                .build();
        inventoryTransactionRepository.save(inventoryTransaction);
    }

    private void increaseOrderItem(
            OrderItemsRequest dto,
            Optional<OrderItems> existingOrderItem,
            InventoryLevel inventoryLevel,
            int quantityDiff,
            OrderItems existingItem,
            Order order) {
        int originalDiff = quantityDiff;

        while (quantityDiff > 0) {
            InventoryLocationDetail inventoryLocationDetail =
                    inventoryLocationDetailRepository
                            .findByFEFOOrFIFO(dto.getProductId(), PageRequest.of(0, 1))
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            int allocateQty = Math.min(quantityDiff, inventoryLocationDetail.getQuantity());
            inventoryLocationDetail.setQuantityAvailable(inventoryLocationDetail.getQuantity() - allocateQty);
            inventoryLocationDetailRepository.save(inventoryLocationDetail);

            OrderItemBatchAllocation orderItemBatchAllocation = OrderItemBatchAllocation.builder()
                    .quantityAllocated(allocateQty)
                    .build();
            orderItemBatchAllocationService.createOrUpdate(
                    existingOrderItem.get().getId(), inventoryLocationDetail.getId(), orderItemBatchAllocation);

            quantityDiff -= allocateQty;
        }

        inventoryLevel.setQuantityAvailable(inventoryLevel.getQuantityAvailable() - originalDiff);
        inventoryLevel.setQuantityReserved(inventoryLevel.getQuantityReserved() + originalDiff);
        inventoryLevelRepository.save(inventoryLevel);

        existingItem.setQuantity(existingItem.getQuantity() + originalDiff);
        orderItemsRepository.save(existingItem);

        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .transactionType(TransactionType.INCREMENT)
                .inventoryLevel(inventoryLevel)
                .quantityChange(originalDiff)
                .currentQuantity(inventoryLevel.getQuantityOnHand())
                .relateEntityId(order.getId())
                .note("Allocate for order: " + order.getId())
                .build();
        inventoryTransactionRepository.save(inventoryTransaction);
    }

    private void decreaseOrderItem(
            Optional<OrderItems> existingOrderItem,
            InventoryLevel inventoryLevel,
            int quantityDiff,
            Order order,
            OrderItemsRequest dto,
            OrderItems existingItem) {
        int rollbackQty = -quantityDiff;

        List<OrderItemBatchAllocation> allocations = orderItemBatchAllocationService.findByOrderItem_Id(
                existingOrderItem.get().getId());
        for (OrderItemBatchAllocation allocation : allocations) {
            if (rollbackQty <= 0) break;
            InventoryLocationDetail inventoryLocationDetail = inventoryLocationDetailRepository
                    .findById(allocation.getInventoryLocationDetail().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LOCATION_DETAIL_NOT_FOUND));
            int allocatedQty = allocation.getQuantityAllocated();
            int qtyToRollback = Math.min(rollbackQty, allocatedQty);
            inventoryLocationDetail.setQuantityAvailable(
                    inventoryLocationDetail.getQuantityAvailable() + qtyToRollback);
            inventoryLocationDetailRepository.save(inventoryLocationDetail);
            allocation.setQuantityAllocated(allocatedQty - qtyToRollback);
            if (allocation.getQuantityAllocated() == 0) {
                orderItemBatchAllocationService.delete(allocation.getId());
            } else {
                orderItemBatchAllocationService.save(allocation);
            }
            rollbackQty -= qtyToRollback;
        }

        inventoryLevel.setQuantityAvailable(inventoryLevel.getQuantityAvailable() + rollbackQty);
        inventoryLevel.setQuantityReserved(inventoryLevel.getQuantityReserved() - rollbackQty);
        inventoryLevelRepository.save(inventoryLevel);

        existingItem.setQuantity(existingItem.getQuantity() - rollbackQty);
        orderItemsRepository.save(existingItem);

        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .transactionType(TransactionType.ROLLBACK)
                .inventoryLevel(inventoryLevel)
                .quantityChange(rollbackQty)
                .currentQuantity(inventoryLevel.getQuantityOnHand())
                .relateEntityId(order.getId())
                .note("Allocate for order: " + order.getId())
                .build();
        inventoryTransactionRepository.save(inventoryTransaction);
    }

    private void rollbackAndDeleteOrderItem(OrderItems existingItem) {
        InventoryLevel inventoryLevel = inventoryLevelRepository
                .findByProduct_Id(existingItem.getProduct().getId())
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LEVEL_NOT_FOUND));

        int rollbackQty = existingItem.getQuantity();

        List<OrderItemBatchAllocation> allocations =
                orderItemBatchAllocationService.findByOrderItem_Id(existingItem.getId());
        for (OrderItemBatchAllocation allocation : allocations) {
            InventoryLocationDetail inventoryLocationDetail = inventoryLocationDetailRepository
                    .findById(allocation.getInventoryLocationDetail().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LOCATION_DETAIL_NOT_FOUND));
            int allocatedQty = allocation.getQuantityAllocated();
            inventoryLocationDetail.setQuantityAvailable(inventoryLocationDetail.getQuantityAvailable() + allocatedQty);
            inventoryLocationDetailRepository.save(inventoryLocationDetail);
            orderItemBatchAllocationService.delete(allocation.getId());
        }

        inventoryLevel.setQuantityAvailable(inventoryLevel.getQuantityAvailable() + rollbackQty);
        inventoryLevel.setQuantityReserved(inventoryLevel.getQuantityReserved() - rollbackQty);
        inventoryLevelRepository.save(inventoryLevel);

        orderItemsRepository.delete(existingItem);

        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .transactionType(TransactionType.ROLLBACK)
                .inventoryLevel(inventoryLevel)
                .quantityChange(rollbackQty)
                .currentQuantity(inventoryLevel.getQuantityOnHand())
                .relateEntityId(existingItem.getOrder().getId())
                .note("Rollback for order item deletion: " + existingItem.getId())
                .build();
        inventoryTransactionRepository.save(inventoryTransaction);
    }
}
