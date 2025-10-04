package com.cvv.scm_link.repository;

import com.cvv.scm_link.entity.OrderItems;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemsRepository extends BaseRepository<OrderItems, String> {
    Optional<OrderItems> findByOrder_IdAndProduct_Id(String order_Id, String productId);
}
