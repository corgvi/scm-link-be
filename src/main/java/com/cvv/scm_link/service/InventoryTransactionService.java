package com.cvv.scm_link.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.constant.TransactionType;
import com.cvv.scm_link.dto.request.InventoryLevelRequest;
import com.cvv.scm_link.dto.request.InventoryTransactionAdjustmentRequest;
import com.cvv.scm_link.dto.request.InventoryTransactionRequest;
import com.cvv.scm_link.dto.response.InventoryLevelResponse;
import com.cvv.scm_link.dto.response.InventoryTransactionResponse;
import com.cvv.scm_link.entity.InventoryLevel;
import com.cvv.scm_link.entity.InventoryTransaction;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.InventoryLevelMapper;
import com.cvv.scm_link.mapper.InventoryTransactionMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.InventoryLevelRepository;
import com.cvv.scm_link.repository.InventoryTransactionRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryTransactionService
        extends BaseServiceImpl<
                InventoryTransactionRequest,
                InventoryTransactionRequest,
                InventoryTransactionResponse,
                InventoryTransaction,
                String> {

    InventoryTransactionRepository inventoryTransactionRepository;
    InventoryLevelRepository inventoryLevelRepository;
    InventoryTransactionMapper inventoryTransactionMapper;
    InventoryLevelService inventoryLevelService;
    InventoryLevelMapper inventoryLevelMapper;

    public InventoryTransactionService(
            BaseRepository<InventoryTransaction, String> baseRepository,
            BaseMapper<
                            InventoryTransaction,
                            InventoryTransactionRequest,
                            InventoryTransactionRequest,
                            InventoryTransactionResponse>
                    baseMapper,
            InventoryTransactionRepository inventoryTransactionRepository,
            InventoryLevelRepository inventoryLevelRepository,
            InventoryTransactionMapper inventoryTransactionMapper,
            InventoryLevelService inventoryLevelService,
            InventoryLevelMapper inventoryLevelMapper) {
        super(baseRepository, baseMapper);
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.inventoryLevelRepository = inventoryLevelRepository;
        this.inventoryTransactionMapper = inventoryTransactionMapper;
        this.inventoryLevelService = inventoryLevelService;
        this.inventoryLevelMapper = inventoryLevelMapper;
    }

    @Transactional(rollbackFor = AppException.class)
    public void createByReceiving(InventoryTransactionRequest dto) {
        InventoryLevel inventoryLevel = inventoryLevelRepository
                .findById(dto.getInventoryLevelId())
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LEVEL_NOT_FOUND));
        InventoryTransaction inventoryTransaction = inventoryTransactionMapper.toEntity(dto);
        inventoryTransaction.setInventoryLevel(inventoryLevel);
        inventoryTransaction.setRelateEntityId(inventoryLevel.getId());
        inventoryTransaction = inventoryTransactionRepository.save(inventoryTransaction);
        inventoryTransactionMapper.toDTO(inventoryTransaction);
    }

    @Transactional(rollbackFor = AppException.class)
    public InventoryTransactionResponse createByAdjusting(InventoryTransactionAdjustmentRequest dto) {
        InventoryLevel inventoryLevel = inventoryLevelRepository
                .findByWarehouse_IdAndProduct_Id(dto.getWarehouseId(), dto.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LEVEL_NOT_FOUND));
        InventoryLevelRequest inventoryLevelRequest = InventoryLevelRequest.builder()
                .warehouse_id(dto.getWarehouseId())
                .product_id(dto.getProductId())
                .quantityOnHand(dto.getQuantityChange())
                .build();
        InventoryLevelResponse inventoryLevelResponse = inventoryLevelService.createOrUpdate(inventoryLevelRequest);
        InventoryTransaction inventoryTransaction = inventoryTransactionMapper.toEntityFromAdjustment(dto);
        inventoryTransaction.setInventoryLevel(inventoryLevel);
        inventoryTransaction.setRelateEntityId(inventoryLevel.getId());
        inventoryTransaction.setTransactionType(TransactionType.ADJUSTMENT);
        inventoryTransaction.setCurrentQuantity(inventoryLevelResponse.getQuantityOnHand());
        inventoryTransaction = inventoryTransactionRepository.save(inventoryTransaction);
        InventoryTransactionResponse inventoryTransactionResponse =
                inventoryTransactionMapper.toDTO(inventoryTransaction);
        inventoryTransactionResponse.setInventoryLevel(inventoryLevelResponse);
        return inventoryTransactionResponse;
    }

    @Override
    public List<InventoryTransactionResponse> findAll() {
        List<InventoryTransactionResponse> inventoryLocationDetailResponseList = new ArrayList<>();
        inventoryTransactionRepository.findAll().forEach(i -> {
            InventoryLevel inventoryLevel = i.getInventoryLevel();
            InventoryTransactionResponse inventoryTransactionResponse = inventoryTransactionMapper.toDTO(i);
            inventoryTransactionResponse.setInventoryLevel(inventoryLevelMapper.toDTO(inventoryLevel));
            inventoryLocationDetailResponseList.add(inventoryTransactionResponse);
        });
        return inventoryLocationDetailResponseList;
    }
}
