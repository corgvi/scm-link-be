package com.cvv.scm_link.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.VehicleFilter;
import com.cvv.scm_link.dto.request.VehicleCreateRequest;
import com.cvv.scm_link.dto.request.VehicleUpdateRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.VehicleResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.VehicleService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/vehicles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VehicleController
        extends BaseController<VehicleCreateRequest, VehicleUpdateRequest, VehicleResponse, String> {

    VehicleService vehicleService;

    public VehicleController(
            BaseService<VehicleCreateRequest, VehicleUpdateRequest, VehicleResponse, String> baseService,
            VehicleService vehicleService) {
        super(baseService);
        this.vehicleService = vehicleService;
    }

    @GetMapping("/available")
    public APIResponse<PageResponse<VehicleResponse>> getAvailableVehicles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        Page<VehicleResponse> result = vehicleService.findVehiclesByStatus(pageable);

        return APIResponse.<PageResponse<VehicleResponse>>builder()
                .result(PageResponse.of(result))
                .build();
    }

    @GetMapping("/filter")
    public APIResponse<PageResponse<VehicleResponse>> filter(
            VehicleFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        return APIResponse.<PageResponse<VehicleResponse>>builder()
                .result(PageResponse.of(vehicleService.filter(filter, pageable)))
                .build();
    }
}
