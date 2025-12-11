package com.cvv.scm_link.service;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.filter.WarehouseFilter;
import com.cvv.scm_link.dto.request.WarehouseRequest;
import com.cvv.scm_link.dto.response.WarehouseResponse;
import com.cvv.scm_link.entity.Warehouse;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.WarehouseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.WarehouseRepository;
import com.cvv.scm_link.repository.specification.WarehouseSpecification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseService
        extends BaseServiceImpl<WarehouseRequest, WarehouseRequest, WarehouseResponse, Warehouse, String> {

    WarehouseMapper warehouseMapper;
    WarehouseRepository warehouseRepository;
    MapboxService mapboxService;

    public WarehouseService(
            BaseRepository<Warehouse, String> baseRepository,
            BaseMapper<Warehouse, WarehouseRequest, WarehouseRequest, WarehouseResponse> baseMapper,
            WarehouseMapper warehouseMapper,
            WarehouseRepository warehouseRepository,
            MapboxService mapboxService) {
        super(baseRepository, baseMapper);
        this.warehouseMapper = warehouseMapper;
        this.warehouseRepository = warehouseRepository;
        this.mapboxService = mapboxService;
    }

    @Override
    public WarehouseResponse create(WarehouseRequest dto) {
        Warehouse warehouse = warehouseMapper.toEntity(dto);
        warehouse.setActive(true);

        double[] coordinates = mapboxService
                .getCoordinatesFromAddress(dto.getAddress(), dto.getCity())
                .block();
        if (Objects.isNull(coordinates)) throw new RuntimeException("Convert to coordinates failed");
        warehouse.setLatitude(coordinates[0]);
        warehouse.setLongitude(coordinates[1]);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDTO(warehouse);
    }

    public Page<WarehouseResponse> filter(WarehouseFilter filter, Pageable pageable) {
        return warehouseRepository
                .findAll(new WarehouseSpecification(filter), pageable)
                .map(baseMapper::toDTO);
    }

    @Override
    public WarehouseResponse update(WarehouseRequest dto, String s) {
        Warehouse warehouse =
                warehouseRepository.findById(s).orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_FOUND));

        if (dto.getAddress() != null || dto.getCity() != null) {
            String address = dto.getAddress() != null ? dto.getAddress() : warehouse.getAddress();
            String city = dto.getCity() != null ? dto.getCity() : warehouse.getCity();
            double[] coordinates =
                    mapboxService.getCoordinatesFromAddress(address, city).block();
            if (Objects.isNull(coordinates)) throw new RuntimeException("Convert to coordinates failed");
            warehouse.setLatitude(coordinates[0]);
            warehouse.setLongitude(coordinates[1]);
        }

        if (dto.getName() != null) {
            warehouse.setName(dto.getName());
        }
        if (dto.getContactPhone() != null) {
            warehouse.setContactPhone(dto.getContactPhone());
        }

        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDTO(warehouse);
    }
}
