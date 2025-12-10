package com.cvv.scm_link.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchDetailDTO {
    String batchNumber;
    LocalDateTime createdAt;
    LocalDate expiryDate;
    int quantity;
    int quantityAvailable;
    Long costPrice;
    Long sellPrice;
    Long totalCost;
    String locationCode;

    public BatchDetailDTO(
            String batchNumber,
            LocalDateTime createdAt,
            LocalDate expiryDate,
            int quantity,
            int quantityAvailable,
            Long costPrice,
            Long sellPrice,
            Long totalCost,
            String locationCode) {
        this.batchNumber = batchNumber;
        this.createdAt = createdAt;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.quantityAvailable = quantityAvailable;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.totalCost = totalCost;
        this.locationCode = locationCode;
    }
}
