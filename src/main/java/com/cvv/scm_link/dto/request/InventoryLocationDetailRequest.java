package com.cvv.scm_link.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryLocationDetailRequest {
    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer quantity;

    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer quantityAvailable;

    LocalDate expiryDate;

    @NotNull(message = "COST_PRICE_IS_REQUIRED")
    Long costPrice;

    @NotBlank(message = "INVENTORY_LEVEL_IS_REQUIRED")
    String inventoryLevelId;

    @NotBlank(message = "WAREHOUSE_IS_REQUIRED")
    String warehouseLocationId;
}
