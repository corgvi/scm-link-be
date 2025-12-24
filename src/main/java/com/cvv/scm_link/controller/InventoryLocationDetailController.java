package com.cvv.scm_link.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.request.InventoryLocationDetailRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.BatchDetailDTO;
import com.cvv.scm_link.dto.response.InventoryLocationDetailResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.InventoryLocationDetailService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/inventoryLocationDetails")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('WAREHOUSE_STAFF') or hasRole('ADMIN')")
public class InventoryLocationDetailController
        extends BaseController<
                InventoryLocationDetailRequest,
                InventoryLocationDetailRequest,
                InventoryLocationDetailResponse,
                String> {
    InventoryLocationDetailService inventoryLocationDetailService;

    public InventoryLocationDetailController(
            BaseService<
                            InventoryLocationDetailRequest,
                            InventoryLocationDetailRequest,
                            InventoryLocationDetailResponse,
                            String>
                    baseService,
            InventoryLocationDetailService inventoryLocationDetailService) {
        super(baseService);
        this.inventoryLocationDetailService = inventoryLocationDetailService;
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public APIResponse<PageResponse<InventoryLocationDetailResponse>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        Page<InventoryLocationDetailResponse> result = inventoryLocationDetailService.findAllIncludeProduct(pageable);
        return APIResponse.<PageResponse<InventoryLocationDetailResponse>>builder()
                .result(PageResponse.of(result))
                .build();
    }

    @GetMapping("/batchDetails/{productId}")
    public APIResponse<List<BatchDetailDTO>> getBatchDetailsByProductId(@PathVariable("productId") String productId) {
        return APIResponse.<List<BatchDetailDTO>>builder()
                .result(inventoryLocationDetailService.getBatchDetails(productId))
                .build();
    }

    @GetMapping("/batchDetails/{productId}/{warehouseId}")
    public APIResponse<List<BatchDetailDTO>> getBatchDetailsByProductAndWarehouse(
            @PathVariable("productId") String productId, @PathVariable("warehouseId") String warehouseId) {
        return APIResponse.<List<BatchDetailDTO>>builder()
                .result(inventoryLocationDetailService.getBatchDetails(productId, warehouseId))
                .build();
    }
}
