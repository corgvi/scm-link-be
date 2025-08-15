package com.cvv.scm_link.repository;

import com.cvv.scm_link.entity.InventoryLocationDetail;
import com.cvv.scm_link.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryLocationDetailRepository extends BaseRepository<InventoryLocationDetail, String>{
    @Query("SELECT ild FROM InventoryLocationDetail ild JOIN FETCH ild.inventoryLevel il JOIN FETCH il.product p")
    List<InventoryLocationDetail> findAllWithProductInfo();

    @Query("select ild from InventoryLocationDetail ild join fetch ild.inventoryLevel il join fetch il.product p join fetch il.warehouse w where ild.batchNumber = :batch and p.id = :pId and w.id = :wId")
    Optional<InventoryLocationDetail> findBySameBatch(@Param("pId") String productId,
                                                              @Param("wId") String warehouseId,
                                                              @Param("batch") String batch);
}
