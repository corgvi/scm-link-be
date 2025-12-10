package com.cvv.scm_link.dto.filter;

import com.cvv.scm_link.dto.BaseFilter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilter extends BaseFilter {
    String sku;
    String name;
    String branchName;
    String code;
    String category;
    String supplier;
}
