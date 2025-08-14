package com.cvv.scm_link.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryLevelRequest {
    int quantityOnHand;
    int quantityReserved;
    int quantityAvailable;
    int minStockLevel;
    String warehouse_id;
    String product_id;
}
