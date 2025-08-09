package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.cvv.scm_link.dto.request.RoleRequest;
import com.cvv.scm_link.dto.response.RoleResponse;
import com.cvv.scm_link.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    void updateRoleFromRequest(RoleRequest request, @MappingTarget Role role);
}
