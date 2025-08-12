package com.cvv.scm_link.mapper;

import com.cvv.scm_link.dto.request.CategoryRequest;
import com.cvv.scm_link.dto.response.CategoryResponse;
import com.cvv.scm_link.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends BaseMapper<Category, CategoryRequest, CategoryRequest, CategoryResponse>{
}
