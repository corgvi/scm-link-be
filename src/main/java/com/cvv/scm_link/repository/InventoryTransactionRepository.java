package com.cvv.scm_link.repository;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.InventoryTransaction;

import java.util.List;

@Repository
public interface InventoryTransactionRepository extends BaseRepository<InventoryTransaction, String> {
    List<InventoryTransaction> findAllByRelateEntityId(String relateEntityId);
}

