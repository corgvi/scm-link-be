package com.cvv.scm_link.service;

import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.request.WarehouseLocationRequest;
import com.cvv.scm_link.dto.response.WarehouseLocationResponse;
import com.cvv.scm_link.entity.WarehouseLocation;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.WarehouseLocationMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.WarehouseLocationRepository;
import com.cvv.scm_link.repository.WarehouseRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseLocationService
        extends BaseServiceImpl<
                WarehouseLocationRequest,
                WarehouseLocationRequest,
                WarehouseLocationResponse,
                WarehouseLocation,
                String> {
    WarehouseLocationRepository warehouseLocationRepository;
    WarehouseRepository warehouseRepository;
    WarehouseLocationMapper warehouseLocationMapper;

    public WarehouseLocationService(
            BaseRepository<WarehouseLocation, String> baseRepository,
            BaseMapper<WarehouseLocation, WarehouseLocationRequest, WarehouseLocationRequest, WarehouseLocationResponse>
                    baseMapper,
            WarehouseLocationRepository warehouseLocationRepository,
            WarehouseRepository warehouseRepository,
            WarehouseLocationMapper warehouseLocationMapper) {
        super(baseRepository, baseMapper);
        this.warehouseLocationRepository = warehouseLocationRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseLocationMapper = warehouseLocationMapper;
    }

    @Override
    public WarehouseLocationResponse create(WarehouseLocationRequest dto) {
        WarehouseLocation location = warehouseLocationMapper.toEntity(dto);
        location.setWarehouse(warehouseRepository
                .findById(dto.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND)));
        location = warehouseLocationRepository.save(location);

        return warehouseLocationMapper.toDTO(location);
    }
}
