package com.cvv.scm_link.dto.filter;

import java.time.LocalDateTime;

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
public class DeliveryFilter extends BaseFilter {
    String code;
    String deliveryStatus;
    LocalDateTime toPickupTime;
    LocalDateTime fromPickupTime;
    LocalDateTime toDeliveryTime;
    LocalDateTime fromDeliveryTime;
    int toDistanceKm;
    int fromDistanceKm;
    int toDurationM;
    int fromDurationM;
    String shipper;
    String vehicle;
}
