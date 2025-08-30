package com.cvv.scm_link.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.dto.request.OrderItemsRequest;
import com.cvv.scm_link.dto.response.OrderDetailResponse;
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
        extends BaseServiceImpl<OrderItemsRequest, OrderItemsRequest, OrderDetailResponse, OrderItems, String> {

    OrderItemsRepository orderItemsRepository;
    OrderItemsMapper orderItemsMapper;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    InventoryLocationDetailRepository inventoryLocationDetailRepository;
    InventoryLevelRepository inventoryLevelRepository;

    public OrderItemsService(
            BaseRepository<OrderItems, String> baseRepository,
            BaseMapper<OrderItems, OrderItemsRequest, OrderItemsRequest, OrderDetailResponse> baseMapper,
            OrderItemsRepository orderItemsRepository,
            OrderItemsMapper orderItemsMapper,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            InventoryLocationDetailRepository inventoryLocationDetailRepository,
            InventoryLevelRepository inventoryLevelRepository) {
        super(baseRepository, baseMapper);
        this.orderItemsRepository = orderItemsRepository;
        this.orderItemsMapper = orderItemsMapper;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.inventoryLocationDetailRepository = inventoryLocationDetailRepository;
        this.inventoryLevelRepository = inventoryLevelRepository;
    }

    @Override
    public OrderDetailResponse findById(String s) {
        return super.findById(s);
    }

    @Transactional(rollbackFor = AppException.class)
    public List<OrderItems> createItems(List<OrderItemsRequest> items, Order order) {
        List<OrderItems> orderItemsList = new ArrayList<>();
        items.forEach(dto -> {
            InventoryLevel inventoryLevel = inventoryLevelRepository
                    .findByProduct_Id(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LEVEL_NOT_FOUND));

            if (dto.getQuantity() > inventoryLevel.getQuantityAvailable()) {
                throw new AppException(ErrorCode.PRODUCT_EXCEEDS_ALLOWABLE);
            }

            inventoryLevel.setQuantityAvailable(inventoryLevel.getQuantityAvailable() - dto.getQuantity());
            inventoryLevel.setQuantityReserved(inventoryLevel.getQuantityReserved() + dto.getQuantity());
            inventoryLevelRepository.save(inventoryLevel);

            int quantityToProcess = dto.getQuantity();
            while (quantityToProcess > 0) {
                InventoryLocationDetail inventoryLocationDetail =
                        inventoryLocationDetailRepository
                                .findByFEFOOrFIFO(dto.getProductId(), PageRequest.of(0, 1))
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                int allocateQty = Math.min(quantityToProcess, inventoryLocationDetail.getQuantity());
                inventoryLocationDetail.setQuantity(inventoryLocationDetail.getQuantity() - allocateQty);
                inventoryLocationDetailRepository.save(inventoryLocationDetail);

                Product product = inventoryLocationDetail.getInventoryLevel().getProduct();

                long priceAtOrder = (long)
                        (inventoryLocationDetail.getCostPrice() + (inventoryLocationDetail.getCostPrice() * 0.2));

                OrderItems orderItems = orderItemsMapper.toEntity(dto);
                orderItems.setProduct(product);
                orderItems.setQuantity(allocateQty);
                orderItems.setPriceAtOrder(priceAtOrder);
                orderItems.setOrder(order);
                orderItemsList.add(orderItemsRepository.save(orderItems));
                quantityToProcess -= allocateQty;
            }
        });
        return orderItemsList;
    }

    @Override
    public OrderDetailResponse update(OrderItemsRequest dto, String s) {
        return super.update(dto, s);
    }
}
