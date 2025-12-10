package com.cvv.scm_link.entity;

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
@Table(name = "customer_order")
public class Order extends BaseEntity {
    @Column(unique = true, nullable = false)
    String code;

    @Column(nullable = false)
    String customerName;

    @Column(nullable = false)
    String customerPhone;

    String customerEmail;

    @Column(nullable = false)
    String shippingAddress;

    @Column(nullable = false)
    String shippingCity;

    @Column(nullable = false)
    double shippingLatitude;

    @Column(nullable = false)
    double shippingLongitude;
    //    SP = CP + (CP * Markup%) + Shipping Fee + Handling Fee + Tax
    //    Shipping Fee = Base Fee + (Rate per km * Distance) + (Rate per kg * Weight)
    @Column(nullable = false)
    long totalAmount;

    String orderStatus;
    String paymentStatus;
    String note;
    String warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    User customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    List<OrderItems> orderItems;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<DeliveryOrders> deliveryOrders;
}
