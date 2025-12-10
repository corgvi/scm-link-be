package com.cvv.scm_link.mapper;

import com.cvv.scm_link.dto.response.stats.RecentOrderResponse;
import com.cvv.scm_link.entity.OrderItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cvv.scm_link.dto.request.OrderCreateRequest;
import com.cvv.scm_link.dto.request.OrderUpdateRequest;
import com.cvv.scm_link.dto.response.OrderResponse;
import com.cvv.scm_link.entity.Order;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper extends BaseMapper<Order, OrderCreateRequest, OrderUpdateRequest, OrderResponse> {
    @Override
    @Mapping(target = "user", ignore = true)
    OrderResponse toDTO(Order entity);

    default RecentOrderResponse toRecentOrderDTO(Order order) {

        OrderItems firstItem = order.getOrderItems().isEmpty()
                ? null
                : order.getOrderItems().get(0);

        String productName = firstItem != null ? firstItem.getProduct().getName() : null;
        String productImage = firstItem != null ? firstItem.getProduct().getImageUrl() : null;
        String category = firstItem != null ? firstItem.getProduct().getCategory().getName() : null;

        return RecentOrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getCode())
                .productName(productName)
                .productImage(productImage)
                .category(category)
                .variants(order.getOrderItems().size())
                .totalAmount(BigDecimal.valueOf(order.getTotalAmount()))
                .status(order.getOrderStatus())
                .customerName(order.getCustomerName())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
