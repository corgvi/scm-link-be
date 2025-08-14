package com.cvv.scm_link.dto.request;

import com.cvv.scm_link.entity.InventoryLevel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryTransactionRequest {
    String transactionType;
    Integer quantityChange;
    Integer currentQuantity;
    String note;
    String inventoryLevelId;
}
