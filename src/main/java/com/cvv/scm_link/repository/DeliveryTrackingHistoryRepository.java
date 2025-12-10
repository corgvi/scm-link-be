package com.cvv.scm_link.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.DeliveryTrackingHistory;

@Repository
public interface DeliveryTrackingHistoryRepository extends BaseRepository<DeliveryTrackingHistory, String> {
    List<DeliveryTrackingHistory> findAllByDelivery_IdOrderByTimestampAsc(String deliveryId);

    @Query(
            "SELECT dth FROM DeliveryTrackingHistory dth WHERE dth.delivery.id = :deliveryId ORDER BY dth.timestamp DESC LIMIT 1")
    Optional<DeliveryTrackingHistory> findLatestByDeliveryId(@Param("deliveryId") String deliveryId);

    Page<DeliveryTrackingHistory> findAllByDelivery_Id(String deliveryId, Pageable pageable);
}
