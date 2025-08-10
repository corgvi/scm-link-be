package com.cvv.scm_link.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.request.PermissionRequest;
import com.cvv.scm_link.dto.response.PermissionResponse;
import com.cvv.scm_link.entity.Permission;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.PermissionMapper;
import com.cvv.scm_link.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        if (permissionRepository.existsById(request.getName())) throw new AppException(ErrorCode.PERMISSION_EXISTED);
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public PermissionResponse update(PermissionRequest request, String name) {
        Permission permission = permissionRepository
                .findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        permissionMapper.updatePermissionFromRequest(request, permission);
        permission.setName(name);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        List<Permission> all = permissionRepository.findAll();
        return all.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission) {
        if (!permissionRepository.existsById(permission)) throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);
        permissionRepository.deleteById(permission);
    }
}
