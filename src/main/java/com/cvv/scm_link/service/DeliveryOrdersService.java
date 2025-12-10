package com.cvv.scm_link.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.constant.StatusDelivery;
import com.cvv.scm_link.constant.StatusOrder;
import com.cvv.scm_link.dto.request.DeliveryOrdersRequest;
import com.cvv.scm_link.dto.response.DeliveryOrdersResponse;
import com.cvv.scm_link.dto.response.MapboxMultiStopRouteInfo;
import com.cvv.scm_link.entity.Delivery;
import com.cvv.scm_link.entity.DeliveryOrders;
import com.cvv.scm_link.entity.Order;
import com.cvv.scm_link.entity.Warehouse;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.DeliveryOrdersMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.DeliveryOrdersRepository;
import com.cvv.scm_link.repository.OrderRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryOrdersService
        extends BaseServiceImpl<
                DeliveryOrdersRequest, DeliveryOrdersRequest, DeliveryOrdersResponse, DeliveryOrders, String> {

    DeliveryOrdersMapper deliveryOrdersMapper;
    OrderRepository orderRepository;
    DeliveryOrdersRepository deliveryOrdersRepository;
    MapboxService mapboxService;

    public DeliveryOrdersService(
            BaseRepository<DeliveryOrders, String> baseRepository,
            BaseMapper<DeliveryOrders, DeliveryOrdersRequest, DeliveryOrdersRequest, DeliveryOrdersResponse> baseMapper,
            DeliveryOrdersMapper deliveryOrdersMapper,
            OrderRepository orderRepository,
            DeliveryOrdersRepository deliveryOrdersRepository,
            MapboxService mapboxService) {
        super(baseRepository, baseMapper);
        this.deliveryOrdersMapper = deliveryOrdersMapper;
        this.orderRepository = orderRepository;
        this.deliveryOrdersRepository = deliveryOrdersRepository;
        this.mapboxService = mapboxService;
    }

    public Page<DeliveryOrdersResponse> getDeliveryOrdersByDeliveryId(String deliveryId, Pageable pageable) {
        return deliveryOrdersRepository.findByDelivery_Id(deliveryId, pageable).map(baseMapper::toDTO);
    }

    @Transactional(rollbackFor = AppException.class)
    public List<DeliveryOrders> createDeliveryOrders(
            List<DeliveryOrdersRequest> deliveryOrdersRequests, Delivery delivery) {
        validateDeliveryOrdersRequests(deliveryOrdersRequests);
        List<DeliveryOrders> deliveryOrdersList = new ArrayList<>();

        for (DeliveryOrdersRequest request : deliveryOrdersRequests) {
            Order order = orderRepository
                    .findById(request.getOrderId())
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            order.setOrderStatus(StatusOrder.ORDER_SHIPPING);
            orderRepository.save(order);

            // Create delivery order
            DeliveryOrders deliveryOrder = deliveryOrdersMapper.toEntity(request);
            deliveryOrder.setOrder(order);
            deliveryOrder.setDelivery(delivery);
            deliveryOrder.setItemStatus(StatusDelivery.PENDING);

            deliveryOrdersList.add(deliveryOrder);
        }
        deliveryOrdersRepository.saveAll(deliveryOrdersList);

        calculateAndUpdateRouteInfo(delivery, deliveryOrdersList);

        return deliveryOrdersList;
    }

    private void validateDeliveryOrdersRequests(List<DeliveryOrdersRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        Set<String> orderIds = new HashSet<>();
        for (DeliveryOrdersRequest request : requests) {
            if (!orderIds.add(request.getOrderId())) {
                throw new AppException(ErrorCode.DUPLICATE_ORDER_IN_DELIVERY);
            }
        }
    }

    public void calculateAndUpdateRouteInfo(Delivery delivery, List<DeliveryOrders> orders) {
        if (orders == null || orders.isEmpty()) {
            log.warn("No orders in delivery {}, skipping route calculation", delivery.getId());
            return;
        }
        List<double[]> coordinates = buildCoordinatesList(delivery, orders);

        try {
            MapboxMultiStopRouteInfo routeInfo = mapboxService
                    .getOptimizedRoute(coordinates)
                    .timeout(Duration.ofSeconds(10))
                    .blockOptional()
                    .orElseThrow(() -> new AppException(ErrorCode.ROUTE_CALCULATION_FAILED));

            delivery.setTotalDistanceKm((int) routeInfo.getTotalDistanceKm());
            delivery.setTotalDurationMinutes((int) routeInfo.getTotalDurationMinutes());
            delivery.setScheduledDeliveryTime(
                    LocalDateTime.now().plusMinutes((long) routeInfo.getTotalDurationMinutes()));
            delivery.setOptimizedOrder(routeInfo.getWaypointOrder().toString());
            updateOrderSequences(delivery, orders, routeInfo.getWaypointOrder());

            log.info(
                    "Route calculated for delivery {}: {}km, {}min",
                    delivery.getId(),
                    routeInfo.getTotalDistanceKm(),
                    routeInfo.getTotalDurationMinutes());
        } catch (Exception e) {
            log.error("Failed to calculate route for delivery: {}", delivery.getId(), e);
            throw new AppException(ErrorCode.ROUTE_CALCULATION_FAILED);
        }
    }

    private List<double[]> buildCoordinatesList(Delivery delivery, List<DeliveryOrders> orders) {
        List<double[]> coordinates = new ArrayList<>();
        Warehouse warehouse = delivery.getPickupWarehouse();
        coordinates.add(new double[] {warehouse.getLatitude(), warehouse.getLongitude()});

        orders.stream()
                .map(DeliveryOrders::getOrder)
                .forEach(order ->
                        coordinates.add(new double[] {order.getShippingLatitude(), order.getShippingLongitude()}));

        return coordinates;
    }

    private void updateOrderSequences(Delivery delivery, List<DeliveryOrders> orders, List<Integer> waypointOrder) {
        for (int i = 1; i < waypointOrder.size(); i++) { // Bắt đầu từ 1 vì 0 là warehouse
            int originalIndex = waypointOrder.get(i);

            // originalIndex - 1 vì coordinates có warehouse ở đầu
            // Nhưng orders list không có warehouse
            DeliveryOrders deliveryOrder = orders.get(originalIndex - 1);

            // i là vị trí sau optimize (1, 2, 3...)
            deliveryOrder.setOrderSequence(i);
            deliveryOrdersRepository.save(deliveryOrder);

            log.debug("Order {} set to sequence {}", deliveryOrder.getOrder().getId(), i);
        }
    }
}
