package com.cvv.scm_link.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryCreateRequest extends BaseDTO {
    String note;
    String driverId;
    String vehicleLicensePlate;
    LocalDate scheduledPickupTime;
    List<DeliveryOrdersRequest> deliveryOrders;
}
