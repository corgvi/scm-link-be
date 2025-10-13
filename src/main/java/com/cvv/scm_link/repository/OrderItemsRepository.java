package com.cvv.scm_link.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.OrderItems;

@Repository
public interface OrderItemsRepository extends BaseRepository<OrderItems, String> {
    Optional<OrderItems> findByOrder_IdAndProduct_Id(String order_Id, String productId);

    List<OrderItems> findAllByOrder_Id(String orderId);
}
