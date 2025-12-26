package com.cvv.scm_link.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.dto.response.ProductBatchFlatDTO;
import com.cvv.scm_link.entity.Product;

@Repository
public interface ProductRepository extends BaseRepository<Product, String> {

    @Query(
            "Select p from Product p where p.code = :pCode and p.category.code = :cCode order by p.sku desc limit 1")
    Optional<Product> findByLastSku(
            @Param("pCode") String pCode, @Param("cCode") String cCode);

    boolean existsByCode(String code);

    @Query(
            """
		SELECT new com.cvv.scm_link.dto.response.ProductBatchFlatDTO(
			p.id,
			p.code,
			p.sku,
			p.origin,
			p.name,
			p.weightG,
			p.description,
			p.imageUrl,
			ild.sellPrice,
			SUM(CAST(ild.quantityAvailable as long))
		)
		FROM Product p
		INNER JOIN p.inventoryLevels il
		INNER JOIN il.inventoryLocationDetails ild
		WHERE p.active = true
		AND ild.quantityAvailable > 0
		GROUP BY
			p.id,
			p.code,
			p.sku,
			p.origin,
			p.name,
			p.weightG,
			p.description,
			p.imageUrl,
			ild.sellPrice
	""")
    List<ProductBatchFlatDTO> getProductStockAndPrice();
}
