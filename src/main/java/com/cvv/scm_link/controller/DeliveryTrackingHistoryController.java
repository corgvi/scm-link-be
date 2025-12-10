package com.cvv.scm_link.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.TrackingHistoryFilter;
import com.cvv.scm_link.dto.request.DeliveryTrackingHistoryRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.DeliveryTrackingHistoryResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.DeliveryTrackingHistoryService;

import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/deliveryTrackingHistories")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryTrackingHistoryController
        extends BaseController<
                DeliveryTrackingHistoryRequest,
                DeliveryTrackingHistoryRequest,
                DeliveryTrackingHistoryResponse,
                String> {

    DeliveryTrackingHistoryService trackingService;

    public DeliveryTrackingHistoryController(
            BaseService<
                            DeliveryTrackingHistoryRequest,
                            DeliveryTrackingHistoryRequest,
                            DeliveryTrackingHistoryResponse,
                            String>
                    baseService,
            DeliveryTrackingHistoryService deliveryTrackingHistoryService) {
        super(baseService);
        this.trackingService = deliveryTrackingHistoryService;
    }

    @GetMapping("/delivery/{deliveryId}")
    public APIResponse<PageResponse<DeliveryTrackingHistoryResponse>> getTrackingHistoriesByDeliveryId(
            @PathVariable String deliveryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        org.springframework.data.domain.Sort.Direction direction = sortParams.length > 1
                ? org.springframework.data.domain.Sort.Direction.fromString(sortParams[1])
                : org.springframework.data.domain.Sort.Direction.ASC;

        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageIndex, size, org.springframework.data.domain.Sort.by(direction, sortParams[0]));
        Page<DeliveryTrackingHistoryResponse> result =
                trackingService.getTrackingHistoryByDeliveryId(deliveryId, pageable);

        return APIResponse.<PageResponse<DeliveryTrackingHistoryResponse>>builder()
                .result(PageResponse.of(result))
                .build();
    }

    @GetMapping("/filter")
    public APIResponse<PageResponse<DeliveryTrackingHistoryResponse>> filter(
            TrackingHistoryFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        return APIResponse.<PageResponse<DeliveryTrackingHistoryResponse>>builder()
                .result(PageResponse.of(trackingService.filter(filter, pageable)))
                .build();
    }
}
