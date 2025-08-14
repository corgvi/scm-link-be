package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "FIELD_REQUIRED")
    Integer totalItemsExpected;
    @NotBlank(message = "FIELD_REQUIRED")
    List<ReceivingItemsRequest> products;

}
