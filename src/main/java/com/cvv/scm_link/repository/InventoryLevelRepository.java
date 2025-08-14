package com.cvv.scm_link.repository;

import com.cvv.scm_link.entity.InventoryLevel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryLevelRepository extends BaseRepository<InventoryLevel, String> {
    boolean existsByWarehouse_IdAndProduct_Id(String warehouseId, String productId);
    Optional<InventoryLevel> findByWarehouse_IdAndProduct_Id(String warehouseId, String productId);
}
