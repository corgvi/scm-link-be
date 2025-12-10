package com.cvv.scm_link.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventorySummaryDTO {
    String sku;
    String productName;
    Long totalQuantity;
    Long batchCount;
    Long totalValue;
    String warehouseId;
    String status;
    String productId;

    public InventorySummaryDTO(
            String sku,
            String name,
            Long quantityOnHand,
            Long batchCount,
            Long totalCost,
            String warehouseId,
            String stockStatus,
            String productId) {
        this.sku = sku;
        this.productName = name;
        this.totalQuantity = quantityOnHand;
        this.batchCount = batchCount;
        this.totalValue = totalCost;
        this.warehouseId = warehouseId;
        this.status = stockStatus;
        this.productId = productId;
    }
}
