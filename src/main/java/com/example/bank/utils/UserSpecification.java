package com.example.bank.utils;

import com.example.bank.model.BankUser;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<BankUser> filterUsers(LocalDate birthDate, String phone, String fullName, String email) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (birthDate != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("birthDate"), birthDate));
            }
            if (phone != null) {
                predicates.add(criteriaBuilder.isMember(phone, root.get("phoneNumbers")));
            }
            if (fullName != null) {
                predicates.add(criteriaBuilder.like(root.get("fullName"), fullName + "%"));
            }
            if (email != null) {
                predicates.add(criteriaBuilder.isMember(email, root.get("emailAddresses")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
