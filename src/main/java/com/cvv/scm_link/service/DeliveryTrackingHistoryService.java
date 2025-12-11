package com.cvv.scm_link.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.constant.StatusDelivery;
import com.cvv.scm_link.dto.filter.TrackingHistoryFilter;
import com.cvv.scm_link.dto.request.DeliveryTrackingHistoryRequest;
import com.cvv.scm_link.dto.response.DeliveryTrackingHistoryResponse;
import com.cvv.scm_link.entity.*;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.DeliveryRepository;
import com.cvv.scm_link.repository.DeliveryTrackingHistoryRepository;
import com.cvv.scm_link.repository.specification.TrackingHistorySpecification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryTrackingHistoryService
        extends BaseServiceImpl<
                DeliveryTrackingHistoryRequest,
                DeliveryTrackingHistoryRequest,
                DeliveryTrackingHistoryResponse,
                DeliveryTrackingHistory,
                String> {
    DeliveryTrackingHistoryRepository trackingRepo;
    MapboxService mapboxService;
    DeliveryRepository deliveryRepository;

    public DeliveryTrackingHistoryService(
            BaseRepository<DeliveryTrackingHistory, String> baseRepository,
            BaseMapper<
                            DeliveryTrackingHistory,
                            DeliveryTrackingHistoryRequest,
                            DeliveryTrackingHistoryRequest,
                            DeliveryTrackingHistoryResponse>
                    baseMapper,
            DeliveryTrackingHistoryRepository trackingRepo,
            MapboxService mapboxService,
            DeliveryRepository deliveryRepository) {
        super(baseRepository, baseMapper);
        this.trackingRepo = trackingRepo;
        this.mapboxService = mapboxService;
        this.deliveryRepository = deliveryRepository;
    }

    //    /**
    //     * Chạy mỗi 3 giờ một lần
    //     * Có thể chỉnh cron: "0 0 */3 * * ?" → mỗi 3 tiếng
    //            * hoặc "0 0 0 * * ?" → mỗi ngày lúc 0h
    //     **/
    @Scheduled(cron = "0 0 */3 * * ?")
    //        @Scheduled(cron = "0 */3 * * * ?")
    @Transactional
    public void scheduledTracking() {
        log.info("Running scheduled delivery tracking...");

        List<Delivery> activeDeliveries = deliveryRepository.findAllByDeliveryStatusIn(
                (List.of(StatusDelivery.DELIVERING, StatusDelivery.PENDING)));

        for (Delivery delivery : activeDeliveries) {
            try {
                trackDelivery(delivery);
            } catch (Exception e) {
                log.error("Failed to track delivery {}: {}", delivery.getCode(), e.getMessage());
            }
        }
    }

    private void trackDelivery(Delivery delivery) {
        Delivery delivery1 = deliveryRepository.findById(delivery.getId()).orElseThrow();
        if (delivery1.getDeliveryStatus().equals(StatusDelivery.PENDING)) {
            delivery1.setDeliveryStatus(StatusDelivery.DELIVERING);
            deliveryRepository.save(delivery1);
        }

        // ⚙️ Giả lập GPS driver (ở thực tế: lấy từ thiết bị hoặc API)
        double baseLat = delivery.getPickupWarehouse().getLatitude();
        double baseLon = delivery.getPickupWarehouse().getLongitude();

        // Giả lập di chuyển ngẫu nhiên trong bán kính 0.01 độ (~1km)
        double simulatedLat = baseLat + (Math.random() - 0.5) / 50;
        double simulatedLon = baseLon + (Math.random() - 0.5) / 50;

        DeliveryTrackingHistory tracking = DeliveryTrackingHistory.builder()
                .delivery(delivery)
                .locationDescription(mapboxService
                        .getAddressFromCoordinates(simulatedLat, simulatedLon)
                        .block())
                .latitude(simulatedLat)
                .longitude(simulatedLon)
                .timestamp(LocalDateTime.now())
                .build();

        trackingRepo.save(tracking);

        log.info("✅ Updated tracking for delivery {} at ({}, {})", delivery.getCode(), simulatedLat, simulatedLon);
    }

    public Page<DeliveryTrackingHistoryResponse> getTrackingHistoryByDeliveryId(String deliveryId, Pageable pageable) {
        return trackingRepo.findAllByDelivery_Id(deliveryId, pageable).map(baseMapper::toDTO);
    }

    public Page<DeliveryTrackingHistoryResponse> filter(TrackingHistoryFilter filter, Pageable pageable) {
        TrackingHistorySpecification specification = new TrackingHistorySpecification(filter);
        return trackingRepo.findAll(specification, pageable).map(baseMapper::toDTO);
    }
}
