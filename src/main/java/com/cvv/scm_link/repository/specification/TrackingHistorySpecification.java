package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.cvv.scm_link.dto.filter.TrackingHistoryFilter;
import com.cvv.scm_link.entity.DeliveryTrackingHistory;

public class TrackingHistorySpecification extends BaseSpecification<DeliveryTrackingHistory, TrackingHistoryFilter> {
    public TrackingHistorySpecification(TrackingHistoryFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<DeliveryTrackingHistory> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }
        String key = "%" + filter.getKeyword().toLowerCase() + "%";
        return cb.or(like(cb, root.get("statusCode"), key), like(cb, root.get("deliveryCode"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<DeliveryTrackingHistory> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStatusCode() != null && !filter.getStatusCode().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("statusCode")),
                    "%" + filter.getStatusCode().toLowerCase() + "%"));
        }
        if (filter.getDeliveryCode() != null && !filter.getDeliveryCode().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("deliveryCode")),
                    "%" + filter.getDeliveryCode().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
