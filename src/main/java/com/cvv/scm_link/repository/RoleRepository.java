package com.cvv.scm_link.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
