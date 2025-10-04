package com.cvv.scm_link.controller;

import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.InventorySummaryDTO;
import com.cvv.scm_link.service.InventoryLevelService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.InventoryLevelRequest;
import com.cvv.scm_link.dto.response.InventoryLevelResponse;
import com.cvv.scm_link.service.BaseService;

import java.util.List;

@RestController
@RequestMapping("/inventoryLevels")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryLevelController
        extends BaseController<InventoryLevelRequest, InventoryLevelRequest, InventoryLevelResponse, String> {

    InventoryLevelService inventoryLevelService;

    public InventoryLevelController(
            BaseService<InventoryLevelRequest, InventoryLevelRequest, InventoryLevelResponse, String> baseService, InventoryLevelService inventoryLevelService) {
        super(baseService);
        this.inventoryLevelService = inventoryLevelService;
    }

    @GetMapping("/summary")
    public APIResponse<List<InventorySummaryDTO>> getInventoryLevels() {
        return  APIResponse.<List<InventorySummaryDTO>>builder()
                .result(inventoryLevelService.getInventorySummary())
                .build();
    }
}
