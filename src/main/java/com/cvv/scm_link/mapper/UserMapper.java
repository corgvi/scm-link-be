package com.cvv.scm_link.mapper;

import org.mapstruct.*;

import com.cvv.scm_link.dto.request.UserCreateRequest;
import com.cvv.scm_link.dto.request.UserUpdateRequest;
import com.cvv.scm_link.dto.response.UserResponse;
import com.cvv.scm_link.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserCreateRequest, UserUpdateRequest, UserResponse> {

    @Override
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "isActive", source = "isActive")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(UserUpdateRequest dto, @MappingTarget User entity);
}
