package com.cvv.scm_link.entity;

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
@Table(name = "inventory_transaction")
public class InventoryTransaction extends BaseEntity {
    String transactionType;
    int quantityChange;
    int currentQuantity;
    String note;
    String relateEntityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryLevel_id")
    InventoryLevel inventoryLevel;
}
