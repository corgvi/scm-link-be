package com.cvv.scm_link.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.User;

@Repository
public interface UserRepository extends BaseRepository<User, String> {
    Optional<User> findByUsernameAndIsActive(String username, Boolean isActive);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    // Trong UserRepository (hoặc một Repository tùy chỉnh)
    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.id = :userId AND r.name = :roleName")
    Optional<User> findByIdAndRoleName(@Param("userId") String userId, @Param("roleName") String roleName);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u")
    long countCustomers();

    @Query(
            """
		SELECT COUNT(u) FROM User u
		WHERE MONTH(u.createdAt) = MONTH(CURRENT_DATE)
		AND YEAR(u.createdAt) = YEAR(CURRENT_DATE)
	""")
    long countNewCustomersThisMonth();

    @Query(
            """
		SELECT COUNT(u) FROM User u
		WHERE MONTH(u.createdAt) = MONTH(CURRENT_DATE) - 1
		AND YEAR(u.createdAt) = YEAR(CURRENT_DATE)
	""")
    long countNewCustomersLastMonth();
}
