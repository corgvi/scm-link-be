package com.cvv.scm_link.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUserResponse {
    String id;
    String code;
    String sku;
    String origin;
    String name;
    Integer weight;
    String description;
    String imageUrl;
    List<InventoryBatchDTO> batches;
}
