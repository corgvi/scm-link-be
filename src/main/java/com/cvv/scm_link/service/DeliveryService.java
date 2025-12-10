package com.cvv.scm_link.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.constant.StatusDelivery;
import com.cvv.scm_link.dto.filter.DeliveryFilter;
import com.cvv.scm_link.dto.request.DeliveryCreateRequest;
import com.cvv.scm_link.dto.request.DeliveryOrdersRequest;
import com.cvv.scm_link.dto.request.DeliveryStatusUpdateRequest;
import com.cvv.scm_link.dto.request.DeliveryUpdateRequest;
import com.cvv.scm_link.dto.response.DeliveryResponse;
import com.cvv.scm_link.entity.*;
import com.cvv.scm_link.enums.Role;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.DeliveryMapper;
import com.cvv.scm_link.mapper.DeliveryOrdersMapper;
import com.cvv.scm_link.repository.*;
import com.cvv.scm_link.repository.specification.DeliverySpecification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryService
        extends BaseServiceImpl<DeliveryCreateRequest, DeliveryUpdateRequest, DeliveryResponse, Delivery, String> {

    VehicleService vehicleService;
    DeliveryMapper deliveryMapper;
    UserRepository userRepository;
    DeliveryRepository deliveryRepository;
    DeliveryOrdersService deliveryOrdersService;
    WarehouseRepository warehouseRepository;
    InventoryTransactionRepository inventoryTransactionRepository;
    DeliveryOrdersRepository deliveryOrdersRepository;
    OrderRepository orderRepository;
    DeliveryOrdersMapper deliveryOrdersMapper;
    private final VehicleRepository vehicleRepository;

    public DeliveryService(
            BaseRepository<Delivery, String> baseRepository,
            BaseMapper<Delivery, DeliveryCreateRequest, DeliveryUpdateRequest, DeliveryResponse> baseMapper,
            VehicleService vehicleService,
            DeliveryMapper deliveryMapper,
            UserRepository userRepository,
            DeliveryRepository deliveryRepository,
            DeliveryOrdersService deliveryOrdersService,
            WarehouseRepository warehouseRepository1,
            InventoryTransactionRepository inventoryTransactionRepository,
            DeliveryOrdersRepository deliveryOrdersRepository,
            OrderRepository orderRepository,
            DeliveryOrdersMapper deliveryOrdersMapper,
            VehicleRepository vehicleRepository) {
        super(baseRepository, baseMapper);
        this.vehicleService = vehicleService;
        this.deliveryMapper = deliveryMapper;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.deliveryOrdersService = deliveryOrdersService;
        this.warehouseRepository = warehouseRepository1;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.deliveryOrdersRepository = deliveryOrdersRepository;
        this.orderRepository = orderRepository;
        this.deliveryOrdersMapper = deliveryOrdersMapper;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(rollbackFor = AppException.class)
    @Override
    public DeliveryResponse create(DeliveryCreateRequest dto) {
        Vehicle vehicle = vehicleService.findByLicensePlate(dto.getVehicleLicensePlate());
        User user = userRepository
                .findByIdAndRoleName(dto.getDriverId(), Role.SHIPPER.name())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String firstOrderId = dto.getDeliveryOrders().get(0).getOrderId();
        Order firstOrder =
                orderRepository.findById(firstOrderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        Warehouse warehouse = warehouseRepository
                .findById(firstOrder.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));

        Delivery delivery = deliveryMapper.toEntity(dto);
        delivery.setCode(generateDeliveryCode());
        delivery.setDeliveryStatus(StatusDelivery.PENDING);
        delivery.setAssignedVehicle(vehicle);
        delivery.setAssignedDriver(user);
        delivery.setPickupWarehouse(warehouse);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        savedDelivery.setDeliveryOrders(
                deliveryOrdersService.createDeliveryOrders(dto.getDeliveryOrders(), savedDelivery));
        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);
        createInventoryTransaction(delivery, StatusDelivery.PENDING, "Delivery Pending by: " + delivery.getCode());
        return deliveryMapper.toDTO(savedDelivery);
    }

    @Transactional(rollbackFor = AppException.class)
    @Override
    public DeliveryResponse update(DeliveryUpdateRequest dto, String s) {
        Delivery delivery =
                deliveryRepository.findDetailById(s).orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOT_FOUND));
        log.info(
                "List orders: {}",
                delivery.getDeliveryOrders().stream()
                        .map(doe -> doe.getOrder().getId())
                        .collect(Collectors.toList()));

        if (delivery.getDeliveryStatus().equals(StatusDelivery.DELIVERY_COMPLETED)
                || delivery.getDeliveryStatus().equals(StatusDelivery.DELIVERY_CANCELLED)) {
            throw new AppException(ErrorCode.DELIVERY_ALREADY_FINALIZED);
        }
//        if (dto.getDeliveryOrders() != null && delivery.getDeliveryStatus().equals(StatusDelivery.DELIVERING)) {
//            throw new AppException(ErrorCode.CANNOT_UPDATE_ORDERS_WHILE_DELIVERING);
//        }
        if (dto.getNote() != null && !dto.getNote().isEmpty()) {
            delivery.setNote(dto.getNote());
        }
        if (dto.getDriverId() != null && !dto.getDriverId().isEmpty()) {
            User user = userRepository
                    .findByIdAndRoleName(dto.getDriverId(), Role.SHIPPER.name())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            delivery.setAssignedDriver(user);
        }
        if (dto.getVehicleLicensePlate() != null && !dto.getVehicleLicensePlate().isEmpty()) {
            Vehicle vehicle = vehicleService.findByLicensePlate(dto.getVehicleLicensePlate());
            delivery.setAssignedVehicle(vehicle);
        }
        if (dto.getDeliveryOrders() != null && !dto.getDeliveryOrders().isEmpty()) {
            handleDeliveryOrdersUpdate(dto.getDeliveryOrders(), delivery);
        }
        handleStatusChange(dto.getDeliveryStatus(), delivery);

        return deliveryMapper.toDTO(deliveryRepository.save(delivery));
    }

    private void handleDeliveryOrdersUpdate(List<DeliveryOrdersRequest> newOrderRequests, Delivery delivery) {
        // Lấy danh sách order IDs hiện tại
        Set<String> currentOrderIds = deliveryOrdersRepository.findByDelivery_Id(delivery.getId()).stream()
                .map(d -> d.getOrder().getId())
                .collect(Collectors.toSet());
        // Lấy danh sách order IDs mới từ request
        Set<String> newOrderIds =
                newOrderRequests.stream().map(DeliveryOrdersRequest::getOrderId).collect(Collectors.toSet());

        // Tìm orders cần XÓA (có trong cũ, không có trong mới)
        Set<String> ordersToRemove = new HashSet<>(currentOrderIds);
        ordersToRemove.removeAll(newOrderIds);

        // Tìm orders cần THÊM (có trong mới, không có trong cũ)
        Set<String> ordersToAdd = new HashSet<>(newOrderIds);
        ordersToAdd.removeAll(currentOrderIds);

        boolean hasChanges = false;

        // Xóa delivery orders không còn trong danh sách
        if (!ordersToRemove.isEmpty()) {
            removeOrdersFromDelivery(delivery.getId(), ordersToRemove);
            hasChanges = true;
            log.info("Removed {} orders from delivery {}", ordersToRemove.size(), delivery.getId());
        }

        // Thêm delivery orders mới
        if (!ordersToAdd.isEmpty()) {
            List<DeliveryOrdersRequest> requestsToAdd = newOrderRequests.stream()
                    .filter(req -> ordersToAdd.contains(req.getOrderId()))
                    .toList();

            addOrdersToDelivery(requestsToAdd, delivery);
            hasChanges = true;
            log.info("Added {} orders to delivery {}", ordersToAdd.size(), delivery.getId());
        }

        // Tính lại route nếu có thay đổi
        if (hasChanges) {
            // Refresh delivery orders từ DB
            List<DeliveryOrders> updatedOrders = deliveryOrdersRepository.findByDelivery_Id(delivery.getId());
            delivery.setDeliveryOrders(updatedOrders);

            // Recalculate route
            deliveryOrdersService.calculateAndUpdateRouteInfo(delivery, updatedOrders);
            log.info("Recalculated route for delivery {}", delivery.getId());
        }
    }

    private void removeOrdersFromDelivery(String deliveryId, Set<String> orderIds) {
        deliveryOrdersRepository.deleteByDelivery_IdAndOrder_IdIn(deliveryId, orderIds);
    }

    private void addOrdersToDelivery(List<DeliveryOrdersRequest> orderRequests, Delivery delivery) {
        for (DeliveryOrdersRequest request : orderRequests) {
            Order order = orderRepository
                    .findById(request.getOrderId())
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            // Tạo delivery order mới
            DeliveryOrders deliveryOrder = deliveryOrdersMapper.toEntity(request);
            deliveryOrder.setOrder(order);
            deliveryOrder.setDelivery(delivery);
            deliveryOrder.setItemStatus(StatusDelivery.PENDING);

            deliveryOrdersRepository.save(deliveryOrder);
        }
    }

    private void handleStatusChange(String newStatus, Delivery delivery) {
        for (DeliveryOrders d : delivery.getDeliveryOrders()) {
            if (d.getOrder() == null) {
                log.error("⚠️ DeliveryOrders {} has NULL ORDER!", d.getId());
            }
        }
        switch (newStatus) {
            case StatusDelivery.DELIVERING -> {
                delivery.setActualPickupTime(LocalDateTime.now());
                delivery.setDeliveryStatus(StatusDelivery.DELIVERING);
                createInventoryTransaction(
                        delivery, StatusDelivery.DELIVERING, "Delivery started: " + delivery.getCode());
            }
            case StatusDelivery.DELIVERY_COMPLETED -> {
                delivery.setActualDeliveryTime(LocalDateTime.now());
                delivery.setDeliveryStatus(StatusDelivery.DELIVERY_COMPLETED);
                createInventoryTransaction(
                        delivery, StatusDelivery.DELIVERY_COMPLETED, "Delivery completed: " + delivery.getCode());
            }
            case StatusDelivery.DELIVERY_CANCELLED -> {
                delivery.setDeliveryStatus(StatusDelivery.DELIVERY_CANCELLED);
                createInventoryTransaction(
                        delivery, StatusDelivery.DELIVERY_CANCELLED, "Delivery cancelled: " + delivery.getCode());
            }
        }
    }

    private String generateDeliveryCode() {
        return "DLV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void createInventoryTransaction(Delivery delivery, String transactionType, String note) {
        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .transactionType(transactionType)
                .relatedEntityId(delivery.getId())
                .note(note)
                .build();
        inventoryTransactionRepository.save(inventoryTransaction);
    }

    @Transactional(rollbackFor = AppException.class)
    public DeliveryResponse updateStatus(String id, DeliveryStatusUpdateRequest dto) {
        Delivery delivery =
                deliveryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOT_FOUND));

        String newStatus = dto.getDeliveryStatus();

        if (delivery.getDeliveryStatus().equals(StatusDelivery.DELIVERY_COMPLETED)
                || delivery.getDeliveryStatus().equals(StatusDelivery.DELIVERY_CANCELLED)) {
            throw new AppException(ErrorCode.DELIVERY_ALREADY_FINALIZED);
        }

        switch (newStatus) {
            case StatusDelivery.DELIVERING -> handleStartDelivery(delivery);
            case StatusDelivery.DELIVERY_COMPLETED -> handleCompleteDelivery(delivery);
            case StatusDelivery.DELIVERY_CANCELLED -> handleCancelDelivery(delivery);
            default -> throw new AppException(ErrorCode.INVALID_DELIVERY_STATUS);
        }

        delivery.setDeliveryStatus(newStatus);

        return deliveryMapper.toDTO(deliveryRepository.save(delivery));
    }

    private void handleStartDelivery(Delivery delivery) {
        if (delivery.getActualPickupTime() == null) {
            delivery.setActualPickupTime(LocalDateTime.now());
        }

        createInventoryTransaction(delivery, StatusDelivery.DELIVERING, "Delivery started: " + delivery.getCode());
    }

    private void handleCompleteDelivery(Delivery delivery) {
        delivery.setActualDeliveryTime(LocalDateTime.now());
        updateStatusVehicle(delivery, true);
        createInventoryTransaction(
                delivery, StatusDelivery.DELIVERY_COMPLETED, "Delivery completed: " + delivery.getCode());
    }

    private void handleCancelDelivery(Delivery delivery) {
        updateStatusVehicle(delivery, true);
        createInventoryTransaction(
                delivery, StatusDelivery.DELIVERY_CANCELLED, "Delivery cancelled: " + delivery.getCode());
    }

    public Page<DeliveryResponse> filter(DeliveryFilter filter, Pageable pageable) {
        DeliverySpecification spec = new DeliverySpecification(filter);
        Page<Delivery> deliveries = deliveryRepository.findAll(spec, pageable);
        return deliveries.map(deliveryMapper::toDTO);
    }

    private void updateStatusVehicle(Delivery delivery, Boolean available) {
        Vehicle vehicle = vehicleService.findByLicensePlate(delivery.getAssignedVehicle().getLicensePlate());
        vehicle.setAvailable(available);
        vehicleRepository.save(vehicle);
    }
}
