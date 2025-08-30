package com.cvv.scm_link.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.Product;

@Repository
public interface ProductRepository extends BaseRepository<Product, String> {

    @Query(
            "Select p from Product p where p.code = :pCode and p.category.code = :cCode and p.size = :size and p.color = :color order by p.sku desc limit 1")
    Optional<Product> findByLastSku(
            @Param("pCode") String pCode,
            @Param("cCode") String cCode,
            @Param("size") String size,
            @Param("color") String color);

    boolean existsByCode(String code);
}
