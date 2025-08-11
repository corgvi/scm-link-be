package com.cvv.scm_link.service;

import com.cvv.scm_link.dto.request.CategoryRequest;
import com.cvv.scm_link.dto.response.CategoryResponse;
import com.cvv.scm_link.entity.Category;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService extends BaseServiceImpl<CategoryRequest, CategoryRequest, CategoryResponse, Category, String>{
    public CategoryService(BaseRepository<Category, String> baseRepository, BaseMapper<Category, CategoryRequest, CategoryRequest, CategoryResponse> baseMapper) {
        super(baseRepository, baseMapper);
    }
}
