package com.cvv.scm_link.controller;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.service.BaseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseController<C, U, R, ID extends Serializable> {

    protected final BaseService<C, U, R, ID> baseService;

    @GetMapping
    public APIResponse<List<R>> findAll() {
        return APIResponse.<List<R>>builder().result(baseService.findAll()).build();
    }

    @GetMapping("/{id}")
    public APIResponse<R> findById(@RequestParam ID id) {
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
