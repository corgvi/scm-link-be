package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cvv.scm_link.dto.request.OrderRequest;
import com.cvv.scm_link.dto.response.OrderResponse;
import com.cvv.scm_link.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper extends BaseMapper<Order, OrderRequest, OrderRequest, OrderResponse> {
    @Override
    @Mapping(target = "user", ignore = true)
    OrderResponse toDTO(Order entity);
}
