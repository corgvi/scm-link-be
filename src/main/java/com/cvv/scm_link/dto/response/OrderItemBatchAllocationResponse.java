package com.cvv.scm_link.dto.response;

import java.time.LocalDate;

public interface OrderItemBatchAllocationResponse {
    String getBatchNumber();

    Integer getQuantityAllocated();

    LocalDate getExpirationDate();

    String getProductId();

    String getSku();

    String getProductName();
}
