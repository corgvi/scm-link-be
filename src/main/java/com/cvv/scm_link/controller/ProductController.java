package com.cvv.scm_link.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.ProductFilter;
import com.cvv.scm_link.dto.request.ProductCreateRequest;
import com.cvv.scm_link.dto.request.ProductUpdateRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.ProductDetailsResponse;
import com.cvv.scm_link.dto.response.ProductUserResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.ProductService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_STAFF')")
public class ProductController
        extends BaseController<ProductCreateRequest, ProductUpdateRequest, ProductDetailsResponse, String> {

    ProductService productService;

    public ProductController(
            BaseService<ProductCreateRequest, ProductUpdateRequest, ProductDetailsResponse, String> baseService,
            ProductService productService) {
        super(baseService);
        this.productService = productService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/filter")
    APIResponse<PageResponse<ProductDetailsResponse>> filter(
            ProductFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        return APIResponse.<PageResponse<ProductDetailsResponse>>builder()
                .result(PageResponse.of(productService.filter(filter, pageable)))
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public APIResponse<List<ProductUserResponse>> getAllProductsWithPrice() {
        return APIResponse.<List<ProductUserResponse>>builder()
                .result(productService.getProductsWithStockAndPrice())
                .build();
    }
}
