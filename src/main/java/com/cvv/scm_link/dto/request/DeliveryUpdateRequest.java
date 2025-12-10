package com.cvv.scm_link.dto.request;

import java.util.List;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryUpdateRequest extends BaseDTO {
    String deliveryStatus;
    String note;
    String driverId;
    String vehicleLicensePlate;
    List<DeliveryOrdersRequest> deliveryOrders;
}
