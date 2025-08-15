package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
