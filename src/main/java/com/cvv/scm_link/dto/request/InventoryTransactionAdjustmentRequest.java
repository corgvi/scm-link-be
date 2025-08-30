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
public class InventoryTransactionAdjustmentRequest {
    @NotBlank(message = "PRODUCT_IS_REQUIRED")
    String productId;

    @NotBlank(message = "WAREHOUSE_IS_REQUIRED")
    String warehouseId;

    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer quantityChange;

    String note;
}
