package com.cvv.scm_link.controller;

import com.cvv.scm_link.dto.request.WarehouseRequest;
import com.cvv.scm_link.dto.response.WarehouseResponse;
import com.cvv.scm_link.entity.Warehouse;
import com.cvv.scm_link.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/warehouses")
public class WarehouseController extends BaseController<WarehouseRequest, WarehouseRequest, WarehouseResponse, String>{
    public WarehouseController(BaseService<WarehouseRequest, WarehouseRequest, WarehouseResponse, String> baseService) {
        super(baseService);
    }
}
