package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.cvv.scm_link.dto.filter.SupplierFilter;
import com.cvv.scm_link.entity.Supplier;

public class SupplierSpecification extends BaseSpecification<Supplier, SupplierFilter> {

    public SupplierSpecification(SupplierFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<Supplier> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }
        String key = "%" + filter.getKeyword().toLowerCase() + "%";
        return cb.or(like(cb, root.get("name"), key), like(cb, root.get("code"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<Supplier> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(filter.getCode())) {
            predicates.add(
                    cb.like(cb.lower(root.get("code")), "%" + filter.getCode().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getName())) {
            predicates.add(
                    cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getEmail())) {
            predicates.add(
                    cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
