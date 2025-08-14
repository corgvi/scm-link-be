package com.cvv.scm_link.dto.response;

import com.cvv.scm_link.dto.BaseDTO;
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
public class InventoryLevelResponse extends BaseDTO {
    int quantityOnHand;
    int quantityReserved;
    int quantityAvailable;
    int minStockLevel;
    ProductResponse product;
    WarehouseResponse warehouse;
}
