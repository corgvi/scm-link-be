package com.cvv.scm_link.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery extends BaseEntity {
    String deliveryStatus;
    LocalDateTime scheduledPickupTime;
    LocalDateTime actualPickupTime;
    LocalDateTime scheduledDeliveryTime;
    LocalDateTime actualDeliveryTime;
    int totalDistanceKm;
    int totalDurationMinutes;
    String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_driver_id")
    User assignedDriver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_vehicle_id")
    Vehicle assignedVehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_warehouse_id")
    Warehouse pickupWarehouse;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    List<DeliveryTrackingHistory> deliveryTrackingHistories;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    List<DeliveryOrders> deliveryOrders;
}
