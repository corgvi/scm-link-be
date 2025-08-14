package com.cvv.scm_link.service;

import com.cvv.scm_link.dto.request.InventoryLocationDetailRequest;
import com.cvv.scm_link.dto.response.InventoryLocationDetailResponse;
import com.cvv.scm_link.entity.InventoryLocationDetail;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.InventoryLocationDetailRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class InventoryLocationDetailService extends BaseServiceImpl<InventoryLocationDetailRequest, InventoryLocationDetailRequest, InventoryLocationDetailResponse, InventoryLocationDetail, String> {
    InventoryLocationDetailRepository inventoryLocationDetailRepository;

    public InventoryLocationDetailService(BaseRepository<InventoryLocationDetail, String> baseRepository, BaseMapper<InventoryLocationDetail, InventoryLocationDetailRequest, InventoryLocationDetailRequest, InventoryLocationDetailResponse> baseMapper, InventoryLocationDetailRepository inventoryLocationDetailRepository) {
        super(baseRepository, baseMapper);
        this.inventoryLocationDetailRepository = inventoryLocationDetailRepository;
    }

    @Override
    public InventoryLocationDetailResponse create(InventoryLocationDetailRequest dto) {



        return super.create(dto);
    }
}
