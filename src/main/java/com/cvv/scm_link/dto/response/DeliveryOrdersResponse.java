package com.cvv.scm_link.dto.response;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryOrdersResponse extends BaseDTO {
    int orderSequence;
    String itemStatus;
    String orderId;
    double orderLatitude;
    double orderLongitude;
    String orderCode;
}
