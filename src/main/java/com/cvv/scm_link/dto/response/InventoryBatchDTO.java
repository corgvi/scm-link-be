package com.cvv.scm_link.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryBatchDTO {
    Long sellPrice;
    Long totalAvailable;
}
