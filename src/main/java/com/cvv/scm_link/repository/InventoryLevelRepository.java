package com.cvv.scm_link.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.InventoryLevel;

@Repository
public interface InventoryLevelRepository extends BaseRepository<InventoryLevel, String> {
    boolean existsByWarehouse_IdAndProduct_Id(String warehouseId, String productId);

    Optional<InventoryLevel> findByWarehouse_IdAndProduct_Id(String warehouseId, String productId);

    Optional<InventoryLevel> findByProduct_Id(String productId);
}
