package com.cvv.scm_link.repository;

import com.cvv.scm_link.entity.OrderItemBatchAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemBatchAllocationRepository extends JpaRepository<OrderItemBatchAllocation, String> {
    Optional<OrderItemBatchAllocation> findByOrderItem_IdAndInventoryLocationDetail_Id(String orderItemId, String inventoryLocationDetailId);

    List<OrderItemBatchAllocation> findByOrderItem_Id(String orderItemId);
}
