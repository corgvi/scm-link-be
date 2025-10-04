package com.cvv.scm_link.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchDetailDTO {
    String batchNumber;
    LocalDateTime createdAt;
    LocalDate expiryDate;
    int quantity;
    Long costPrice;
    Long sellPrice;
    Long totalCost;

    public BatchDetailDTO(String batchNumber, LocalDateTime createdAt, LocalDate expiryDate, int quantity, Long costPrice, Long sellPrice, Long totalCost) {
        this.batchNumber = batchNumber;
        this.createdAt = createdAt;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.totalCost = totalCost;
    }
}
