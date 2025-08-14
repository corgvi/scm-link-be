package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryLocationDetailRequest {
    @NotBlank(message = "FIELD_REQUIRED")
    Integer quantity;
    @NotBlank(message = "FIELD_REQUIRED")
    String batchNumber;
    LocalDateTime expiryDate;
    @NotBlank(message = "FIELD_REQUIRED")
    Long costPrice;
    @NotBlank(message = "FIELD_REQUIRED")
    String inventoryLevelId;
    @NotBlank(message = "FIELD_REQUIRED")
    String warehouseLocationId;
}
