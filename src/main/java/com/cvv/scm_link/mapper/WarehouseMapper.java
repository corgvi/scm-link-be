package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.cvv.scm_link.dto.request.WarehouseRequest;
import com.cvv.scm_link.dto.request.WarehouseUpdateRequest;
import com.cvv.scm_link.dto.response.WarehouseResponse;
import com.cvv.scm_link.entity.Warehouse;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface WarehouseMapper
        extends BaseMapper<Warehouse, WarehouseRequest, WarehouseUpdateRequest, WarehouseResponse> {}
