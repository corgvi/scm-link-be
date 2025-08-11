package com.cvv.scm_link.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.User;

@Repository
public interface UserRepository extends BaseRepository<User, String> {
    Optional<User> findByUsernameAndIsActive(String username, Boolean isActive);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
