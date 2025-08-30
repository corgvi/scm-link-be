package com.cvv.scm_link.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.InventoryLocationDetail;

@Repository
public interface InventoryLocationDetailRepository extends BaseRepository<InventoryLocationDetail, String> {
    @Query("SELECT ild FROM InventoryLocationDetail ild JOIN FETCH ild.inventoryLevel il JOIN FETCH il.product p")
    List<InventoryLocationDetail> findAllWithProductInfo();

    @Query(
            "select ild from InventoryLocationDetail ild join fetch ild.inventoryLevel il join fetch il.product p join fetch il.warehouse w where ild.batchNumber = :batch and p.id = :pId and w.id = :wId")
    Optional<InventoryLocationDetail> findBySameBatch(
            @Param("pId") String productId, @Param("wId") String warehouseId, @Param("batch") String batch);

    @Query("select ild from InventoryLocationDetail ild " + "join ild.inventoryLevel il "
            + "join il.product p "
            + "where p.id = :productId and ild.quantity > 0 "
            + "order by coalesce(ild.expiryDate, '9999-12-31'), ild.createdAt")
    List<InventoryLocationDetail> findByFEFOOrFIFO(@Param("productId") String productId, Pageable pageable);
}
