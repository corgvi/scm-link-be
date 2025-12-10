package com.cvv.scm_link.dto.response;

import java.time.LocalDateTime;

import com.cvv.scm_link.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryLocationDetailResponse extends BaseDTO {
    Integer quantity;
    String batchNumber;
    LocalDateTime expiryDate;
    Long costPrice;
    Long sellPrice;
    String warehouseName;
    WarehouseLocationResponse warehouse;
    ProductResponse product;
}
