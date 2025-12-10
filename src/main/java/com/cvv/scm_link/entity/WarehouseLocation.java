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
@Table(name = "warehouse_location")
public class WarehouseLocation extends BaseEntity {
    String locationCode;
    String locationType;
    int maxCapacityKg;
    boolean available;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    Warehouse warehouse;

    @OneToMany(mappedBy = "warehouseLocation", cascade = CascadeType.ALL)
    List<InventoryLocationDetail> inventoryLocationDetails;
}
