package com.cvv.scm_link.mapper;

import com.cvv.scm_link.dto.request.InventoryLocationDetailRequest;
import com.cvv.scm_link.dto.response.InventoryLocationDetailResponse;
import com.cvv.scm_link.entity.InventoryLocationDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryLocationDetailMapper extends BaseMapper<InventoryLocationDetail, InventoryLocationDetailRequest, InventoryLocationDetailRequest, InventoryLocationDetailResponse>{
    @Override
    @Mapping(target = "inventoryLevel", ignore = true)
    @Mapping(target = "warehouseLocation", ignore = true)
    InventoryLocationDetail toEntity(InventoryLocationDetailRequest dto);

    @Override
    @Mapping(target = "warehouse", source = "warehouseLocation")
    @Mapping(target = "product" , ignore = true)
    InventoryLocationDetailResponse toDTO(InventoryLocationDetail entity);
}
