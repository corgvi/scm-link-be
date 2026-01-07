package com.cvv.scm_link.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class User extends BaseEntity implements UserDetails {
    @Column(unique = true, name = "username", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    String password;
    String email;
    String fullName;
    String phoneNumber;
    String address;
    String city;
    String imageUrl;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authorities = new ArrayList<SimpleGrantedAuthority>();

        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });

        return authorities;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive != null && this.isActive;
    }
}
