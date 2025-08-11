package com.cvv.scm_link.entity;

import jakarta.persistence.Entity;
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
public class InventoryTransactions extends BaseEntity {
    String transactionType;
    Integer quantityChange;
    Integer currentQuantity;
    String note;

    @ManyToOne
    @JoinColumn(name = "inventoryLevel_id")
    InventoryLevel inventoryLevel;
}
