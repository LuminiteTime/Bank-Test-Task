package com.example.bank.repository;

import com.example.bank.model.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<BankUser, Long>, JpaSpecificationExecutor<BankUser> {
    default Optional<BankUser> findByEmail(String email) {
        return findAll().stream()
                .filter(u -> u.getEmailAddresses().contains(email))
                .findFirst();
    }
    default Optional<BankUser> findByPhone(String phone) {
        return findAll().stream()
                .filter(u -> u.getPhoneNumbers().contains(phone))
                .findFirst();
    }
    Optional<BankUser> findByLogin(String login);
}
