package com.cvv.scm_link.dto.response;

import com.cvv.scm_link.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
