package com.cvv.scm_link.repository;

import com.cvv.scm_link.entity.InventoryTransaction;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends BaseRepository<InventoryTransaction, String> {
}
