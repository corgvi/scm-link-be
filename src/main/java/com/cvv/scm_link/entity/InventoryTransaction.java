package com.cvv.scm_link.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class InventoryTransaction extends BaseEntity {
    String transactionType;
    int quantityChange;
    int currentQuantity;
    String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryLevel_id")
    InventoryLevel inventoryLevel;
}
