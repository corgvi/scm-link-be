package com.cvv.scm_link.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class Category extends BaseEntity{
    String name;
    String description;
    @Column(nullable = false, unique = true, name = "code", columnDefinition = "VARCHAR(10) COLLATE utf8mb4_unicode_ci")
    String code;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<Product> products;

    public void setCode(String code) {
        this.code = (code == null) ? null : code.toUpperCase();
    }
}
