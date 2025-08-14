package com.cvv.scm_link.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseLocationResponse {
    String locationCode;
    String locationType;
    Integer maxCapacityKg;
    Boolean isAvailable;
    WarehouseResponse warehouse;
}
