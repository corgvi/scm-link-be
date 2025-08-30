package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.InventoryLevelRequest;
import com.cvv.scm_link.dto.response.InventoryLevelResponse;
import com.cvv.scm_link.service.BaseService;

@RestController
@RequestMapping("/inventoryLevels")
public class InventoryLevelController
        extends BaseController<InventoryLevelRequest, InventoryLevelRequest, InventoryLevelResponse, String> {
    public InventoryLevelController(
            BaseService<InventoryLevelRequest, InventoryLevelRequest, InventoryLevelResponse, String> baseService) {
        super(baseService);
    }
}
