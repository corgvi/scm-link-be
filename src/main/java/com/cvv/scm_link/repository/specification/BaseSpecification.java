package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.*;

import org.springframework.data.jpa.domain.Specification;

import com.cvv.scm_link.dto.BaseFilter;

public abstract class BaseSpecification<T, F extends BaseFilter> implements Specification<T> {

    protected final F filter;

    protected BaseSpecification(F filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        query.distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        Predicate keywordPredicate = keywordPredicate(root, cb);
        if (keywordPredicate != null) {
            predicates.add(keywordPredicate);
        }

        addDateRange(predicates, root, cb);

        Predicate extra = additionalFilters(root, cb);
        if (extra != null) {
            predicates.add(extra);
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    protected Predicate keywordPredicate(Root<T> root, CriteriaBuilder cb) {
        return null;
    }

    protected Predicate additionalFilters(Root<T> root, CriteriaBuilder cb) {
        return null;
    }

    protected void addDateRange(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb) {

        if (filter.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getFromDate()));
        }

        if (filter.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getToDate()));
        }
    }

    protected Predicate like(CriteriaBuilder cb, Expression<String> path, String value) {
        return cb.like(cb.lower(path), "%" + value.toLowerCase() + "%");
    }
}
