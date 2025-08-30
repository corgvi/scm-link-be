package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.SupplierRequest;
import com.cvv.scm_link.dto.response.SupplierResponse;
import com.cvv.scm_link.service.BaseService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/suppliers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierController extends BaseController<SupplierRequest, SupplierRequest, SupplierResponse, String> {
    public SupplierController(BaseService<SupplierRequest, SupplierRequest, SupplierResponse, String> baseService) {
        super(baseService);
    }
}
