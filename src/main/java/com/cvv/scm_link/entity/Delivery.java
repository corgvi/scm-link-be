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
    @Column(unique = true, nullable = false)
    String code;

    @Column(nullable = false)
    String deliveryStatus;

    @Column(nullable = false)
    LocalDateTime scheduledPickupTime;

    @Column(nullable = false)
    LocalDateTime actualPickupTime;

    @Column(nullable = false)
    LocalDateTime scheduledDeliveryTime;

    @Column(nullable = false)
    LocalDateTime actualDeliveryTime;

    @Column(nullable = false)
    int totalDistanceKm;

    @Column(nullable = false)
    int totalDurationMinutes;

    @Column(nullable = false)
    String note;

    String optimizedOrder;

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
