package com.cvv.scm_link.dto.response.stats;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlySalesDTO {
    List<String> months;
    List<Long> revenue; // doanh thu mỗi tháng
    List<Integer> orders; // số đơn hàng mỗi tháng
    List<Double> growthRate; // tăng trưởng %
}
