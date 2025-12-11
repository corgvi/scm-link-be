package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.cvv.scm_link.dto.filter.TransactionFilter;
import com.cvv.scm_link.entity.InventoryTransaction;

public class TransactionSpecification extends BaseSpecification<InventoryTransaction, TransactionFilter> {
    public TransactionSpecification(TransactionFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<InventoryTransaction> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }
        String key = "%" + filter.getKeyword().toLowerCase() + "%";
        return cb.or(like(cb, root.get("transactionType"), key), like(cb, root.get("sku"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<InventoryTransaction> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getTransactionType() != null && !filter.getTransactionType().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("transactionType")),
                    "%" + filter.getTransactionType().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
