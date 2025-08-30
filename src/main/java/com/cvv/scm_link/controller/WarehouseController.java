package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.WarehouseRequest;
import com.cvv.scm_link.dto.response.WarehouseResponse;
import com.cvv.scm_link.service.BaseService;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController extends BaseController<WarehouseRequest, WarehouseRequest, WarehouseResponse, String> {
    public WarehouseController(BaseService<WarehouseRequest, WarehouseRequest, WarehouseResponse, String> baseService) {
        super(baseService);
    }
}
