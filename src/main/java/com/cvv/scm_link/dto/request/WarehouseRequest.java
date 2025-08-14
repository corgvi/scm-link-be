package com.cvv.scm_link.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseRequest {
    String name;
    String address;
    String contactPhone;
    String latitude;
    String longitude;
    Boolean isActive;
}
