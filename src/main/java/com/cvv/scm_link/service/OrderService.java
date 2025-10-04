package com.cvv.scm_link.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.constant.StatusOrder;
import com.cvv.scm_link.constant.StatusPayment;
import com.cvv.scm_link.dto.request.OrderCreateRequest;
import com.cvv.scm_link.dto.request.OrderUpdateRequest;
import com.cvv.scm_link.dto.response.OrderResponse;
import com.cvv.scm_link.entity.*;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.OrderMapper;
import com.cvv.scm_link.mapper.UserMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.OrderRepository;
import com.cvv.scm_link.repository.ProductRepository;
import com.cvv.scm_link.repository.UserRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

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
    ProductRepository productRepository;
    UserRepository userRepository;
    MapboxService mapboxService;
    UserMapper userMapper;

    public OrderService(
            BaseRepository<Order, String> baseRepository,
            BaseMapper<Order, OrderCreateRequest, OrderUpdateRequest, OrderResponse> baseMapper,
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            OrderItemsService orderItemsService,
            ProductRepository productRepository,
            UserRepository userRepository,
            MapboxService mapboxService,
            UserMapper userMapper) {
        super(baseRepository, baseMapper);
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemsService = orderItemsService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mapboxService = mapboxService;
        this.userMapper = userMapper;
    }

    @Override
    public OrderResponse findById(String s) {
        return super.findById(s);
    }

    @Transactional(rollbackFor = AppException.class)
    @Override
    public OrderResponse create(OrderCreateRequest dto) {
        User user = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Order order = orderMapper.toEntity(dto);
        order.setOrderStatus(StatusOrder.ORDER_PROCESSING);
        order.setPaymentStatus(StatusPayment.PAYMENT_PENDING);
        order.setCustomer(user);

        double[] coordinates = mapboxService
                .getCoordinatesFromAddress(dto.getShippingAddress())
                .block();

        if (Objects.isNull(coordinates)) throw new RuntimeException("Convert to coordinates failed");

        order.setShippingLatitude(coordinates[0]);
        order.setShippingLongitude(coordinates[1]);

        AtomicLong shippingFees = new AtomicLong(0);
        AtomicLong subTotal = new AtomicLong(0);
        orderRepository.save(order);
        List<OrderItems> orderItemsList = orderItemsService.createOrUpdate(dto.getItems(), order);

        Warehouse warehouse = orderItemsList
                .getFirst()
                .getProduct()
                .getInventoryLevels()
                .getFirst()
                .getWarehouse();

        if (Objects.isNull(warehouse)) throw new AppException(ErrorCode.WAREHOUSE_NOT_FOUND);

        double distanceKm = mapboxService
                .getDistanceKm(warehouse.getLatitude(), warehouse.getLongitude(), coordinates[0], coordinates[1])
                .block();

        orderItemsList.forEach(i -> {
            Product product = i.getProduct();
            long weightKg = ((long) product.getWeightG() * i.getQuantity()) / 1000;
            long shippingFeeForItem =
                    (long) (SHIPPING_BASE_FEE + (distanceKm * SHIPPING_PER_KM_FEE) + (weightKg * SHIPPING_PER_KG_FEE));

            shippingFees.addAndGet(shippingFeeForItem);
            subTotal.addAndGet(i.getPriceAtOrder() * i.getQuantity());
        });

        long totalAmount = shippingFees.get() + subTotal.get();
        order.setTotalAmount(totalAmount);
        OrderResponse orderResponse = orderMapper.toDTO(orderRepository.save(order));
        orderResponse.setUser(userMapper.toDTO(user));
        return orderResponse;
    }

    @Transactional(rollbackFor = AppException.class)
    @Override
    public OrderResponse update(OrderUpdateRequest dto, String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (dto.getShippingAddress() != null && dto.getShippingAddress().trim().isEmpty())
            throw new AppException(ErrorCode.ADDRESS_IS_REQUIRED);
        if (dto.getCustomerName() != null && !dto.getCustomerName().trim().isEmpty())
            throw new AppException(ErrorCode.NAME_INVALID);
        if (dto.getCustomerPhone() != null && !dto.getCustomerPhone().trim().isEmpty())
            throw new AppException(ErrorCode.PHONE_NUMBER_INVALID);
        order.setOrderStatus(dto.getOrderStatus());
        order.setShippingAddress(dto.getShippingAddress());
        order.setCustomerName(dto.getCustomerName());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setCustomerEmail(dto.getCustomerEmail());
        order.setNote(dto.getNote());
        if (dto.getOrderStatus().equalsIgnoreCase(StatusOrder.ORDER_SHIPPING)) {
            order.setOrderStatus(StatusOrder.ORDER_SHIPPING);
            order.setPaymentStatus(StatusPayment.PAYMENT_PENDING);
        } else if (dto.getOrderStatus().equalsIgnoreCase(StatusOrder.ORDER_DELIVERED) && order.getPaymentStatus().equals(StatusPayment.PAYMENT_PAID)) {
            order.setPaymentStatus(StatusPayment.PAYMENT_PAID);
            order.setOrderStatus(StatusOrder.ORDER_COMPLETED);
        } else if (dto.getOrderStatus().equalsIgnoreCase(StatusOrder.ORDER_CANCELLED)) {
            order.setPaymentStatus(StatusPayment.PAYMENT_REFUND);
            order.setOrderStatus(StatusOrder.ORDER_CANCELLED);
        } else if (dto.getOrderStatus().equalsIgnoreCase(StatusOrder.ORDER_DELIVERED)) {
            order.setPaymentStatus(StatusPayment.PAYMENT_PENDING);
            order.setOrderStatus(StatusOrder.ORDER_DELIVERED);
        } else {
            order.setOrderStatus(StatusOrder.ORDER_PROCESSING);
            order.setPaymentStatus(StatusPayment.PAYMENT_PENDING);
        }
        List<OrderItems> orderItemsList = orderItemsService.createOrUpdate(dto.getItems(), order);
        return null;
    }
}
