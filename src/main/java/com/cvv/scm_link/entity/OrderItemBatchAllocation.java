package com.cvv.scm_link.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "order_item_batch_allocation")
public class OrderItemBatchAllocation extends BaseEntity {
    int quantityAllocated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderItem_id")
    OrderItems orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryLocationDetail_id")
    InventoryLocationDetail inventoryLocationDetail;
}
