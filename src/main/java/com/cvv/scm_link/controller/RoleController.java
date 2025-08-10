package com.cvv.scm_link.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.request.RoleRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.RoleResponse;
import com.cvv.scm_link.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    RoleService roleService;

    @PostMapping
    public APIResponse<RoleResponse> create(@RequestBody @Valid RoleRequest roleRequest) {
        return APIResponse.<RoleResponse>builder()
                .result(roleService.create(roleRequest))
                .build();
    }

    @PutMapping("/{name}")
    public APIResponse<RoleResponse> update(@PathVariable String name, @RequestBody @Valid RoleRequest roleRequest) {
        return APIResponse.<RoleResponse>builder()
                .result(roleService.update(roleRequest, name))
                .build();
    }

    @DeleteMapping("/{name}")
    public APIResponse<Void> delete(@PathVariable String name) {
        roleService.delete(name);
        return APIResponse.<Void>builder().build();
    }

    @GetMapping
    public APIResponse<List<RoleResponse>> findAll() {
        return APIResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }
}
