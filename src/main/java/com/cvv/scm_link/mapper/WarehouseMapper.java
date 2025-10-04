package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;

import com.cvv.scm_link.dto.request.WarehouseRequest;
import com.cvv.scm_link.dto.response.WarehouseResponse;
import com.cvv.scm_link.entity.Warehouse;

@Mapper(componentModel = "spring")
public interface WarehouseMapper extends BaseMapper<Warehouse, WarehouseRequest, WarehouseRequest, WarehouseResponse> {}
