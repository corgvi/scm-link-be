package com.cvv.scm_link.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.WarehouseLocationRequest;
import com.cvv.scm_link.dto.response.WarehouseLocationResponse;
import com.cvv.scm_link.service.BaseService;

@RestController
@RequestMapping("/warehouseLocations")
@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
public class WarehouseLocationController
        extends BaseController<WarehouseLocationRequest, WarehouseLocationRequest, WarehouseLocationResponse, String> {
    public WarehouseLocationController(
            BaseService<WarehouseLocationRequest, WarehouseLocationRequest, WarehouseLocationResponse, String>
                    baseService) {
        super(baseService);
    }
}
