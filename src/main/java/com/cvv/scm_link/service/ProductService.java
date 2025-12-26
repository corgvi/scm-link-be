package com.cvv.scm_link.service;

import java.util.*;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.dto.filter.ProductFilter;
import com.cvv.scm_link.dto.request.ProductCreateRequest;
import com.cvv.scm_link.dto.request.ProductUpdateRequest;
import com.cvv.scm_link.dto.response.InventoryBatchDTO;
import com.cvv.scm_link.dto.response.ProductBatchFlatDTO;
import com.cvv.scm_link.dto.response.ProductDetailsResponse;
import com.cvv.scm_link.dto.response.ProductUserResponse;
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
import com.cvv.scm_link.repository.specification.ProductSpecification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService
        extends BaseServiceImpl<ProductCreateRequest, ProductUpdateRequest, ProductDetailsResponse, Product, String> {

    CategoryRepository categoryRepository;
    SupplierRepository supplierRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;

    public ProductService(
            BaseRepository<Product, String> baseRepository,
            BaseMapper<Product, ProductCreateRequest, ProductUpdateRequest, ProductDetailsResponse> baseMapper,
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository,
            ProductRepository productRepository,
            ProductMapper productMapper) {
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
        Category category = categoryRepository
                .findByCode(dto.getCategoryCode())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Supplier supplier = supplierRepository
                .findByCode(dto.getSupplierCode())
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        entity.setCategory(category);
        entity.setSupplier(supplier);
        entity.setActive(true);
        entity.setSku(generateSku(category.getCode(), dto.getCode()));
        entity = productRepository.save(entity);
        return productMapper.toDTO(entity);
    }

    private String generateSku(String categoryCode, String productCode) {
        StringJoiner sku = new StringJoiner("-");
        int lastIndexSku = 1;
        Product product =
                productRepository.findByLastSku(productCode, categoryCode).orElse(null);
        if (Objects.isNull(product)) {
            sku.add(productCode).add(categoryCode).add(String.format("%03d", lastIndexSku));
        } else {
            String[] parts = product.getSku().split("-");
            if (parts.length == 5) {
                lastIndexSku = Integer.parseInt(parts[4]) + 1;
            }
            sku.add(productCode).add(categoryCode).add(String.format("%03d", lastIndexSku));
        }

        return sku.toString();
    }

    @Override
    @Transactional(rollbackFor = AppException.class)
    public ProductDetailsResponse update(ProductUpdateRequest dto, String id) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (dto.getCode() != null) {
            if (!dto.getCode().equals(product.getCode())) {
                if (productRepository.existsByCode(dto.getCode())) {
                    throw new AppException(ErrorCode.CODE_EXISTED);
                }
            }
        }

        if (dto.getCategoryCode() != null) {
            Category category = categoryRepository
                    .findByCode(dto.getCategoryCode())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            product.setCategory(category);
        }

        if (dto.getSupplierCode() != null) {
            Supplier supplier = supplierRepository
                    .findByCode(dto.getSupplierCode())
                    .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
            product.setSupplier(supplier);
        }

        productMapper.updateFromDTO(dto, product);

        product = productRepository.save(product);

        return productMapper.toDTO(product);
    }

    public Page<ProductDetailsResponse> filter(ProductFilter filter, Pageable pageable) {
        return productRepository
                .findAll(new ProductSpecification(filter), pageable)
                .map(productMapper::toDTO);
    }

    public List<ProductUserResponse> getProductsWithStockAndPrice() {
        List<ProductBatchFlatDTO> productBatchFlatDTOs = productRepository.getProductStockAndPrice();
        Map<String, ProductUserResponse> result = new LinkedHashMap<>();
        for (ProductBatchFlatDTO dto : productBatchFlatDTOs) {
            ProductUserResponse productUserResponse =
                    result.computeIfAbsent(dto.getId(), id -> ProductUserResponse.builder()
                            .id(dto.getId())
                            .name(dto.getName())
                            .code(dto.getCode())
                            .imageUrl(dto.getImageUrl())
                            .origin(dto.getOrigin())
                            .sku(dto.getSku())
                            .batches(new ArrayList<>())
                            .weight(dto.getWeight())
                            .description(dto.getDescription())
                            .build());

            if (dto.getSellPrice() > 0 && dto.getTotalAvailable() > 0) {
                productUserResponse
                        .getBatches()
                        .add(InventoryBatchDTO.builder()
                                .totalAvailable(dto.getTotalAvailable())
                                .sellPrice(dto.getSellPrice())
                                .build());
            }
        }
        return new ArrayList<>(result.values());
    }
}
