package com.cvv.scm_link.repository;

import com.cvv.scm_link.entity.Supplier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends BaseRepository<Supplier, String>{
    Optional<Supplier> findByCode(String code);
}
