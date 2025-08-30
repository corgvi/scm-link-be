package com.cvv.scm_link.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.constant.StatusOrder;
import com.cvv.scm_link.constant.StatusPayment;
import com.cvv.scm_link.dto.request.OrderRequest;
import com.cvv.scm_link.dto.response.OrderResponse;
import com.cvv.scm_link.entity.*;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.OrderMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.OrderRepository;
import com.cvv.scm_link.repository.ProductRepository;
import com.cvv.scm_link.repository.UserRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService extends BaseServiceImpl<OrderRequest, OrderRequest, OrderResponse, Order, String> {

    private static final long SHIPPING_BASE_FEE = 20000;
    private static final long SHIPPING_PER_KG_FEE = 2000;
    private static final long SHIPPING_PER_KM_FEE = 5000;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderItemsService orderItemsService;
    ProductRepository productRepository;
    UserRepository userRepository;
    MapboxService mapboxService;

    public OrderService(
            BaseRepository<Order, String> baseRepository,
            BaseMapper<Order, OrderRequest, OrderRequest, OrderResponse> baseMapper,
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            OrderItemsService orderItemsService,
            ProductRepository productRepository,
            UserRepository userRepository,
            MapboxService mapboxService) {
        super(baseRepository, baseMapper);
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemsService = orderItemsService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mapboxService = mapboxService;
    }

    @Override
    public OrderResponse findById(String s) {
        return super.findById(s);
    }

    @Override
    public OrderResponse create(OrderRequest dto) {
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
        List<OrderItems> orderItemsList = orderItemsService.createItems(dto.getItems(), order);

        Warehouse warehouse = orderItemsList
                .getFirst()
                .getProduct()
                .getInventoryLevels()
                .getFirst()
                .getWarehouse();

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
        return orderMapper.toDTO(orderRepository.save(order));
    }

    @Override
    public OrderResponse update(OrderRequest dto, String s) {
        return super.update(dto, s);
    }
}
