package com.cvv.scm_link.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    public APIResponse<List<InventoryLocationDetailResponse>> findAll() {
        return APIResponse.<List<InventoryLocationDetailResponse>>builder()
                .result(inventoryLocationDetailService.findAllIncludeProduct())
                .build();
    }

    @GetMapping("/batchDetails/{productId}")
    public APIResponse<List<BatchDetailDTO>> getBatchDetailsByProductId(@PathVariable("productId") String productId) {
        return APIResponse.<List<BatchDetailDTO>>builder()
                .result(inventoryLocationDetailService.getBatchDetails(productId))
                .build();
    }
}
