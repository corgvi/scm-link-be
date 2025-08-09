package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;

import com.cvv.scm_link.dto.request.PermissionRequest;
import com.cvv.scm_link.dto.response.PermissionResponse;
import com.cvv.scm_link.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
