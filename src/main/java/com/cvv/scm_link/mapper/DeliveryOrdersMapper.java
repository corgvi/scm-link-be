package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cvv.scm_link.dto.request.DeliveryOrdersRequest;
import com.cvv.scm_link.dto.response.DeliveryOrdersResponse;
import com.cvv.scm_link.entity.DeliveryOrders;

@Mapper(componentModel = "spring")
public interface DeliveryOrdersMapper
        extends BaseMapper<DeliveryOrders, DeliveryOrdersRequest, DeliveryOrdersRequest, DeliveryOrdersResponse> {
    @Override
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.code", target = "orderCode")
    @Mapping(source = "order.shippingLatitude", target = "orderLatitude")
    @Mapping(source = "order.shippingLongitude", target = "orderLongitude")
    DeliveryOrdersResponse toDTO(DeliveryOrders entity);
}
