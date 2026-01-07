package com.cvv.scm_link.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.filter.UserFilter;
import com.cvv.scm_link.dto.request.UserCreateRequest;
import com.cvv.scm_link.dto.request.UserUpdateRequest;
import com.cvv.scm_link.dto.response.UserResponse;
import com.cvv.scm_link.entity.Role;
import com.cvv.scm_link.entity.User;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.UserMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.RoleRepository;
import com.cvv.scm_link.repository.UserRepository;
import com.cvv.scm_link.repository.specification.UserSpecification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService extends BaseServiceImpl<UserCreateRequest, UserUpdateRequest, UserResponse, User, String> {

    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public UserService(
            BaseRepository<User, String> baseRepository,
            BaseMapper<User, UserCreateRequest, UserUpdateRequest, UserResponse> baseMapper,
            UserRepository userRepository,
            UserMapper userMapper,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        super(baseRepository, baseMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(com.cvv.scm_link.enums.Role.USER.name()).ifPresent(roles::add);
        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toDTO(user);
    }

    @PreAuthorize("(authentication.name == #username) || hasRole('ADMIN')")
    @Override
    public UserResponse update(UserUpdateRequest request, String username) {

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateFromDTO(request, user);
        if (request.getRoles() != null) user.setRoles(new HashSet<>(roleRepository.findAllById(request.getRoles())));
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserResponse findById(String s) {
        return super.findById(s);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(String s) {
        super.deleteById(s);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getMyInfo() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userMapper.toDTO(user);
    }

    public Page<UserResponse> findByRole(String roleName, Pageable pageable) {
        return userRepository.findByRoleName(roleName, pageable).map(userMapper::toDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> filter(UserFilter filter, Pageable pageable) {
        UserSpecification spec = new UserSpecification(filter);
        return userRepository.findAll(spec, pageable).map(userMapper::toDTO);
    }
}
