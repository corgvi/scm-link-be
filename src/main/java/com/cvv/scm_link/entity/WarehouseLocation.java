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
public class WarehouseLocation extends BaseEntity {
    String locationCode;
    String locationType;
    int maxCapacityKg;
    boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    Warehouse warehouse;

    @OneToMany(mappedBy = "warehouseLocation", cascade = CascadeType.ALL)
    List<InventoryLocationDetail> inventoryLocationDetails;
}
