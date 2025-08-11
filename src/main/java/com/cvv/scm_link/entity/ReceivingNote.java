package com.cvv.scm_link.entity;

import jakarta.persistence.Entity;
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
public class ReceivingNote extends BaseEntity{
    String receivingType;
    String status;
    Integer totalItemsExpected;
    Integer totalItemsReceived;
    String note;
}
