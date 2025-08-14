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
public class InventoryLevel extends BaseEntity{
    int quantityOnHand;
    int quantityReserved;
    int quantityAvailable;
    int minStockLevel;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @OneToMany(mappedBy = "inventoryLevel", cascade = CascadeType.ALL)
    List<InventoryLocationDetail> inventoryLocationDetails;
}