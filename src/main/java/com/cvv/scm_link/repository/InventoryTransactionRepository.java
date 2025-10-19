package com.cvv.scm_link.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.InventoryTransaction;

import java.util.List;

@Repository
public interface InventoryTransactionRepository extends BaseRepository<InventoryTransaction, String> {
    Page<InventoryTransaction> findAllByRelateEntityId(String relateEntityId, Pageable pageable);

}

