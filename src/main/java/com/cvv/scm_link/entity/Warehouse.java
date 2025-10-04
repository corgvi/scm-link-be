package com.cvv.scm_link.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

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
public class Warehouse extends BaseEntity {
    String name;
    String address;
    String contactPhone;
    double latitude;
    double longitude;
    boolean active;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    List<InventoryLevel> inventoryLevels;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    List<WarehouseLocation> warehouseLocations;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    List<ReceivingNote> receivingNotes;

    @OneToMany(mappedBy = "pickupWarehouse", cascade = CascadeType.ALL)
    List<Delivery> deliveries;
}
