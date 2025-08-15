package com.cvv.scm_link.repository;

import com.cvv.scm_link.entity.WarehouseLocation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseLocationRepository extends BaseRepository<WarehouseLocation, String> {
    Optional<WarehouseLocation> findByIdAndWarehouse_Id(String id, String warehouseId);
}
