package com.cvv.scm_link.dto.request;

import com.cvv.scm_link.entity.InventoryLevel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryTransactionRequest {
    @NotBlank(message = "TRANSACTION_TYPE_IS_REQUIRED")
    String transactionType;
    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer quantityChange;
    @NotNull(message = "QUANTITY_IS_REQUIRED")
    Integer currentQuantity;
    String note;
    String inventoryLevelId;
}
