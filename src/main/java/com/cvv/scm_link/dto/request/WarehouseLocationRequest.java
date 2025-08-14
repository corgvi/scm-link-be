package com.cvv.scm_link.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseLocationRequest {
    String locationCode;
    String locationType;
    Integer maxCapacityKg;
    Boolean isAvailable;
    String warehouseId;
}
