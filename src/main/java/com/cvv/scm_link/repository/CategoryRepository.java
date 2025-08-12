package com.cvv.scm_link.repository;

import com.cvv.scm_link.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category, String>{
    Optional<Category> findByCode(String code);

    boolean existsByCode(String code);
}
