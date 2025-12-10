package com.cvv.scm_link.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.SupplierFilter;
import com.cvv.scm_link.dto.request.SupplierRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.SupplierResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.SupplierService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/suppliers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierController extends BaseController<SupplierRequest, SupplierRequest, SupplierResponse, String> {

    SupplierService supplierService;

    public SupplierController(
            BaseService<SupplierRequest, SupplierRequest, SupplierResponse, String> baseService,
            SupplierService supplierService) {
        super(baseService);
        this.supplierService = supplierService;
    }

    @GetMapping("/filter")
    public APIResponse<PageResponse<SupplierResponse>> filter(
            SupplierFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        return APIResponse.<PageResponse<SupplierResponse>>builder()
                .result(PageResponse.of(supplierService.filter(filter, pageable)))
                .build();
    }
}
