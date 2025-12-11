package com.cvv.scm_link.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String name;
    String code;
    Boolean active;
    String description;
    String imageUrl;
    String weightG;
    String lengthCm;
    String widthCm;
    String heightCm;
    String branchName;
    String size;
    String color;
    String categoryCode;
    String supplierCode;
}
