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
public class OrderFilter extends BaseFilter {
    String code;
    String customerName;
    String customerPhone;
    String orderStatus;
    String paymentStatus;
    String shippingCity;
    int toAmount;
    int fromAmount;
    String customer;
}
