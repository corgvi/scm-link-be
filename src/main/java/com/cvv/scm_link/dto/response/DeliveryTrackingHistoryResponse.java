package com.cvv.scm_link.dto.response;

import java.time.LocalDateTime;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryTrackingHistoryResponse extends BaseDTO {
    String statusCode;
    String statusDescription;
    double latitude;
    double longitude;
    String locationDescription;
    LocalDateTime timestamp;
}
