package com.cvv.scm_link.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.dto.request.InventoryLevelRequest;
import com.cvv.scm_link.dto.response.InventoryLevelResponse;
import com.cvv.scm_link.dto.response.InventorySummaryDTO;
import com.cvv.scm_link.entity.InventoryLevel;
import com.cvv.scm_link.entity.Product;
import com.cvv.scm_link.entity.Warehouse;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.InventoryLevelMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.InventoryLevelRepository;
import com.cvv.scm_link.repository.ProductRepository;
import com.cvv.scm_link.repository.WarehouseRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryLevelService
        extends BaseServiceImpl<
                InventoryLevelRequest, InventoryLevelRequest, InventoryLevelResponse, InventoryLevel, String> {

    InventoryLevelRepository inventoryLevelRepository;
    WarehouseRepository warehouseRepository;
    ProductRepository productRepository;
    InventoryLevelMapper inventoryLevelMapper;

    public InventoryLevelService(
            BaseRepository<InventoryLevel, String> baseRepository,
            BaseMapper<InventoryLevel, InventoryLevelRequest, InventoryLevelRequest, InventoryLevelResponse> baseMapper,
            InventoryLevelRepository inventoryLevelRepository,
            WarehouseRepository warehouseRepository,
            ProductRepository productRepository,
            InventoryLevelMapper inventoryLevelMapper) {
        super(baseRepository, baseMapper);
        this.inventoryLevelRepository = inventoryLevelRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.inventoryLevelMapper = inventoryLevelMapper;
    }

    @Transactional(rollbackFor = AppException.class)
    public InventoryLevelResponse createOrUpdate(InventoryLevelRequest request) {
        InventoryLevel inventoryLevel = inventoryLevelRepository
                .findByWarehouse_IdAndProduct_Id(request.getWarehouse_id(), request.getProduct_id())
                .orElse(null);
        if (inventoryLevel == null) {
            Warehouse warehouse = warehouseRepository
                    .findById(request.getWarehouse_id())
                    .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));
            Product product = productRepository
                    .findById(request.getProduct_id())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            inventoryLevel = InventoryLevel.builder()
                    .quantityOnHand(request.getQuantityOnHand())
                    .quantityAvailable(request.getQuantityAvailable())
                    .warehouse(warehouse)
                    .product(product)
                    .build();
        } else {
            int quantityOnHand = inventoryLevel.getQuantityOnHand();
            inventoryLevelMapper.updateFromDTO(request, inventoryLevel);
            inventoryLevel.setQuantityOnHand(quantityOnHand + request.getQuantityOnHand());
            inventoryLevel.setQuantityAvailable(request.getQuantityAvailable() + inventoryLevel.getQuantityAvailable());
        }
        inventoryLevel = inventoryLevelRepository.save(inventoryLevel);
        return inventoryLevelMapper.toDTO(inventoryLevel);
    }

    public Page<InventorySummaryDTO> getInventorySummary(Pageable pageable) {
        return inventoryLevelRepository.getInventorySummary(pageable);
    }

    public InventoryLevel findByProductId(String id) {
        return inventoryLevelRepository.findByProduct_Id(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
