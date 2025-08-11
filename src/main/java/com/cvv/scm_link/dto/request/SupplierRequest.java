package com.cvv.scm_link.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
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
    String phoneNumber;
    String taxId;
    String note;
    @Size(min = 3, max = 3, message = "CODE_INVALID")
    String code;
    Boolean isActive;
}
