package com.cvv.scm_link.controller;

import java.io.Serializable;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.service.BaseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseController<C, U, R, ID extends Serializable> {

    protected final BaseService<C, U, R, ID> baseService;

    @GetMapping
    public APIResponse<PageResponse<R>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        Page<R> result = baseService.findAll(pageable);
        return APIResponse.<PageResponse<R>>builder()
                .result(PageResponse.of(result))
                .build();
    }

    @GetMapping("/{id}")
    public APIResponse<R> findById(@PathVariable("id") ID id) {
        return APIResponse.<R>builder().result(baseService.findById(id)).build();
    }

    @PostMapping
    public APIResponse<R> save(@RequestBody @Valid C dto) {
        return APIResponse.<R>builder().result(baseService.create(dto)).build();
    }

    @PutMapping("/{id}")
    public APIResponse<R> update(@RequestBody @Valid U dto, @PathVariable ID id) {
        return APIResponse.<R>builder().result(baseService.update(dto, id)).build();
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> delete(@PathVariable ID id) {
        baseService.deleteById(id);
        return APIResponse.<Void>builder().build();
    }
}
