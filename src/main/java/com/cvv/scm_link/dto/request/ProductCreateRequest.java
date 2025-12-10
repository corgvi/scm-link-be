package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {
    @NotBlank(message = "NAME_INVALID")
    String name;

    String description;
    String imageUrl;

    @NotBlank(message = "WEIGHT_INVALID")
    String weightG;

    @NotBlank(message = "CODE_INVALID")
    String code;

    String lengthCm;
    String widthCm;
    String heightCm;

    @NotBlank(message = "BRANCH_NAME_INVALID")
    String branchName;

    @Size(min = 1, max = 3, message = "SIZE_INVALID")
    String size;

    String color;

    @NotBlank(message = "CATEGORY_CODE_INVALID")
    String categoryCode;

    @NotBlank(message = "SUPPLIER_CODE_INVALID")
    String supplierCode;
}
