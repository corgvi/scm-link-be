package com.cvv.scm_link.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    @NotBlank(message = "NAME_INVALID")
    String name;
    String description;
    String imageUrl;
    String weightG;
    String lengthCm;
    String widthCm;
    String heightCm;
    @NotBlank(message = "BRANCH_NAME_INVALID")
    String branchName;
    String color;
    @NotBlank(message = "CATEGORY_ID_INVALID")
    String categoryCode;
    @NotBlank(message = "SUPPLIER_ID_INVALID")
    String supplierCode;
}
