package com.cvv.scm_link.dto.response;

import java.time.LocalDateTime;

import com.cvv.scm_link.dto.BaseDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryLocationDetailResponse extends BaseDTO {
    Integer quantity;
    String batchNumber;
    LocalDateTime expiryDate;
    Long costPrice;
    WarehouseLocationResponse warehouse;
    ProductResponse product;
}
