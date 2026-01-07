package com.cvv.scm_link.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.dto.response.InventorySummaryDTO;
import com.cvv.scm_link.entity.InventoryLevel;

@Repository
public interface InventoryLevelRepository extends BaseRepository<InventoryLevel, String> {
    boolean existsByWarehouse_IdAndProduct_Id(String warehouseId, String productId);

    Optional<InventoryLevel> findByWarehouse_IdAndProduct_Id(String warehouseId, String productId);

    Optional<InventoryLevel> findByProduct_Id(String productId);

    @Query(
            """
	SELECT new com.cvv.scm_link.dto.response.InventorySummaryDTO(
		p.sku,
		p.name,
		il.quantityOnHand,
		COUNT(DISTINCT ild.batchNumber),
		SUM(ild.quantity * ild.costPrice),
		w.id,
		CASE WHEN SUM(il.quantityOnHand) > 0 THEN 'IN_STOCK' ELSE 'OUT_OF_STOCK' END,
		p.id
	)
	FROM InventoryLevel il
	JOIN il.product p
	JOIN il.warehouse w
	LEFT JOIN il.inventoryLocationDetails ild
	GROUP BY p.sku, p.name,il.quantityOnHand, w.id,p.id
""")
    Page<InventorySummaryDTO> getInventorySummary(Pageable pageable);
}
