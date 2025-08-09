package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.cvv.scm_link.dto.request.UserCreateRequest;
import com.cvv.scm_link.dto.request.UserUpdateRequest;
import com.cvv.scm_link.dto.response.UserResponse;
import com.cvv.scm_link.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);
}
