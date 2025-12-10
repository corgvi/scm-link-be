package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.cvv.scm_link.dto.filter.WarehouseFilter;
import com.cvv.scm_link.entity.Warehouse;

public class WarehouseSpecification extends BaseSpecification<Warehouse, WarehouseFilter> {
    public WarehouseSpecification(WarehouseFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<Warehouse> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }
        String key = "%" + filter.getKeyword().toLowerCase() + "%";
        return cb.or(like(cb, root.get("name"), key), like(cb, root.get("city"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<Warehouse> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            predicates.add(
                    cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }
        if (filter.getCity() != null && !filter.getCity().isBlank()) {
            predicates.add(
                    cb.like(cb.lower(root.get("city")), "%" + filter.getCity().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
