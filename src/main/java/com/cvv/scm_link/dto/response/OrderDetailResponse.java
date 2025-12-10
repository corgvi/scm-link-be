package com.cvv.scm_link.dto.response;

import java.util.List;

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
public class OrderDetailResponse extends BaseDTO {
    String orderCode;
    String customerName;
    String customerPhone;
    String customerEmail;
    String shippingAddress;
    String shippingCity;
    long totalAmount;
    String orderStatus;
    String paymentStatus;
    String note;
    List<OrderItemDetailResponse> orderItems;
}
