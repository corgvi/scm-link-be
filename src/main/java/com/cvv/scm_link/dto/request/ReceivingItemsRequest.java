package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceivingItemsRequest {
    String productId;
    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer quantity;
    @NotBlank(message = "BATCH_NUMBER_IS_REQUIRED")
    String batchNumber;
    LocalDate expiryDate;
    @NotNull(message = "COST_PRICE_IS_REQUIRED")
    Long costPrice;
    String warehouseLocationId;
}
