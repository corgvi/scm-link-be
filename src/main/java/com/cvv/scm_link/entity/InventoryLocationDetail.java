package com.cvv.scm_link.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryLocationDetail extends BaseEntity {
    Integer quantity;
    String batchNumber;
    LocalDateTime expiryDate;
    Long costPrice;

    @ManyToOne
    @JoinColumn(name = "inventoryLevel_id")
    InventoryLevel inventoryLevel;

    @ManyToOne
    @JoinColumn(name = "warehouseLocation_id")
    WarehouseLocation warehouseLocation;
}
