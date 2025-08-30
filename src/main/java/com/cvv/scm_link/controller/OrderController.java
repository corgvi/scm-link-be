package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.OrderRequest;
import com.cvv.scm_link.dto.response.OrderResponse;
import com.cvv.scm_link.service.BaseService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController extends BaseController<OrderRequest, OrderRequest, OrderResponse, String> {
    public OrderController(BaseService<OrderRequest, OrderRequest, OrderResponse, String> baseService) {
        super(baseService);
    }
}
