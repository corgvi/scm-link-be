package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseLocationRequest {
    @NotBlank(message = "LOCATION_CODE_IS_REQUIRED")
    String locationCode;
    @NotBlank(message = "LOCATION_TYPE_IS_REQUIRED")
    String locationType;
    @NotNull(message = "MAX_CAPACITY_IS_REQUIRED")
    Integer maxCapacityKg;
    @NotNull(message = "IS_AVAILABLE_IS_REQUIRED")
    Boolean isAvailable;
    String warehouseId;
}
