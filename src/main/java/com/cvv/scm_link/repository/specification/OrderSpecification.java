package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.cvv.scm_link.dto.filter.OrderFilter;
import com.cvv.scm_link.entity.Order;

public class OrderSpecification extends BaseSpecification<Order, OrderFilter> {
    public OrderSpecification(OrderFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<Order> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }

        String key = "%" + filter.getKeyword().toLowerCase() + "%";

        return cb.or(
                like(cb, root.get("code"), key),
                like(cb, root.get("customerName"), key),
                like(cb, root.get("orderStatus"), key),
                like(cb, root.get("paymentStatus"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<Order> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasText(filter.getCode())) {
            predicates.add(
                    cb.like(cb.lower(root.get("code")), "%" + filter.getCode().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getCustomerName())) {
            predicates.add(cb.like(
                    cb.lower(root.get("customerName")),
                    "%" + filter.getCustomerName().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getCustomerPhone())) {
            predicates.add(cb.like(
                    cb.lower(root.get("customerPhone")),
                    "%" + filter.getCustomerPhone().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getOrderStatus())) {
            predicates.add(cb.like(
                    cb.lower(root.get("orderStatus")),
                    "%" + filter.getOrderStatus().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getPaymentStatus())) {
            predicates.add(cb.like(
                    cb.lower(root.get("paymentStatus")),
                    "%" + filter.getPaymentStatus().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getShippingCity())) {
            predicates.add(cb.like(
                    cb.lower(root.get("shippingCity")),
                    "%" + filter.getShippingCity().toLowerCase() + "%"));
        }
        if (filter.getFromAmount() > 0) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("totalAmount"), filter.getFromAmount()));
        }
        if (filter.getToAmount() > 0) {
            predicates.add(cb.lessThanOrEqualTo(root.get("totalAmount"), filter.getToAmount()));
        }
        if (StringUtils.hasText(filter.getCustomer())) {
            predicates.add(root.join("customer").get("username").in(filter.getCustomer()));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
