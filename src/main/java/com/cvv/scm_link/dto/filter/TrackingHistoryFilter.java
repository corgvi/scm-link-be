package com.cvv.scm_link.dto.filter;

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
public class TrackingHistoryFilter extends BaseFilter {
    String statusCode;
    String deliveryCode;
}
