package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.cvv.scm_link.dto.request.DeliveryTrackingHistoryRequest;
import com.cvv.scm_link.dto.response.DeliveryTrackingHistoryResponse;
import com.cvv.scm_link.entity.DeliveryTrackingHistory;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DeliveryTrackingHistoryMapper
        extends BaseMapper<
                DeliveryTrackingHistory,
                DeliveryTrackingHistoryRequest,
                DeliveryTrackingHistoryRequest,
                DeliveryTrackingHistoryResponse> {
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Override
    DeliveryTrackingHistoryResponse toDTO(DeliveryTrackingHistory entity);
}
