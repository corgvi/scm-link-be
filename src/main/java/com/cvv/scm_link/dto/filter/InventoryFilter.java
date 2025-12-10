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
public class InventoryFilter extends BaseFilter {
    int toQuantityOnHand;
    int fromQuantityOnHand;
    int toQuantityReserved;
    int fromQuantityReserved;
    int toQuantityAvailable;
    int fromQuantityAvailable;
    String product;
    String warehouse;
}
