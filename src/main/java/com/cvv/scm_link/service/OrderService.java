package com.cvv.scm_link.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import com.cvv.scm_link.constant.TransactionType;
import com.cvv.scm_link.dto.BaseFilterRequest;
import com.cvv.scm_link.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.constant.StatusOrder;
import com.cvv.scm_link.constant.StatusPayment;
import com.cvv.scm_link.dto.request.OrderCreateRequest;
import com.cvv.scm_link.dto.request.OrderUpdateRequest;
import com.cvv.scm_link.dto.response.OrderDetailResponse;
import com.cvv.scm_link.dto.response.OrderItemBatchAllocationResponse;
import com.cvv.scm_link.dto.response.OrderItemDetailResponse;
import com.cvv.scm_link.dto.response.OrderResponse;
import com.cvv.scm_link.entity.*;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.*;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService
        extends BaseServiceImpl<OrderCreateRequest, OrderUpdateRequest, OrderResponse, Order, String> {

    private static final long SHIPPING_BASE_FEE = 20000;
    private static final long SHIPPING_PER_KG_FEE = 2000;
    private static final long SHIPPING_PER_KM_FEE = 5000;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderItemsService orderItemsService;
    UserRepository userRepository;
    MapboxService mapboxService;
    UserMapper userMapper;
    OrderItemBatchAllocationService orderItemBatchAllocationService;
    OrderItemsMapper orderItemsMapper;
    InventoryLevelRepository inventoryLevelRepository;
    InventoryLocationDetailRepository inventoryLocationDetailRepository;
    InventoryTransactionRepository inventoryTransactionRepository;

    public OrderService(
            BaseRepository<Order, String> baseRepository,
            BaseMapper<Order, OrderCreateRequest, OrderUpdateRequest, OrderResponse> baseMapper,
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            OrderItemsService orderItemsService,
            UserRepository userRepository,
            MapboxService mapboxService,
            UserMapper userMapper,
            OrderItemBatchAllocationService orderItemBatchAllocationService,
            OrderItemsMapper orderItemsMapper, InventoryLevelRepository inventoryLevelRepository, InventoryLocationDetailRepository inventoryLocationDetailRepository, InventoryTransactionRepository inventoryTransactionRepository) {
        super(baseRepository, baseMapper);
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemsService = orderItemsService;
        this.userRepository = userRepository;
        this.mapboxService = mapboxService;
        this.userMapper = userMapper;
        this.orderItemBatchAllocationService = orderItemBatchAllocationService;
        this.orderItemsMapper = orderItemsMapper;
        this.inventoryLevelRepository = inventoryLevelRepository;
        this.inventoryLocationDetailRepository = inventoryLocationDetailRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    public OrderDetailResponse findOrderDetailById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        List<OrderItems> items = orderItemsService.findAllByOrderId(order.getId());
        List<OrderItemDetailResponse> orderItemDetailResponses = new ArrayList<>();
        for (OrderItems item : items) {
            List<OrderItemBatchAllocationResponse> orderItemBatchAllocationResponses =
                    orderItemBatchAllocationService.findOrderItemDetails(item.getId());
            OrderItemDetailResponse orderItemDetailResponse = orderItemsMapper.toDTO(item);
            orderItemDetailResponse.setBatchAllocations(orderItemBatchAllocationResponses);
            orderItemDetailResponses.add(orderItemDetailResponse);
        }

        return OrderDetailResponse.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .createdBy(order.getCreatedBy())
                .updatedBy(order.getUpdatedBy())
                .orderCode(order.getOrderCode())
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .customerEmail(order.getCustomerEmail())
                .shippingAddress(order.getShippingAddress())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .note(order.getNote())
                .orderItems(orderItemDetailResponses)
                .build();
    }

    @Transactional(rollbackFor = AppException.class)
    @Override
    public OrderResponse create(OrderCreateRequest dto) {
        User user = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Order order = orderMapper.toEntity(dto);
        order.setOrderCode(generateCode());
        order.setOrderStatus(StatusOrder.ORDER_PROCESSING);
        order.setPaymentStatus(StatusPayment.PAYMENT_PENDING);
        order.setCustomer(user);

        order.setTotalAmount(calculateTotalAmount(dto.getShippingAddress(), dto.getShippingCity(), orderItemsService.createOrUpdate(dto.getItems(), order), order));
        OrderResponse orderResponse = orderMapper.toDTO(orderRepository.save(order));
        orderResponse.setUser(userMapper.toDTO(user));
        return orderResponse;
    }

    @Transactional(rollbackFor = AppException.class)
    @Override
    public OrderResponse update(OrderUpdateRequest dto, String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getOrderStatus().equalsIgnoreCase(StatusOrder.ORDER_COMPLETED)) throw new AppException(ErrorCode.ORDER_COMPLETED);
        if (order.getOrderStatus().equalsIgnoreCase(StatusOrder.ORDER_CANCELLED)) throw new AppException(ErrorCode.ORDER_CANCELLED);
        if (dto.getOrderStatus().equalsIgnoreCase(StatusOrder.ORDER_PROCESSING) && (dto.getPaymentStatus().equalsIgnoreCase(StatusPayment.PAYMENT_PAID) || dto.getPaymentStatus().equalsIgnoreCase(StatusPayment.PAYMENT_REFUND))) {
            throw new AppException(ErrorCode.PAYMENT_STATUS_INVALID);
        }

        if (dto.getShippingAddress() != null && dto.getShippingAddress().trim().isEmpty()) {
            throw new AppException(ErrorCode.ADDRESS_IS_REQUIRED);
        }
        if (dto.getCustomerName() != null && dto.getCustomerName().trim().isEmpty()) {
            throw new AppException(ErrorCode.NAME_INVALID);
        }
        if (dto.getCustomerPhone() != null && dto.getCustomerPhone().trim().isEmpty()) {
            throw new AppException(ErrorCode.PHONE_NUMBER_INVALID);
        }

        order.setShippingAddress(dto.getShippingAddress());
        order.setCustomerName(dto.getCustomerName());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setCustomerEmail(dto.getCustomerEmail());
        order.setNote(dto.getNote());
        order.setOrderStatus(dto.getOrderStatus());
        order.setPaymentStatus(dto.getPaymentStatus());

        List<OrderItems> updatedItems = new ArrayList<>();
        if (!dto.getOrderStatus().equalsIgnoreCase(StatusOrder.ORDER_CANCELLED)) {
            updatedItems = orderItemsService.createOrUpdate(dto.getItems(), order);
        } else {
            order.setPaymentStatus(StatusPayment.PAYMENT_REFUND);
            rollbackForCancelled(order);
        }


        if (dto.getOrderStatus().equalsIgnoreCase(StatusOrder.ORDER_DELIVERED) && dto.getPaymentStatus().equalsIgnoreCase(StatusPayment.PAYMENT_PAID)) {
            order.setPaymentStatus(StatusPayment.PAYMENT_PAID);
            order.setOrderStatus(StatusOrder.ORDER_COMPLETED);
            orderItemsService.completedOrder(dto.getItems(), order);
        }

        boolean addressChanged = !Objects.equals(order.getShippingAddress(), dto.getShippingAddress());
        boolean itemsChanged = !updatedItems.isEmpty();

        if (addressChanged || itemsChanged) {
            long newTotal = calculateTotalAmount(dto.getShippingAddress(), dto.getShippingCity(), updatedItems, order);
            order.setTotalAmount(newTotal);
        }

        return orderMapper.toDTO(orderRepository.save(order));
    }


    private String generateCode() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private long calculateTotalAmount(
            String shippingAddress,
            String shippingCity,
            List<OrderItems> orderItemsList,
            Order order
    ) {
        double[] coordinates = mapboxService
                .getCoordinatesFromAddress(shippingAddress, shippingCity)
                .block();

        if (Objects.isNull(coordinates))
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);

        order.setShippingAddress(shippingAddress + ", " + shippingCity);
        order.setShippingLatitude(coordinates[0]);
        order.setShippingLongitude(coordinates[1]);

        AtomicLong shippingFees = new AtomicLong(0);
        AtomicLong subTotal = new AtomicLong(0);

        log.info("items: " + orderItemsList);

        Warehouse warehouse = orderItemsList.getFirst()
                .getProduct()
                .getInventoryLevels()
                .getFirst()
                .getWarehouse();


        if (Objects.isNull(warehouse))
            throw new AppException(ErrorCode.WAREHOUSE_NOT_FOUND);

        double distanceKm = mapboxService
                .getDistanceKm(warehouse.getLatitude(), warehouse.getLongitude(), coordinates[0], coordinates[1])
                .block();

        orderItemsList.forEach(item -> {
            Product product = item.getProduct();
            long weightKg = ((long) product.getWeightG() * item.getQuantity()) / 1000;
            long shippingFeeForItem = (long) (
                    SHIPPING_BASE_FEE +
                            (distanceKm * SHIPPING_PER_KM_FEE) +
                            (weightKg * SHIPPING_PER_KG_FEE)
            );

            shippingFees.addAndGet(shippingFeeForItem);
            subTotal.addAndGet(item.getPriceAtOrder() * item.getQuantity());
        });

        return shippingFees.get() + subTotal.get();
    }

    private void rollbackForCancelled(Order order) {
        List<OrderItems> existingItems = orderItemsService.findAllByOrderId(order.getId());
        for(OrderItems existingItem : existingItems) {
            int rollbackQty = existingItem.getQuantity();
            InventoryLevel inventoryLevel = inventoryLevelRepository.findByProduct_Id(existingItem.getProduct().getId()).orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LEVEL_NOT_FOUND));

            List<OrderItemBatchAllocation> allocations = orderItemBatchAllocationService.findAllByOrderItem_Id(existingItem.getId());
            for(OrderItemBatchAllocation allocation : allocations) {
                InventoryLocationDetail inventoryLocationDetail = inventoryLocationDetailRepository
                        .findById(allocation.getInventoryLocationDetail().getId())
                        .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LOCATION_DETAIL_NOT_FOUND));
                inventoryLocationDetail.setQuantityAvailable(inventoryLocationDetail.getQuantityAvailable() + allocation.getQuantityAllocated());
                inventoryLocationDetailRepository.save(inventoryLocationDetail);
                allocation.setCompleted(true);
                orderItemBatchAllocationService.save(allocation);
            }


            inventoryLevel.setQuantityAvailable(inventoryLevel.getQuantityOnHand() + rollbackQty);
            inventoryLevel.setQuantityReserved(inventoryLevel.getQuantityReserved() - rollbackQty);
            inventoryLevelRepository.save(inventoryLevel);

            InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                    .transactionType(TransactionType.ROLLBACK)
                    .inventoryLevel(inventoryLevel)
                    .quantityChange(rollbackQty)
                    .currentQuantity(inventoryLevel.getQuantityOnHand())
                    .relateEntityId(existingItem.getOrder().getId())
                    .note("Rollback all for cancelled order: " + existingItem.getOrder().getOrderCode())
                    .build();
            inventoryTransactionRepository.save(inventoryTransaction);
        }
    }

    @Override
    public Page<OrderResponse> filter(BaseFilterRequest filter, Pageable pageable) {
        return super.filter(filter, pageable);
    }
}
