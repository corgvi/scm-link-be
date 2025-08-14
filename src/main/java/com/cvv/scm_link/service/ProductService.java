package com.cvv.scm_link.service;

import com.cvv.scm_link.dto.request.ProductCreateRequest;
import com.cvv.scm_link.dto.request.ProductUpdateRequest;
import com.cvv.scm_link.dto.response.ProductDetailsResponse;
import com.cvv.scm_link.entity.Category;
import com.cvv.scm_link.entity.Product;
import com.cvv.scm_link.entity.Supplier;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.ProductMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.CategoryRepository;
import com.cvv.scm_link.repository.ProductRepository;
import com.cvv.scm_link.repository.SupplierRepository;
import jakarta.persistence.LockModeType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProductService extends BaseServiceImpl<ProductCreateRequest, ProductUpdateRequest, ProductDetailsResponse, Product, String>{

    CategoryRepository categoryRepository;
    SupplierRepository supplierRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;

    public ProductService(BaseRepository<Product, String> baseRepository, BaseMapper<Product, ProductCreateRequest, ProductUpdateRequest, ProductDetailsResponse> baseMapper, CategoryRepository categoryRepository, SupplierRepository supplierRepository, ProductRepository productRepository, ProductMapper productMapper) {
        super(baseRepository, baseMapper);
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(rollbackFor = AppException.class)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public ProductDetailsResponse create(ProductCreateRequest dto) {

        if (dto == null) throw new AppException(ErrorCode.DTO_IS_NULL);
        if (productRepository.existsByCode((dto.getCode()))) throw new AppException(ErrorCode.CODE_EXISTED);

        Product entity = baseMapper.toEntity(dto);
        Category category = categoryRepository.findByCode(dto.getCategoryCode()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Supplier supplier = supplierRepository.findByCode(dto.getSupplierCode()).orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        entity.setCategory(category);
        entity.setSupplier(supplier);
        entity.setSku(generateSku(dto.getSize(), category.getCode(), dto.getCode(), dto.getColor()));
        entity = productRepository.save(entity);
        return productMapper.toDTO(entity);
    }

    private String generateSku(String size, String categoryCode, String productCode, String color) {
        StringJoiner sku = new StringJoiner("-");
        int lastIndexSku = 1;
        Product product = productRepository.findByLastSku(productCode, categoryCode, size, color).orElse(null);
        if (Objects.isNull(product)) {
            sku.add(productCode).add(categoryCode).add(color).add(size).add(String.format("%03d", lastIndexSku));
        } else {
            String[] parts = product.getSku().split("-");
            if (parts.length == 5) {
                lastIndexSku = Integer.parseInt(parts[4]) + 1;
            }
            sku.add(productCode).add(categoryCode).add(color).add(size).add(String.format("%03d", lastIndexSku));
        }

        return sku.toString();
    }

    @Override
    @Transactional(rollbackFor = AppException.class)
    public ProductDetailsResponse update(ProductUpdateRequest dto, String s) {
        Product product = productRepository.findById(s).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateFromDTO(dto, product);
        if (productRepository.existsByCode(dto.getCode())) throw new AppException(ErrorCode.CODE_EXISTED);
        product.setCategory(categoryRepository.findByCode(dto.getCategoryCode()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
        product.setSupplier(supplierRepository.findByCode(dto.getSupplierCode()).orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)));
        product.setSku(generateSku(dto.getSize(), dto.getCategoryCode(), dto.getCode(), dto.getColor()));
        product = productRepository.save(product);
        return productMapper.toDTO(product);
    }
}
