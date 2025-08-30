package com.cvv.scm_link.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.InventoryTransactionAdjustmentRequest;
import com.cvv.scm_link.dto.request.InventoryTransactionRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.InventoryTransactionResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.InventoryTransactionService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/inventoryTransactions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryTransactionController
        extends BaseController<
                InventoryTransactionRequest, InventoryTransactionRequest, InventoryTransactionResponse, String> {

    InventoryTransactionService inventoryTransactionService;

    public InventoryTransactionController(
            BaseService<InventoryTransactionRequest, InventoryTransactionRequest, InventoryTransactionResponse, String>
                    baseService,
            InventoryTransactionService inventoryTransactionService) {
        super(baseService);
        this.inventoryTransactionService = inventoryTransactionService;
    }

    @PostMapping("/adjustment")
    APIResponse<InventoryTransactionResponse> adjustment(
            @RequestBody @Valid InventoryTransactionAdjustmentRequest request) {
        return APIResponse.<InventoryTransactionResponse>builder()
                .result(inventoryTransactionService.createByAdjusting(request))
                .build();
    }
}
