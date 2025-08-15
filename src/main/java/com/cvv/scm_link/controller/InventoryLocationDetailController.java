package com.cvv.scm_link.controller;

import com.cvv.scm_link.dto.request.InventoryLevelRequest;
import com.cvv.scm_link.dto.request.InventoryLocationDetailRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.InventoryLevelResponse;
import com.cvv.scm_link.dto.response.InventoryLocationDetailResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.InventoryLocationDetailService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventoryLocationDetails")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryLocationDetailController extends BaseController<InventoryLocationDetailRequest, InventoryLocationDetailRequest, InventoryLocationDetailResponse, String> {
    InventoryLocationDetailService inventoryLocationDetailService;
    public InventoryLocationDetailController(BaseService<InventoryLocationDetailRequest, InventoryLocationDetailRequest, InventoryLocationDetailResponse, String> baseService, InventoryLocationDetailService inventoryLocationDetailService) {
        super(baseService);
        this.inventoryLocationDetailService = inventoryLocationDetailService;
    }

    @Override
    public APIResponse<List<InventoryLocationDetailResponse>> findAll() {
        return APIResponse.<List<InventoryLocationDetailResponse>>builder().result(inventoryLocationDetailService.findAllIncludeProduct()).build();
    }
}
