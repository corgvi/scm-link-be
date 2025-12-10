package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleCreateRequest extends BaseDTO {
    @NotBlank(message = "LICENSE_PLATE_INVALID")
    String licensePlate;

    @NotBlank(message = "TYPE_INVALID")
    String type;

    @NotNull(message = "CAPACITY_INVALID")
    Integer capacityKg;
}
