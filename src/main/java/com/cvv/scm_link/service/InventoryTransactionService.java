package com.cvv.scm_link.service;

import com.cvv.scm_link.dto.request.InventoryTransactionRequest;
import com.cvv.scm_link.dto.response.InventoryTransactionResponse;
import com.cvv.scm_link.entity.InventoryLevel;
import com.cvv.scm_link.entity.InventoryTransaction;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.InventoryTransactionMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.InventoryLevelRepository;
import com.cvv.scm_link.repository.InventoryTransactionRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryTransactionService extends BaseServiceImpl<InventoryTransactionRequest, InventoryTransactionRequest, InventoryTransactionResponse, InventoryTransaction, String>{

    InventoryTransactionRepository inventoryTransactionRepository;
    InventoryLevelRepository inventoryLevelRepository;
    InventoryTransactionMapper inventoryTransactionMapper;

    public InventoryTransactionService(BaseRepository<InventoryTransaction, String> baseRepository, BaseMapper<InventoryTransaction, InventoryTransactionRequest, InventoryTransactionRequest, InventoryTransactionResponse> baseMapper, InventoryTransactionRepository inventoryTransactionRepository, InventoryLevelRepository inventoryLevelRepository, InventoryTransactionMapper inventoryTransactionMapper) {
        super(baseRepository, baseMapper);
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.inventoryLevelRepository = inventoryLevelRepository;
        this.inventoryTransactionMapper = inventoryTransactionMapper;
    }

    @Transactional(rollbackFor = AppException.class)
    public InventoryTransactionResponse createByReceiving(InventoryTransactionRequest dto) {
        InventoryLevel inventoryLevel = inventoryLevelRepository.findById(dto.getInventoryLevelId()).orElseThrow(() -> new AppException(ErrorCode.INVENTORY_LEVEL_NOTFOUND));
        InventoryTransaction inventoryTransaction = inventoryTransactionMapper.toEntity(dto);
        inventoryTransaction.setInventoryLevel(inventoryLevel);
        inventoryTransaction = inventoryTransactionRepository.save(inventoryTransaction);
        return inventoryTransactionMapper.toDTO(inventoryTransaction);
    }
}
