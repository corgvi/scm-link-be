package com.cvv.scm_link.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryTransactionResponse {
    String transactionType;
    Integer quantityChange;
    Integer currentQuantity;
    String note;
    InventoryLevelResponse inventoryLevel;
}
