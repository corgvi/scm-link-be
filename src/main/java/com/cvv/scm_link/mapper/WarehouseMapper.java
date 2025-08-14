package com.cvv.scm_link.mapper;

import com.cvv.scm_link.dto.request.WarehouseRequest;
import com.cvv.scm_link.dto.response.WarehouseResponse;
import com.cvv.scm_link.entity.Warehouse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarehouseMapper extends BaseMapper<Warehouse, WarehouseRequest, WarehouseRequest, WarehouseResponse>{
}
