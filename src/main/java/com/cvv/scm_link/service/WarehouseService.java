package com.cvv.scm_link.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.request.WarehouseRequest;
import com.cvv.scm_link.dto.response.WarehouseResponse;
import com.cvv.scm_link.entity.Warehouse;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.mapper.WarehouseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.WarehouseRepository;

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

        double[] coordinates =
                mapboxService.getCoordinatesFromAddress(dto.getAddress()).block();
        if (Objects.isNull(coordinates)) throw new RuntimeException("Convert to coordinates failed");
        warehouse.setLatitude(coordinates[0]);
        warehouse.setLongitude(coordinates[1]);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDTO(warehouse);
    }
}
