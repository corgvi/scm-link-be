package com.cvv.scm_link.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.WarehouseFilter;
import com.cvv.scm_link.dto.request.WarehouseRequest;
import com.cvv.scm_link.dto.request.WarehouseUpdateRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.WarehouseResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.WarehouseService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/warehouses")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
public class WarehouseController
        extends BaseController<WarehouseRequest, WarehouseUpdateRequest, WarehouseResponse, String> {

    WarehouseService warehouseService;

    public WarehouseController(
            BaseService<WarehouseRequest, WarehouseUpdateRequest, WarehouseResponse, String> baseService,
            WarehouseService warehouseService) {
        super(baseService);
        this.warehouseService = warehouseService;
    }

    @GetMapping("/filter")
    public APIResponse<PageResponse<WarehouseResponse>> filter(
            WarehouseFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        return APIResponse.<PageResponse<WarehouseResponse>>builder()
                .result(PageResponse.of(warehouseService.filter(filter, pageable)))
                .build();
    }
}
