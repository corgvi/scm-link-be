package com.cvv.scm_link.dto.response.stats;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardResponse {
    CustomerStats customerStats;
    OrderStats orderStats;
    InventoryStats inventoryStats;
    RevenueStats revenueStats;
    DeliveryStats deliveryStats;
}
