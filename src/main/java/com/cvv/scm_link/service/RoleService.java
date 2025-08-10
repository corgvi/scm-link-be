package com.cvv.scm_link.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.request.RoleRequest;
import com.cvv.scm_link.dto.response.RoleResponse;
import com.cvv.scm_link.entity.Permission;
import com.cvv.scm_link.entity.Role;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.RoleMapper;
import com.cvv.scm_link.repository.PermissionRepository;
import com.cvv.scm_link.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class RoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest roleRequest) {
        if (roleRepository.existsById(roleRequest.getName())) throw new AppException(ErrorCode.ROLE_EXISTED);
        Role role = roleMapper.toRole(roleRequest);
        var permissions = new HashSet<Permission>();
        roleRequest.getPermissions().stream().forEach(permission -> {
            permissionRepository.findById(permission).ifPresent(permissions::add);
        });
        role.setPermissions(permissions);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public RoleResponse update(RoleRequest roleRequest, String roleName) {
        Role role = roleRepository.findById(roleName).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roleMapper.updateRoleFromRequest(roleRequest, role);
        var permissions = new HashSet<Permission>();
        roleRequest.getPermissions().stream().forEach(permission -> {
            permissionRepository.findById(permission).ifPresent(permissions::add);
        });
        role.setName(roleName);
        role.setPermissions(permissions);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public void delete(String roleName) {
        if (!roleRepository.existsById(roleName)) throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        roleRepository.deleteById(roleName);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }
}
