package com.cvv.scm_link.mapper;

import com.cvv.scm_link.dto.request.InventoryTransactionRequest;
import com.cvv.scm_link.dto.response.InventoryTransactionResponse;
import com.cvv.scm_link.entity.InventoryTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryTransactionMapper extends BaseMapper<InventoryTransaction, InventoryTransactionRequest, InventoryTransactionRequest, InventoryTransactionResponse>{
}
