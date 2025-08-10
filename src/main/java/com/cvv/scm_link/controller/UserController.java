package com.cvv.scm_link.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.request.UserCreateRequest;
import com.cvv.scm_link.dto.request.UserUpdateRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.UserResponse;
import com.cvv.scm_link.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping("/my-info")
    public APIResponse<UserResponse> getMyInfo() {
        return APIResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping
    public APIResponse<List<UserResponse>> getUsers() {
        return APIResponse.<List<UserResponse>>builder()
                .result(userService.findAll())
                .build();
    }

    @GetMapping("/{id}")
    public APIResponse<UserResponse> getUserById(@PathVariable String id) {
        return APIResponse.<UserResponse>builder()
                .result(userService.findById(id))
                .build();
    }

    @PostMapping
    public APIResponse<UserResponse> create(@RequestBody @Valid UserCreateRequest request) {
        return APIResponse.<UserResponse>builder()
                .result(userService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public APIResponse<UserResponse> update(@RequestBody @Valid UserUpdateRequest request, @PathVariable String id) {
        return APIResponse.<UserResponse>builder()
                .result(userService.update(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> delete(@PathVariable String id) {
        userService.deleteById(id);
        return APIResponse.<Void>builder().build();
    }
}
