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
public class WarehouseLocation extends BaseEntity{
    String locationCode;
    String locationType;
    Integer maxCapacityKg;
    Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    Warehouse warehouse;

    @OneToMany(mappedBy = "warehouseLocation", cascade = CascadeType.ALL)
    List<InventoryLocationDetail> inventoryLocationDetails;
}
