package com.cvv.scm_link.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.Category;

@Repository
public interface CategoryRepository extends BaseRepository<Category, String> {
    Optional<Category> findByCode(String code);

    boolean existsByCode(String code);
}
