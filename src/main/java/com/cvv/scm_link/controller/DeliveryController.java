package com.cvv.scm_link.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.DeliveryFilter;
import com.cvv.scm_link.dto.request.DeliveryCreateRequest;
import com.cvv.scm_link.dto.request.DeliveryStatusUpdateRequest;
import com.cvv.scm_link.dto.request.DeliveryUpdateRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.DeliveryResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.DeliveryService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/deliveries")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryController
        extends BaseController<DeliveryCreateRequest, DeliveryUpdateRequest, DeliveryResponse, String> {
    DeliveryService deliveryService;

    public DeliveryController(
            BaseService<DeliveryCreateRequest, DeliveryUpdateRequest, DeliveryResponse, String> baseService,
            DeliveryService deliveryService) {
        super(baseService);
        this.deliveryService = deliveryService;
    }

    @PatchMapping("/{id}/status")
    public APIResponse<DeliveryResponse> updateStatus(
            @PathVariable String id, @RequestBody @Valid DeliveryStatusUpdateRequest dto) {
        return APIResponse.<DeliveryResponse>builder()
                .result(deliveryService.updateStatus(id, dto))
                .build();
    }

    @GetMapping("/filter")
    public APIResponse<PageResponse<DeliveryResponse>> filter(
            DeliveryFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        return APIResponse.<PageResponse<DeliveryResponse>>builder()
                .result(PageResponse.of(deliveryService.filter(filter, pageable)))
                .build();
    }
}
