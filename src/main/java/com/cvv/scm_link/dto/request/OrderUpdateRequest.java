package com.cvv.scm_link.dto.request;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
    String customerName;
    String customerPhone;
    String customerEmail;
    String shippingAddress;
    String shippingCity;
    String note;
    String orderStatus;
    String paymentStatus;
    List<OrderItemsRequest> items;
}
