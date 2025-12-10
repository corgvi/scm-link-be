package com.cvv.scm_link.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.filter.CategoryFilter;
import com.cvv.scm_link.dto.request.CategoryRequest;
import com.cvv.scm_link.dto.response.CategoryResponse;
import com.cvv.scm_link.entity.Category;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.CategoryRepository;
import com.cvv.scm_link.repository.specification.CategorySpecification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService
        extends BaseServiceImpl<CategoryRequest, CategoryRequest, CategoryResponse, Category, String> {

    CategoryRepository categoryRepository;

    public CategoryService(
            BaseRepository<Category, String> baseRepository,
            BaseMapper<Category, CategoryRequest, CategoryRequest, CategoryResponse> baseMapper,
            CategoryRepository categoryRepository) {
        super(baseRepository, baseMapper);
        this.categoryRepository = categoryRepository;
    }

    public Page<CategoryResponse> filter(CategoryFilter filter, Pageable pageable) {
        return categoryRepository
                .findAll(new CategorySpecification(filter), pageable)
                .map(baseMapper::toDTO);
    }
}
