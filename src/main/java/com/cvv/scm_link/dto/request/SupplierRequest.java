package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierRequest {
    @NotBlank(message = "NAME_INVALID")
    String name;

    String contactPerson;
    String email;
    String address;

    @Pattern(regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
    String phoneNumber;

    String taxId;
    String note;

    @Size(min = 3, max = 3, message = "CODE_INVALID")
    String code;
}
