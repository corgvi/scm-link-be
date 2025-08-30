package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.cvv.scm_link.dto.request.InventoryLevelRequest;
import com.cvv.scm_link.dto.response.InventoryLevelResponse;
import com.cvv.scm_link.entity.InventoryLevel;

@Mapper(componentModel = "spring")
public interface InventoryLevelMapper
        extends BaseMapper<InventoryLevel, InventoryLevelRequest, InventoryLevelRequest, InventoryLevelResponse> {
    @Override
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "quantityOnHand", ignore = true)
    void updateFromDTO(InventoryLevelRequest dto, @MappingTarget InventoryLevel entity);
}
