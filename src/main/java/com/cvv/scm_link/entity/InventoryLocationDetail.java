package com.cvv.scm_link.entity;

import java.time.LocalDate;
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
@Table(name = "inventory_location_detail")
public class InventoryLocationDetail extends BaseEntity {
    int quantity;
    int quantityAvailable;
    String batchNumber;

    LocalDate expiryDate;

    Long costPrice;
    Long sellPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryLevel_id")
    InventoryLevel inventoryLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseLocation_id")
    WarehouseLocation warehouseLocation;

    @OneToMany(mappedBy = "inventoryLocationDetail", cascade = CascadeType.ALL)
    List<OrderItemBatchAllocation> orderItemBatchAllocations;
}
