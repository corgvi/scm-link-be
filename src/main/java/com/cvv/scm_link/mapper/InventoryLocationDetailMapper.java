package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cvv.scm_link.dto.request.InventoryLocationDetailRequest;
import com.cvv.scm_link.dto.response.InventoryLocationDetailResponse;
import com.cvv.scm_link.entity.InventoryLocationDetail;

@Mapper(componentModel = "spring")
public interface InventoryLocationDetailMapper
        extends BaseMapper<
                InventoryLocationDetail,
                InventoryLocationDetailRequest,
                InventoryLocationDetailRequest,
                InventoryLocationDetailResponse> {
    @Override
    @Mapping(target = "inventoryLevel", ignore = true)
    @Mapping(target = "warehouseLocation", ignore = true)
    InventoryLocationDetail toEntity(InventoryLocationDetailRequest dto);

    @Override
    @Mapping(target = "warehouse", source = "warehouseLocation")
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouseName", source = "warehouseLocation.warehouse.name")
    InventoryLocationDetailResponse toDTO(InventoryLocationDetail entity);
}
