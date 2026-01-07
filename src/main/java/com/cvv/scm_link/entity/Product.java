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
@Table(name = "product")
public class Product extends BaseEntity {
    @Column(nullable = false, unique = true, name = "sku", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String sku;

    String name;
    String description;
    String imageUrl;
    Integer weightG;
    String storageCondition;
    String origin;
    String color;
    boolean active;

    @Column(
            nullable = false,
            unique = true,
            name = "code",
            columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<InventoryLevel> inventoryLevels;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<OrderItems> orderItems;

    @PrePersist
    @PreUpdate
    public void normalizeCode() {
        this.code = (code == null) ? null : code.toUpperCase();
    }
}
