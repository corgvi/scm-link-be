package com.cvv.scm_link.controller;

import com.cvv.scm_link.dto.request.ProductCreateRequest;
import com.cvv.scm_link.dto.request.ProductUpdateRequest;
import com.cvv.scm_link.dto.response.ProductResponse;
import com.cvv.scm_link.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController extends BaseController<ProductCreateRequest, ProductUpdateRequest, ProductResponse, String>{
    public ProductController(BaseService<ProductCreateRequest, ProductUpdateRequest, ProductResponse, String> baseService) {
        super(baseService);
    }
}
