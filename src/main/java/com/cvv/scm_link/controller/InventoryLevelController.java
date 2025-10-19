package com.cvv.scm_link.controller;

import java.util.List;

import com.cvv.scm_link.dto.response.InventoryLocationDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.InventoryLevelRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.InventoryLevelResponse;
import com.cvv.scm_link.dto.response.InventorySummaryDTO;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.InventoryLevelService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/inventoryLevels")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryLevelController
        extends BaseController<InventoryLevelRequest, InventoryLevelRequest, InventoryLevelResponse, String> {

    InventoryLevelService inventoryLevelService;

    public InventoryLevelController(
            BaseService<InventoryLevelRequest, InventoryLevelRequest, InventoryLevelResponse, String> baseService,
            InventoryLevelService inventoryLevelService) {
        super(baseService);
        this.inventoryLevelService = inventoryLevelService;
    }

    @GetMapping("/summary")
    public APIResponse<Page<InventorySummaryDTO>> getInventoryLevels(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1
                ? Sort.Direction.fromString(sortParams[1])
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        Page<InventorySummaryDTO> result = inventoryLevelService.getInventorySummary(pageable);
        return APIResponse.<Page<InventorySummaryDTO>>builder()
                .result(result)
                .build();
    }
}
