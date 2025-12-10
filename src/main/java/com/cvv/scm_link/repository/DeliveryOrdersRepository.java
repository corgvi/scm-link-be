package com.cvv.scm_link.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.DeliveryOrders;

@Repository
public interface DeliveryOrdersRepository extends BaseRepository<DeliveryOrders, String> {
    List<DeliveryOrders> findByDelivery_Id(String deliveryId);

    Page<DeliveryOrders> findByDelivery_Id(String deliveryId, Pageable pageable);

    void deleteByDelivery_IdAndOrder_IdIn(String deliveryId, Set<String> orderIds);
}
