package com.cvv.scm_link.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    @Column(nullable = false, unique = true, name = "sku", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String sku;
    String name;
    String description;
    String imageUrl;
    String weightG;
    String lengthCm;
    String widthCm;
    String heightCm;
    String branchName;
    String color;
    String size;
    @Column(nullable = false, unique = true, name = "code", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<InventoryLevel> inventoryLevels;
}
