package com.cvv.scm_link.mapper;

import com.cvv.scm_link.dto.request.WarehouseLocationRequest;
import com.cvv.scm_link.dto.response.WarehouseLocationResponse;
import com.cvv.scm_link.entity.WarehouseLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarehouseLocationMapper extends BaseMapper<WarehouseLocation, WarehouseLocationRequest, WarehouseLocationRequest, WarehouseLocationResponse>{
}
