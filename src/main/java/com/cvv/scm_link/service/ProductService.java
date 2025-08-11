package com.cvv.scm_link.service;

import com.cvv.scm_link.dto.request.ProductCreateRequest;
import com.cvv.scm_link.dto.request.ProductUpdateRequest;
import com.cvv.scm_link.dto.response.ProductResponse;
import com.cvv.scm_link.entity.Product;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProductService extends BaseServiceImpl<ProductCreateRequest, ProductUpdateRequest, ProductResponse, Product, String>{
    public ProductService(BaseRepository<Product, String> baseRepository, BaseMapper<Product, ProductCreateRequest, ProductUpdateRequest, ProductResponse> baseMapper) {
        super(baseRepository, baseMapper);
    }
}
