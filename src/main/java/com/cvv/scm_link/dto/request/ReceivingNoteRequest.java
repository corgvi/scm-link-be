package com.cvv.scm_link.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceivingNoteRequest {
    String warehouse_id;
    String supplier_id;

    @NotNull(message = "TOTAL_ITEMS_IS_REQUIRED")
    Integer totalItemsExpected;

    List<ReceivingItemsRequest> products;
}
