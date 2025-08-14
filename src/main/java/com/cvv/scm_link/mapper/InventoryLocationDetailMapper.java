package com.cvv.scm_link.mapper;

import com.cvv.scm_link.dto.request.InventoryLocationDetailRequest;
import com.cvv.scm_link.dto.response.InventoryLocationDetailResponse;
import com.cvv.scm_link.entity.InventoryLocationDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryLocationDetailMapper extends BaseMapper<InventoryLocationDetail, InventoryLocationDetailRequest, InventoryLocationDetailRequest, InventoryLocationDetailResponse>{
}
