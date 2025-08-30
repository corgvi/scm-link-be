package com.cvv.scm_link.repository;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.OrderItems;

@Repository
public interface OrderItemsRepository extends BaseRepository<OrderItems, String> {}
