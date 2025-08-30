package com.cvv.scm_link.controller;

import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.request.UserCreateRequest;
import com.cvv.scm_link.dto.request.UserUpdateRequest;
import com.cvv.scm_link.dto.response.APIResponse;
import com.cvv.scm_link.dto.response.UserResponse;
import com.cvv.scm_link.service.BaseService;
import com.cvv.scm_link.service.UserService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController extends BaseController<UserCreateRequest, UserUpdateRequest, UserResponse, String> {

    UserService userService;

    public UserController(
            BaseService<UserCreateRequest, UserUpdateRequest, UserResponse, String> baseService,
            UserService userService) {
        super(baseService);
        this.userService = userService;
    }

    @GetMapping("/my-info")
    public APIResponse<UserResponse> getMyInfo() {
        return APIResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }
}
