package com.cvv.scm_link.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryLocationDetail extends BaseEntity {
    int quantity;
    String batchNumber;
    LocalDate expiryDate;
    Long costPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryLevel_id")
    InventoryLevel inventoryLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseLocation_id")
    WarehouseLocation warehouseLocation;
}
