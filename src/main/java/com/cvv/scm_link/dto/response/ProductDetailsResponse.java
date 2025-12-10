package com.cvv.scm_link.dto.response;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailsResponse extends BaseDTO {
    String sku;
    String name;
    String description;
    Boolean active;
    String imageUrl;
    String weightG;
    String lengthCm;
    String widthCm;
    String heightCm;
    String branchName;
    String color;
    String code;
    String size;
    CategoryResponse category;
    SupplierResponse supplier;
}
