package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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

    @NotBlank(message = "ADDRESS_IS_REQUIRED")
    String address;

    @Pattern(regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
    String contactPhone;
}
