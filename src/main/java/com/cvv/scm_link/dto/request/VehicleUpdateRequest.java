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
public class VehicleUpdateRequest extends BaseDTO {
    Integer capacityKg;

    Boolean available;
}
