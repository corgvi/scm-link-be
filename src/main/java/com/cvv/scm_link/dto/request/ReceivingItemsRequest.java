package com.cvv.scm_link.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceivingItemsRequest {
    String productId;
    Integer quantity;
    String batchNumber;
    LocalDateTime expiryDate;
    Long costPrice;
    String warehouseLocationId;
}
