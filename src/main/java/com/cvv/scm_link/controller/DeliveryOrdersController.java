package com.cvv.scm_link.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.request.DeliveryOrdersRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.DeliveryOrdersResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.DeliveryOrdersService;

import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/deliveryOrders")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryOrdersController
        extends BaseController<DeliveryOrdersRequest, DeliveryOrdersRequest, DeliveryOrdersResponse, String> {

    DeliveryOrdersService deliveryOrdersService;

    public DeliveryOrdersController(
            BaseService<DeliveryOrdersRequest, DeliveryOrdersRequest, DeliveryOrdersResponse, String> baseService,
            DeliveryOrdersService deliveryOrdersService) {
        super(baseService);
        this.deliveryOrdersService = deliveryOrdersService;
    }

    @GetMapping("/byDelivery/{id}")
    public APIResponse<PageResponse<DeliveryOrdersResponse>> findOrdersByDeliveryId(
            @PathVariable String id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        Page<DeliveryOrdersResponse> result = deliveryOrdersService.getDeliveryOrdersByDeliveryId(id, pageable);
        return APIResponse.<PageResponse<DeliveryOrdersResponse>>builder()
                .result(PageResponse.of(result))
                .build();
    }
}
