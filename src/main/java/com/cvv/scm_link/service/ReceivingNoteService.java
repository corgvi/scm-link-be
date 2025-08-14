package com.cvv.scm_link.service;

import com.cvv.scm_link.constant.StatusReceivingNote;
import com.cvv.scm_link.constant.TransactionType;
import com.cvv.scm_link.dto.request.*;
import com.cvv.scm_link.dto.response.InventoryLevelResponse;
import com.cvv.scm_link.dto.response.ReceivingNoteResponse;
import com.cvv.scm_link.entity.ReceivingNote;
import com.cvv.scm_link.entity.Warehouse;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.InventoryLevelMapper;
import com.cvv.scm_link.mapper.ReceivingNoteMapper;
import com.cvv.scm_link.repository.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReceivingNoteService extends BaseServiceImpl<ReceivingNoteRequest, ReceivingNoteRequest, ReceivingNoteResponse, ReceivingNote, String> {

    ReceivingNoteRepository repository;
    WarehouseRepository warehouseRepository;
    ReceivingNoteMapper receivingNoteMapper;
    ProductRepository productRepository;
    InventoryLevelService inventoryRepository;
    InventoryLocationDetailService inventoryLocationDetailService;
    WarehouseLocationRepository warehouseLocationRepository;
    InventoryLevelMapper inventoryLevelMapper;
    private final InventoryTransactionService inventoryTransactionService;
    private final SupplierRepository supplierRepository;

    public ReceivingNoteService(BaseRepository<ReceivingNote, String> baseRepository, BaseMapper<ReceivingNote, ReceivingNoteRequest, ReceivingNoteRequest, ReceivingNoteResponse> baseMapper, ReceivingNoteRepository repository, WarehouseRepository warehouseRepository, ReceivingNoteMapper receivingNoteMapper, ProductRepository productRepository, InventoryLevelService inventoryRepository, InventoryLocationDetailService inventoryLocationDetailService, WarehouseLocationRepository warehouseLocationRepository, InventoryLevelMapper inventoryLevelMapper, InventoryTransactionService inventoryTransactionService, SupplierRepository supplierRepository) {
        super(baseRepository, baseMapper);
        this.repository = repository;
        this.warehouseRepository = warehouseRepository;
        this.receivingNoteMapper = receivingNoteMapper;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryLocationDetailService = inventoryLocationDetailService;
        this.warehouseLocationRepository = warehouseLocationRepository;
        this.inventoryLevelMapper = inventoryLevelMapper;
        this.inventoryTransactionService = inventoryTransactionService;
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional(rollbackFor = AppException.class)
    public ReceivingNoteResponse create(ReceivingNoteRequest dto) {
        ReceivingNote receivingNote = receivingNoteMapper.toEntity(dto);

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouse_id()).orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));
        receivingNote.setWarehouse(warehouse);
        receivingNote.setSupplier(supplierRepository.findById(dto.getSupplier_id()).orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)));

        int totalItemsReceived = 0;

        dto.getProducts().forEach(p -> {
            InventoryLevelRequest request = InventoryLevelRequest.builder()
                    .warehouse_id(warehouse.getId())
                    .product_id(p.getProductId())
                    .quantityOnHand(p.getQuantity())
                    .build();
            InventoryLevelResponse inventoryLevelResponse = inventoryRepository.createOrUpdate(request);

            InventoryLocationDetailRequest inventoryLocationDetailRequest = InventoryLocationDetailRequest.builder()
                    .batchNumber(p.getBatchNumber())
                    .costPrice(p.getCostPrice())
                    .expiryDate(p.getExpiryDate())
                    .inventoryLevelId(inventoryLevelResponse.getId())
                    .warehouseLocationId(p.getWarehouseLocationId())
                    .build();
            inventoryLocationDetailService.create(inventoryLocationDetailRequest);

            InventoryTransactionRequest inventoryTransaction = InventoryTransactionRequest.builder()
                    .inventoryLevelId(inventoryLevelResponse.getId())
                    .transactionType(TransactionType.RECEIVING)
                    .quantityChange(p.getQuantity())
                    .currentQuantity(inventoryLevelResponse.getQuantityOnHand())
                    .build();
            inventoryTransactionService.createByReceiving(inventoryTransaction);

        });
        receivingNote.setStatus(StatusReceivingNote.RECEIVED_COMPLETELY);
        receivingNote.setTotalItemsReceived(dto.getProducts().stream().mapToInt(ReceivingItemsRequest::getQuantity).sum());
        receivingNote =  repository.save(receivingNote);
        return receivingNoteMapper.toDTO(receivingNote);
    }

    @Override
    public ReceivingNoteResponse update(ReceivingNoteRequest dto, String s) {
        return super.update(dto, s);
    }
}
