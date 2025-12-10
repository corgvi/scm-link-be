package com.cvv.scm_link.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.TransactionFilter;
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

    @GetMapping("/relatedEntity/{id}")
    APIResponse<PageResponse<InventoryTransactionResponse>> getByRelatedEntityId(
            @PathVariable String id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = (page - 1) * size;

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        Page<InventoryTransactionResponse> result = inventoryTransactionService.findAllByRelateEntityId(id, pageable);

        return APIResponse.<PageResponse<InventoryTransactionResponse>>builder()
                .result(PageResponse.of(result))
                .build();
    }

    @GetMapping("/filter")
    APIResponse<PageResponse<InventoryTransactionResponse>> filter(
            TransactionFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = (page - 1) * size;

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        Page<InventoryTransactionResponse> result = inventoryTransactionService.filter(filter, pageable);

        return APIResponse.<PageResponse<InventoryTransactionResponse>>builder()
                .result(PageResponse.of(result))
                .build();
    }
}
