package com.cvv.scm_link.controller;

import com.cvv.scm_link.dto.response.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.request.PermissionRequest;
import com.cvv.scm_link.dto.response.PermissionResponse;
import com.cvv.scm_link.service.PermissionService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {


    PermissionService permissionService;

    @PostMapping
    APIResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return APIResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    APIResponse<List<PermissionResponse>> getAll() {
        return APIResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    APIResponse<Void> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        return APIResponse.<Void>builder().build();
    }
}
