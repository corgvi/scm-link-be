package com.cvv.scm_link.mapper;

import com.cvv.scm_link.dto.request.ProductCreateRequest;
import com.cvv.scm_link.dto.request.ProductUpdateRequest;
import com.cvv.scm_link.dto.response.ProductResponse;
import com.cvv.scm_link.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Product, ProductCreateRequest, ProductUpdateRequest, ProductResponse>{
    @Override
    ProductResponse toDTO(Product entity);

    @Override
    Product toEntity(ProductCreateRequest dto);

    @Override
    void updateFromDTO(ProductUpdateRequest dto, Product entity);

    @Override
    List<ProductResponse> toDTOList(List<Product> list);
}
