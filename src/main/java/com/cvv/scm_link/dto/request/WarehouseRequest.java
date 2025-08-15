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
public class WarehouseRequest {
    @NotBlank(message = "NAME_INVALID")
    String name;
    String address;
    String contactPhone;
    @NotBlank(message = "LATITUDE_INVALID")
    String latitude;
    @NotBlank(message = "LONGITUDE_INVALID")
    String longitude;
    @NotNull(message = "IS_AVAILABLE_IS_REQUIRED")
    Boolean isActive;
}
