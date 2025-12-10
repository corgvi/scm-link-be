package com.cvv.scm_link.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.CategoryFilter;
import com.cvv.scm_link.dto.request.CategoryRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.CategoryResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.CategoryService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController extends BaseController<CategoryRequest, CategoryRequest, CategoryResponse, String> {

    CategoryService categoryService;

    public CategoryController(
            BaseService<CategoryRequest, CategoryRequest, CategoryResponse, String> baseService,
            CategoryService categoryService) {
        super(baseService);
        this.categoryService = categoryService;
    }

    @GetMapping("/filter")
    public APIResponse<PageResponse<CategoryResponse>> filter(
            CategoryFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        return APIResponse.<PageResponse<CategoryResponse>>builder()
                .result(PageResponse.of(categoryService.filter(filter, pageable)))
                .build();
    }
}
