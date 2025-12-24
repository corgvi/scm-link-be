package com.cvv.scm_link.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductBatchFlatDTO {
    String id;
    String code;
    String sku;
    String origin;
    String name;
    Integer weight;
    String description;
    String imageUrl;
    Long sellPrice;
    Long totalAvailable;

    public ProductBatchFlatDTO(String id, String code, String sku, String origin, String name, Integer weight, String description, String imageUrl, Long sellPrice, Long totalAvailable) {
        this.id = id;
        this.code = code;
        this.sku = sku;
        this.origin = origin;
        this.name = name;
        this.weight = weight;
        this.description = description;
        this.imageUrl = imageUrl;
        this.sellPrice = sellPrice;
        this.totalAvailable = totalAvailable;
    }
}
