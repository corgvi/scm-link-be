package com.cvv.scm_link.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.dto.response.OrderItemBatchAllocationResponse;
import com.cvv.scm_link.entity.OrderItemBatchAllocation;

@Repository
public interface OrderItemBatchAllocationRepository extends JpaRepository<OrderItemBatchAllocation, String> {
    Optional<OrderItemBatchAllocation> findByOrderItem_IdAndInventoryLocationDetail_Id(
            String orderItemId, String inventoryLocationDetailId);

    List<OrderItemBatchAllocation> findAllByOrderItem_Id(String orderItemId);

    @Query(
            value = """
        select
            oiba.quantity_allocated as quantityAllocated,
            ild.batch_number as batchNumber,
            ild.expiry_date as expirationDate,
            p.id as productId,
            p.sku as sku,
            p.name as productName
        from order_item_batch_allocation oiba
            join inventory_location_detail ild on oiba.inventory_location_detail_id = ild.id
            join inventory_level il on ild.inventory_level_id = il.id
            join product p on il.product_id = p.id
        where oiba.order_item_id = :orderItemId
          and (:includeCompleted = true or oiba.completed = false)
        """,
            nativeQuery = true
    )
    List<OrderItemBatchAllocationResponse> findOrderItemDetails(
            @Param("orderItemId") String orderItemId,
            @Param("includeCompleted") boolean includeCompleted
    );

}
