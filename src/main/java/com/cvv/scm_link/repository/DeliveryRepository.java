package com.cvv.scm_link.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.Delivery;

@Repository
public interface DeliveryRepository extends BaseRepository<Delivery, String> {
    List<Delivery> findAllByDeliveryStatusIn(Collection<String> deliveryStatuses);

    @Query(
            """
	SELECT d FROM Delivery d
	LEFT JOIN FETCH d.deliveryOrders do
	LEFT JOIN FETCH do.order o
	LEFT JOIN FETCH o.customer
	WHERE d.id = :id
""")
    Optional<Delivery> findDetailById(@Param("id") String id);

    long countByDeliveryStatus(String status);

    @Query("""
        SELECT (COUNT(d) * 1.0 / 
               (SELECT COUNT(d2) FROM Delivery d2 WHERE d2.deliveryStatus = 'DELIVERY_COMPLETED')) * 100
        FROM Delivery d 
        WHERE d.deliveryStatus = 'DELIVERY_COMPLETED'
          AND d.actualDeliveryTime <= d.scheduledDeliveryTime
    """)
    double calculateOnTimeRate();
}
