package com.cvv.scm_link.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryResponse extends BaseDTO {
    String code;
    String deliveryStatus;
    LocalDateTime scheduledPickupTime;
    LocalDateTime actualPickupTime;
    LocalDateTime scheduledDeliveryTime;
    LocalDateTime actualDeliveryTime;
    int totalDistanceKm;
    int totalDurationMinutes;
    String note;
    UserResponse assignedDriver;
    VehicleResponse assignedVehicle;
    List<DeliveryOrdersResponse> deliveryOrders;
    double warehouseLatitude;
    double warehouseLongitude;
}
