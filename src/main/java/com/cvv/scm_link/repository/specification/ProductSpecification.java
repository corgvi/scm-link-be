package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.cvv.scm_link.dto.filter.ProductFilter;
import com.cvv.scm_link.entity.Product;

public class ProductSpecification extends BaseSpecification<Product, ProductFilter> {
    public ProductSpecification(ProductFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<Product> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }

        String key = "%" + filter.getKeyword().toLowerCase() + "%";

        return cb.or(like(cb, root.get("name"), key), like(cb, root.get("sku"), key), like(cb, root.get("code"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<Product> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(filter.getSku())) {
            predicates.add(cb.like(cb.lower(root.get("sku")), filter.getSku().toUpperCase() + "%"));
        }
        if (StringUtils.hasText(filter.getCode())) {
            predicates.add(
                    cb.like(cb.lower(root.get("code")), "%" + filter.getCode().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getName())) {
            predicates.add(
                    cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getBranchName())) {
            predicates.add(cb.like(
                    cb.lower(root.join("branchName").get("name")),
                    "%" + filter.getBranchName().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getCategory())) {
            predicates.add(root.join("category").get("code").in(filter.getCategory()));
        }
        if (StringUtils.hasText(filter.getSupplier())) {
            predicates.add(root.join("supplier").get("code").in(filter.getSupplier()));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
