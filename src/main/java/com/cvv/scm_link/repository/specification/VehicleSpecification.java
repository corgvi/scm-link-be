package com.cvv.scm_link.repository.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.cvv.scm_link.dto.filter.VehicleFilter;
import com.cvv.scm_link.entity.Vehicle;

public class VehicleSpecification extends BaseSpecification<Vehicle, VehicleFilter> {
    public VehicleSpecification(VehicleFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<Vehicle> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }
        String key = "%" + filter.getKeyword().toLowerCase() + "%";
        return cb.or(like(cb, root.get("licensePlate"), key), like(cb, root.get("type"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<Vehicle> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getLicensePlate() != null && !filter.getLicensePlate().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("licensePlate")),
                    "%" + filter.getLicensePlate().toLowerCase() + "%"));
        }
        if (filter.getType() != null && !filter.getType().isBlank()) {
            predicates.add(
                    cb.like(cb.lower(root.get("type")), "%" + filter.getType().toLowerCase() + "%"));
        }
        if (filter.getIsAvailable() != null) {
            predicates.add(cb.equal(root.get("isAvailable"), filter.getIsAvailable()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
