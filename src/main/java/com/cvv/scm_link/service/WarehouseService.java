package com.cvv.scm_link.service;

import com.cvv.scm_link.dto.request.WarehouseRequest;
import com.cvv.scm_link.dto.response.WarehouseResponse;
import com.cvv.scm_link.entity.Warehouse;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseService extends BaseServiceImpl<WarehouseRequest, WarehouseRequest, WarehouseResponse, Warehouse, String>{

    public WarehouseService(BaseRepository<Warehouse, String> baseRepository, BaseMapper<Warehouse, WarehouseRequest, WarehouseRequest, WarehouseResponse> baseMapper) {
        super(baseRepository, baseMapper);
    }
}
