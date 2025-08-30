package com.cvv.scm_link.repository;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.Order;

@Repository
public interface OrderRepository extends BaseRepository<Order, String> {}
