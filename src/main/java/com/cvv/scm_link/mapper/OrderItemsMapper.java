package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cvv.scm_link.dto.request.OrderItemsRequest;
import com.cvv.scm_link.dto.response.OrderDetailResponse;
import com.cvv.scm_link.entity.OrderItems;

@Mapper(componentModel = "spring")
public interface OrderItemsMapper
        extends BaseMapper<OrderItems, OrderItemsRequest, OrderItemsRequest, OrderDetailResponse> {
    @Override
    @Mapping(target = "product", ignore = true)
    OrderDetailResponse toDTO(OrderItems entity);
}
