package com.cvv.scm_link.dto.response.stats;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerStats {
    long totalCustomers;
    long newCustomersThisMonth;
    double growthRate;
}
