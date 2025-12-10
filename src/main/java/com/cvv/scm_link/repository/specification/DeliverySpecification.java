package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.cvv.scm_link.dto.filter.DeliveryFilter;
import com.cvv.scm_link.entity.Delivery;

public class DeliverySpecification extends BaseSpecification<Delivery, DeliveryFilter> {
    public DeliverySpecification(DeliveryFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<Delivery> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }

        String key = "%" + filter.getKeyword().toLowerCase() + "%";

        return cb.or(like(cb, root.get("code"), key), like(cb, root.get("deliveryStatus"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<Delivery> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(filter.getCode())) {
            predicates.add(
                    cb.like(cb.lower(root.get("code")), "%" + filter.getCode().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filter.getDeliveryStatus())) {
            predicates.add(cb.like(
                    cb.lower(root.get("deliveryStatus")),
                    "%" + filter.getDeliveryStatus().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filter.getVehicle())) {
            predicates.add(root.join("vehicle").get("licensePlate").in(filter.getVehicle()));
        }

        if (StringUtils.hasText(filter.getShipper())) {
            predicates.add(root.join("user").get("username").in(filter.getShipper()));
        }

        if (filter.getFromDistanceKm() > 0) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("totalDistanceKm"), filter.getFromDistanceKm()));
        }

        if (filter.getToDistanceKm() > 0) {
            predicates.add(cb.lessThanOrEqualTo(root.get("totalDistanceKm"), filter.getToDistanceKm()));
        }

        if (filter.getFromDurationM() > 0) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("totalDurationMinutes"), filter.getFromDurationM()));
        }

        if (filter.getToDurationM() > 0) {
            predicates.add(cb.lessThanOrEqualTo(root.get("totalDurationMinutes"), filter.getToDurationM()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
