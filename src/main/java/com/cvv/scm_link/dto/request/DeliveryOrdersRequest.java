package com.cvv.scm_link.dto.request;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryOrdersRequest extends BaseDTO {
    String itemStatus;
    String orderId;
}
