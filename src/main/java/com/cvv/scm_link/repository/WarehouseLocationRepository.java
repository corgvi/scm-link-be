package com.cvv.scm_link.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.WarehouseLocation;

@Repository
public interface WarehouseLocationRepository extends BaseRepository<WarehouseLocation, String> {
    Optional<WarehouseLocation> findByIdAndWarehouse_Id(String id, String warehouseId);
}
