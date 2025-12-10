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
public class OrderResponse extends BaseDTO {
    String customerName;
    String customerPhone;
    String customerEmail;
    String shippingAddress;
    String shippingCity;
    int totalAmount;
    String orderStatus;
    String paymentStatus;
    String note;
    String code;
    String warehouseId;

    UserResponse user;
}
