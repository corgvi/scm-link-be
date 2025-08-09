package com.cvv.scm_link.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(Permission.class)
public class Permission {
    @Id
    String name;

    String description;
}
