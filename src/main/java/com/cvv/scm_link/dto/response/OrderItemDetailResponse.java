package com.cvv.scm_link.dto.response;

import java.util.List;

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
public class OrderItemDetailResponse extends BaseDTO {
    int quantity;
    long priceAtOrder;
    String orderId;
    ProductResponse product;
    List<OrderItemBatchAllocationResponse> batchAllocations;
}
