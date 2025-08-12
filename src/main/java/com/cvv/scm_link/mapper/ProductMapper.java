package com.cvv.scm_link.mapper;

import com.cvv.scm_link.dto.request.ProductCreateRequest;
import com.cvv.scm_link.dto.request.ProductUpdateRequest;
import com.cvv.scm_link.dto.response.ProductResponse;
import com.cvv.scm_link.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, SupplierMapper.class})
public interface ProductMapper extends BaseMapper<Product, ProductCreateRequest, ProductUpdateRequest, ProductResponse>{
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "category" , ignore = true)
    @Mapping(target = "supplier",  ignore = true)
    @Mapping(target = "inventoryLevels", ignore = true)
    void updateFromDTO(ProductUpdateRequest dto, @MappingTarget Product entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "category" , ignore = true)
    @Mapping(target = "supplier",  ignore = true)
    @Mapping(target = "inventoryLevels", ignore = true)
    Product toEntity(ProductCreateRequest dto);
}
