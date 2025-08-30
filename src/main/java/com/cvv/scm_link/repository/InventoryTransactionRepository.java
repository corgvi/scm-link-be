package com.cvv.scm_link.repository;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.InventoryTransaction;

@Repository
public interface InventoryTransactionRepository extends BaseRepository<InventoryTransaction, String> {}
