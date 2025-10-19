package com.cvv.scm_link.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.dto.response.OrderItemBatchAllocationResponse;
import com.cvv.scm_link.entity.OrderItemBatchAllocation;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.repository.OrderItemBatchAllocationRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(rollbackFor = AppException.class)
public class OrderItemBatchAllocationService {
    OrderItemBatchAllocationRepository orderItemBatchAllocationRepository;

    public OrderItemBatchAllocationService(OrderItemBatchAllocationRepository orderItemBatchAllocationRepository) {
        this.orderItemBatchAllocationRepository = orderItemBatchAllocationRepository;
    }

    protected OrderItemBatchAllocation save(OrderItemBatchAllocation orderItemBatchAllocation) {
        return orderItemBatchAllocationRepository.save(orderItemBatchAllocation);
    }

    protected void createOrUpdate(String orderItemId, String ildId, OrderItemBatchAllocation allocation) {
        Optional<OrderItemBatchAllocation> existingOpt =
                orderItemBatchAllocationRepository.findByOrderItem_IdAndInventoryLocationDetail_Id(orderItemId, ildId);

        if (existingOpt.isEmpty()) {
            orderItemBatchAllocationRepository.save(allocation);
        } else {
            OrderItemBatchAllocation existing = existingOpt.get();
            existing.setQuantityAllocated(existing.getQuantityAllocated() + allocation.getQuantityAllocated());
            orderItemBatchAllocationRepository.save(existing);
        }
    }

    protected void delete(String id) {
        OrderItemBatchAllocation existingAllocation = orderItemBatchAllocationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED));
        orderItemBatchAllocationRepository.delete(existingAllocation);
    }

    protected List<OrderItemBatchAllocation> findAllByOrderItem_Id(String orderItemId) {
        return orderItemBatchAllocationRepository.findAllByOrderItem_Id(orderItemId);
    }

    protected List<OrderItemBatchAllocationResponse> findOrderItemDetails(String orderItemId) {
        return orderItemBatchAllocationRepository.findOrderItemDetails(orderItemId, false);
    }
}
