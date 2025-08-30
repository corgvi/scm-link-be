package com.cvv.scm_link.dto.response;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseLocationResponse extends BaseDTO {
    String locationCode;
    String locationType;
    Integer maxCapacityKg;
    Boolean isAvailable;
    WarehouseResponse warehouse;
}
