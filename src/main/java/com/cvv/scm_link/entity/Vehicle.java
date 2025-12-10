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
public class Vehicle extends BaseEntity {
    @Column(unique = true, name = "license_plate", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String licensePlate;

    String type;
    int capacityKg;

    @Builder.Default
    boolean isAvailable = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    User driver;

    @OneToMany(mappedBy = "assignedVehicle", cascade = CascadeType.ALL)
    List<Delivery> deliveries;
}
