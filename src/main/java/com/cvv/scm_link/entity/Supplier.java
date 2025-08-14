package com.cvv.scm_link.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier extends BaseEntity{
    String name;
    String contactPerson;
    String email;
    String address;
    String phoneNumber;
    String taxId;
    String note;
    @Column(nullable = false, unique = true, name = "code", columnDefinition = "VARCHAR(10) COLLATE utf8mb4_unicode_ci")
    String code;
    Boolean isActive;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    List<Product> products;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    List<ReceivingNote> receivingNotes;

    @PrePersist
    @PreUpdate
    public void normalizeCode() {
        this.code = (code == null) ? null : code.toUpperCase();
    }
}
