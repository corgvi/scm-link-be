package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryLevelRequest {
    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer quantityOnHand;

    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer quantityReserved;

    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer quantityAvailable;

    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer minStockLevel;

    @NotBlank(message = "WAREHOUSE_IS_REQUIRED")
    String warehouse_id;

    @NotBlank(message = "PRODUCT_IS_REQUIRED")
    String product_id;
}
