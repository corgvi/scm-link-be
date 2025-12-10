package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemsRequest {
    String productId;

    @Size(message = "QUANTITY_IS_REQUIRED", min = 1)
    Integer quantity;
}
