package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvv.scm_link.dto.request.CategoryRequest;
import com.cvv.scm_link.dto.response.CategoryResponse;
import com.cvv.scm_link.service.BaseService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController extends BaseController<CategoryRequest, CategoryRequest, CategoryResponse, String> {
    public CategoryController(BaseService<CategoryRequest, CategoryRequest, CategoryResponse, String> baseService) {
        super(baseService);
    }
}
