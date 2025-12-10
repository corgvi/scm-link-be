package com.cvv.scm_link.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.dto.request.InventoryLocationDetailRequest;
import com.cvv.scm_link.dto.response.BatchDetailDTO;
import com.cvv.scm_link.dto.response.InventoryLocationDetailResponse;
import com.cvv.scm_link.dto.response.ProductResponse;
import com.cvv.scm_link.entity.InventoryLocationDetail;
import com.cvv.scm_link.entity.Product;
import com.cvv.scm_link.entity.WarehouseLocation;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.InventoryLocationDetailMapper;
import com.cvv.scm_link.mapper.ProductMapper;
import com.cvv.scm_link.repository.*;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryLocationDetailService
        extends BaseServiceImpl<
                InventoryLocationDetailRequest,
                InventoryLocationDetailRequest,
                InventoryLocationDetailResponse,
                InventoryLocationDetail,
                String> {
    InventoryLocationDetailRepository inventoryLocationDetailRepository;
    WarehouseRepository warehouseRepository;
    WarehouseLocationRepository warehouseLocationRepository;
    InventoryLocationDetailMapper inventoryLocationDetailMapper;
    InventoryLevelRepository inventoryLevelRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;

    public InventoryLocationDetailService(
            BaseRepository<InventoryLocationDetail, String> baseRepository,
            BaseMapper<
                            InventoryLocationDetail,
                            InventoryLocationDetailRequest,
                            InventoryLocationDetailRequest,
                            InventoryLocationDetailResponse>
                    baseMapper,
            InventoryLocationDetailRepository inventoryLocationDetailRepository,
            WarehouseRepository warehouseRepository,
            WarehouseLocationRepository warehouseLocationRepository,
            InventoryLocationDetailMapper inventoryLocationDetailMapper,
            InventoryLevelRepository inventoryLevelRepository,
            ProductRepository productRepository,
            ProductMapper productMapper) {
        super(baseRepository, baseMapper);
        this.inventoryLocationDetailRepository = inventoryLocationDetailRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseLocationRepository = warehouseLocationRepository;
        this.inventoryLocationDetailMapper = inventoryLocationDetailMapper;
        this.inventoryLevelRepository = inventoryLevelRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional(rollbackFor = AppException.class)
    public InventoryLocationDetailResponse create (
            InventoryLocationDetailRequest dto, String warehouseId, String productId) {
        InventoryLocationDetail inventoryLocationDetail = inventoryLocationDetailMapper.toEntity(dto);
            WarehouseLocation location = warehouseLocationRepository
                    .findByIdAndWarehouse_Id(dto.getWarehouseLocationId(), warehouseId)
                    .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_LOCATION_NOT_IN_WAREHOUSE));
            inventoryLocationDetail.setWarehouseLocation(location);
            inventoryLocationDetail.setSellPrice((long) (dto.getCostPrice() + (dto.getCostPrice() * 0.4)));
            inventoryLocationDetail.setInventoryLevel(inventoryLevelRepository
                    .findByWarehouse_IdAndProduct_Id(warehouseId, productId)
                    .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LEVEL_NOT_FOUND)));
            inventoryLocationDetail.setBatchNumber(generateBatchNumber());

        inventoryLocationDetail = inventoryLocationDetailRepository.save(inventoryLocationDetail);
        ProductResponse productResponse = productMapper.toProductResponse(
                inventoryLocationDetail.getInventoryLevel().getProduct());
        log.info("Product response in inventory location detail: {}", productResponse);

        InventoryLocationDetailResponse inventoryLocationDetailResponse =
                inventoryLocationDetailMapper.toDTO(inventoryLocationDetail);
        inventoryLocationDetailResponse.setProduct(productResponse);
        return inventoryLocationDetailResponse;
    }

    private String generateBatchNumber() {
        return "B" + System.currentTimeMillis();
    }

    @Transactional(readOnly = true)
    public Page<InventoryLocationDetailResponse> findAllIncludeProduct(Pageable pageable) {
        Page<InventoryLocationDetail> page = inventoryLocationDetailRepository.findAllWithProductInfo(pageable);

        return page.map(ild -> {
            Product product = ild.getInventoryLevel().getProduct();
            InventoryLocationDetailResponse dto = inventoryLocationDetailMapper.toDTO(ild);
            dto.setProduct(productMapper.toProductResponse(product));
            return dto;
        });
    }

    public List<BatchDetailDTO> getBatchDetails(String productId) {
        productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return inventoryLocationDetailRepository.getBatchDetails(productId);
    }
}
