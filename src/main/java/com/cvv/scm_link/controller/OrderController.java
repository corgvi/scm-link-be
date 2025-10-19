package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.OrderCreateRequest;
import com.cvv.scm_link.dto.request.OrderUpdateRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.OrderDetailResponse;
import com.cvv.scm_link.dto.response.OrderResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.OrderService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController extends BaseController<OrderCreateRequest, OrderUpdateRequest, OrderResponse, String> {

    OrderService orderService;

    public OrderController(BaseService<OrderCreateRequest, OrderUpdateRequest, OrderResponse, String> baseService, OrderService orderService) {
        super(baseService);
        this.orderService = orderService;
    }


    @GetMapping("/details/{id}")
    public APIResponse<OrderDetailResponse> findOrderDetailById(@PathVariable String id) {
        return APIResponse.<OrderDetailResponse>builder()
                .result(orderService.findOrderDetailById(id))
                .build();
    }
}
