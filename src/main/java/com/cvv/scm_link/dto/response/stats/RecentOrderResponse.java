package com.cvv.scm_link.dto.response.stats;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecentOrderResponse {
    String id;
    String orderCode;

    String productName;
    String productImage;
    String category;
    Integer variants;

    BigDecimal totalAmount;
    String status;

    String customerName;
    LocalDateTime createdAt;
}
