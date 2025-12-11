package com.cvv.scm_link.repository.specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.cvv.scm_link.dto.filter.UserFilter;
import com.cvv.scm_link.entity.User;

public class UserSpecification extends BaseSpecification<User, UserFilter> {

    public UserSpecification(UserFilter filter) {
        super(filter);
    }

    @Override
    protected Predicate keywordPredicate(Root<User> root, CriteriaBuilder cb) {
        if (filter.getKeyword() == null || filter.getKeyword().isBlank()) {
            return null;
        }

        String key = "%" + filter.getKeyword().toLowerCase() + "%";

        return cb.or(
                like(cb, root.get("username"), key),
                like(cb, root.get("email"), key),
                like(cb, root.get("fullName"), key));
    }

    @Override
    protected Predicate additionalFilters(Root<User> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        // username LIKE
        if (StringUtils.hasText(filter.getUsername())) {
            predicates.add(cb.like(
                    cb.lower(root.get("username")), "%" + filter.getUsername().toLowerCase() + "%"));
        }

        // email
        if (StringUtils.hasText(filter.getEmail())) {
            predicates.add(
                    cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
        }

        // fullname
        if (StringUtils.hasText(filter.getFullName())) {
            predicates.add(cb.like(
                    cb.lower(root.get("fullName")), "%" + filter.getFullName().toLowerCase() + "%"));
        }

        // phone
        if (StringUtils.hasText(filter.getPhoneNumber())) {
            predicates.add(cb.like(root.get("phoneNumber"), "%" + filter.getPhoneNumber() + "%"));
        }

        // address
        if (StringUtils.hasText(filter.getCity())) {
            predicates.add(
                    cb.like(cb.lower(root.get("city")), "%" + filter.getCity().toLowerCase() + "%"));
        }

        // isActive
        if (filter.getIsActive() != null) {
            predicates.add(cb.equal(root.get("isActive"), filter.getIsActive()));
        }

        // roles
        if (filter.getRoles() != null && !filter.getRoles().isEmpty()) {
            predicates.add(root.join("roles").get("name").in(filter.getRoles()));
        }

        if (filter.getFromDate() != null) {
            filter.setFromDate(filter.getFromDate().with(LocalTime.MAX));
            predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), filter.getFromDate()));
        }

        if (filter.getToDate() != null) {
            filter.setToDate(filter.getToDate().with(LocalTime.MAX));
            predicates.add(cb.lessThanOrEqualTo(root.get("creatAt"), filter.getToDate()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
