package com.cvv.scm_link.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
public class User extends BaseEntity {
    @Column(unique = true, name = "username", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    String password;
    String email;
    String fullName;
    String phoneNumber;
    String address;
    Boolean isActive;
    LocalDate dob;

    @ManyToMany(fetch = FetchType.LAZY)
    Set<Role> roles;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<Order> orders;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    List<Vehicle> vehicles;

    @OneToMany(mappedBy = "assignedDriver", cascade = CascadeType.ALL)
    List<Delivery> deliveries;
}
