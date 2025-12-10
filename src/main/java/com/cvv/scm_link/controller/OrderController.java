package com.cvv.scm_link.controller;

import com.cvv.scm_link.dto.response.stats.RecentOrderResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.OrderFilter;
import com.cvv.scm_link.dto.request.OrderCreateRequest;
import com.cvv.scm_link.dto.request.OrderUpdateRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.OrderDetailResponse;
import com.cvv.scm_link.dto.response.OrderResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.OrderService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController
@RequestMapping("/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController extends BaseController<OrderCreateRequest, OrderUpdateRequest, OrderResponse, String> {

    OrderService orderService;

    public OrderController(
            BaseService<OrderCreateRequest, OrderUpdateRequest, OrderResponse, String> baseService,
            OrderService orderService) {
        super(baseService);
        this.orderService = orderService;
    }

    @GetMapping("/details/{id}")
    public APIResponse<OrderDetailResponse> findOrderDetailById(@PathVariable String id) {
        return APIResponse.<OrderDetailResponse>builder()
                .result(orderService.findOrderDetailById(id))
                .build();
    }

    @GetMapping("/filter")
    public APIResponse<PageResponse<OrderResponse>> filter(
            OrderFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        return APIResponse.<PageResponse<OrderResponse>>builder()
                .result(PageResponse.of(orderService.filter(filter, pageable)))
                .build();
    }

    @GetMapping("/recent-orders")
    public APIResponse<List<RecentOrderResponse>> getRecentOrders() {
        return APIResponse.<List<RecentOrderResponse>>builder()
                .result(orderService.getRecentOrders())
                .build();
    }
}
