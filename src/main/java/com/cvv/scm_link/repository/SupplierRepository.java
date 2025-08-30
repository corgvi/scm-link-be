package com.cvv.scm_link.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.Supplier;

@Repository
public interface SupplierRepository extends BaseRepository<Supplier, String> {
    Optional<Supplier> findByCode(String code);
}
