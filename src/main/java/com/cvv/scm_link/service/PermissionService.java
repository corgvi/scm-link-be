package com.cvv.scm_link.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.request.PermissionRequest;
import com.cvv.scm_link.dto.response.PermissionResponse;
import com.cvv.scm_link.entity.Permission;
import com.cvv.scm_link.mapper.PermissionMapper;
import com.cvv.scm_link.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        List<Permission> all = permissionRepository.findAll();
        return all.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
