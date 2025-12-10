package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.cvv.scm_link.dto.filter.InventoryFilter;
import com.cvv.scm_link.entity.InventoryLevel;

public class InventorySpecification extends BaseSpecification<InventoryLevel, InventoryFilter> {
    protected InventorySpecification(InventoryFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<InventoryLevel> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }

        String key = "%" + filter.getKeyword().toLowerCase() + "%";

        return cb.or(
                like(cb, root.join("product").get("code"), key),
                like(cb, root.join("warehouse").get("name"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<InventoryLevel> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getFromQuantityAvailable() > 0) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("quantityAvailable"), filter.getFromQuantityAvailable()));
        }

        if (filter.getToQuantityAvailable() > 0) {
            predicates.add(cb.lessThanOrEqualTo(root.get("quantityAvailable"), filter.getToQuantityAvailable()));
        }

        if (filter.getFromQuantityReserved() > 0) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("quantityReserved"), filter.getFromQuantityReserved()));
        }

        if (filter.getToQuantityReserved() > 0) {
            predicates.add(cb.lessThanOrEqualTo(root.get("quantityReserved"), filter.getToQuantityReserved()));
        }

        if (filter.getFromQuantityOnHand() > 0) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("quantityOnHand"), filter.getFromQuantityOnHand()));
        }

        if (filter.getToQuantityOnHand() > 0) {
            predicates.add(cb.lessThanOrEqualTo(root.get("quantityOnHand"), filter.getToQuantityOnHand()));
        }

        if (StringUtils.hasText(filter.getProduct())) {
            predicates.add(root.join("product").get("code").in(filter.getProduct()));
        }

        if (StringUtils.hasText(filter.getWarehouse())) {
            predicates.add(root.join("warehouse").get("name").in(filter.getWarehouse()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
