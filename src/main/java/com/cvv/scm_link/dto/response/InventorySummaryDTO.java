package com.cvv.scm_link.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventorySummaryDTO {
    String sku;
    String productName;
    Long totalQuantity;
    Long batchCount;
    Long totalValue;
    String warehouseName;
    String status;
    String productId;
    public InventorySummaryDTO(String sku, String name, Long quantityOnHand, Long batchCount, Long totalCost, String warehouseName, String stockStatus, String productId) {
        this.sku = sku;
        this.productName = name;
        this.totalQuantity = quantityOnHand;
        this.batchCount = batchCount;
        this.totalValue = totalCost;
        this.warehouseName = warehouseName;
        this.status = stockStatus;
        this.productId = productId;
    }
}
