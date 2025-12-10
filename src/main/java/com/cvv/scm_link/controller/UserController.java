package com.cvv.scm_link.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.cvv.scm_link.dto.PageResponse;
import com.cvv.scm_link.dto.filter.UserFilter;
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

    @GetMapping("/role/{role}")
    public APIResponse<PageResponse<UserResponse>> getUsersByRole(
            @PathVariable String role,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));
        Page<UserResponse> users = userService.findByRole(role, pageable);
        return APIResponse.<PageResponse<UserResponse>>builder()
                .result(PageResponse.of(users))
                .build();
    }

    @GetMapping("/filter")
    public APIResponse<PageResponse<UserResponse>> filter(
            UserFilter userFilter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        int pageIndex = Math.max(page - 1, 0);

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(direction, sortParams[0]));

        return APIResponse.<PageResponse<UserResponse>>builder()
                .result(PageResponse.of(userService.filter(userFilter, pageable)))
                .build();
    }
}
