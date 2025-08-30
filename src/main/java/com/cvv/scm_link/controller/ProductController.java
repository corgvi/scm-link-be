package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.ProductCreateRequest;
import com.cvv.scm_link.dto.request.ProductUpdateRequest;
import com.cvv.scm_link.dto.response.ProductDetailsResponse;
import com.cvv.scm_link.service.BaseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController
        extends BaseController<ProductCreateRequest, ProductUpdateRequest, ProductDetailsResponse, String> {
    public ProductController(
            BaseService<ProductCreateRequest, ProductUpdateRequest, ProductDetailsResponse, String> baseService) {
        super(baseService);
    }
}
