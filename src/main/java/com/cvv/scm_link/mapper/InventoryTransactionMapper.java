package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cvv.scm_link.dto.request.InventoryTransactionAdjustmentRequest;
import com.cvv.scm_link.dto.request.InventoryTransactionRequest;
import com.cvv.scm_link.dto.response.InventoryTransactionResponse;
import com.cvv.scm_link.entity.InventoryTransaction;

@Mapper(componentModel = "spring")
public interface InventoryTransactionMapper
        extends BaseMapper<
                InventoryTransaction,
                InventoryTransactionRequest,
                InventoryTransactionRequest,
                InventoryTransactionResponse> {
    @Override
    @Mapping(target = "inventoryLevel", ignore = true)
    InventoryTransactionResponse toDTO(InventoryTransaction entity);

    @Mapping(target = "inventoryLevel", ignore = true)
    InventoryTransaction toEntityFromAdjustment(InventoryTransactionAdjustmentRequest dto);
}
