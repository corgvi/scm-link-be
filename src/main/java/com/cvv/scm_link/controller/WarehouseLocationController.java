package com.cvv.scm_link.controller;

import com.cvv.scm_link.dto.request.WarehouseLocationRequest;
import com.cvv.scm_link.dto.response.WarehouseLocationResponse;
import com.cvv.scm_link.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/warehouseLocation")
public class WarehouseLocationController extends BaseController<WarehouseLocationRequest, WarehouseLocationRequest, WarehouseLocationResponse, String>{
    public WarehouseLocationController(BaseService<WarehouseLocationRequest, WarehouseLocationRequest, WarehouseLocationResponse, String> baseService) {
        super(baseService);
    }
}
