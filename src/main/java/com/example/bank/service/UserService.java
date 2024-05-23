package com.example.bank.service;

import com.example.bank.dto.AddDeleteContactInfoRequest;
import com.example.bank.dto.RegisterUserRequest;
import com.example.bank.dto.UpdateContactInfoRequest;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.model.Account;
import com.example.bank.model.BankUser;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.UserRepository;
import com.example.bank.utils.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public BankUser registerUser(RegisterUserRequest request) {
        userRepository.findByLogin(request.getLogin())
                .ifPresent(u -> new IllegalArgumentException("User with login " + request.getLogin() + " already exists."));
        userRepository.findByPhone(request.getPhone())
                .ifPresent(u -> new IllegalArgumentException("User with phone " + request.getPhone() + " already exists."));
        userRepository.findByEmail(request.getEmail()).
                ifPresent(u -> new IllegalArgumentException("User with email " + request.getEmail() + " already exists."));

        BankUser newUser = BankUser.builder()
                .login(request.getLogin())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .birthDate(request.getBirthDate())
                .phoneNumbers(Set.of(request.getPhone()))
                .emailAddresses(Set.of(request.getEmail()))
                .build();
        Account account = Account.builder()
                .initialBalance(request.getInitialBalance())
                .balance(request.getInitialBalance())
                .maxBalance(request.getInitialBalance().multiply(BigDecimal.valueOf(2.07)))
                .build();
        newUser.setAccount(account);
        accountRepository.save(newUser.getAccount());
        return userRepository.save(newUser);
    }

    public BankUser addContactInfo(Long userId, AddDeleteContactInfoRequest request) {
        BankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (request.getPhoneNumbers() != null) {
            for (String phone: request.getPhoneNumbers()) {
                userRepository.findByPhone(phone)
                        .ifPresent(u -> {
                            if (!u.getId().equals(userId)) {
                                throw new IllegalArgumentException("User with phone " + phone + " already exists.");
                            }
                        });
                user.addPhone(phone);
            }
        }

        if (request.getEmailAddresses() != null) {
            for (String email: request.getEmailAddresses()) {
                userRepository.findByEmail(email)
                        .ifPresent(u -> {
                            if (!u.getId().equals(userId)) {
                                throw new IllegalArgumentException("User with email " + email + " already exists.");
                            }
                        });
                user.addEmail(email);
            }
        }

        return userRepository.save(user);
    }

    public BankUser updateContactInfo(Long userId, UpdateContactInfoRequest request) {
        BankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (request.getPhoneNumbers() != null) {
            for (String phoneToChange: request.getPhoneNumbers().keySet()) {
                if (!user.getPhoneNumbers().contains(phoneToChange)) {
                    throw new IllegalArgumentException("User " + userId + " does not have phone " + phoneToChange);
                }
                String newPhone = request.getPhoneNumbers().get(phoneToChange);
                userRepository.findByPhone(newPhone)
                        .ifPresent(u -> {
                            if (!u.getId().equals(userId)) {
                                throw new IllegalArgumentException("User with phone " + request.getPhoneNumbers().get(phoneToChange) + " already exists.");
                            }
                        });
                user.removePhone(phoneToChange);
                user.addPhone(newPhone);
            }
        }

        if (request.getEmailAddresses() != null) {
            for (String emailToChange: request.getEmailAddresses().keySet()) {
                if (!user.getEmailAddresses().contains(emailToChange)) {
                    throw new IllegalArgumentException("User " + userId + " does not have email " + emailToChange);
                }
                String newEmail = request.getEmailAddresses().get(emailToChange);
                userRepository.findByEmail(newEmail)
                        .ifPresent(u -> {
                            if (!u.getId().equals(userId)) {
                                throw new IllegalArgumentException("User with email " + request.getEmailAddresses().get(emailToChange) + " already exists.");
                            }
                        });
                user.removeEmail(emailToChange);
                user.addEmail(newEmail);
            }
        }

        return userRepository.save(user);
    }

    public BankUser deleteContactInfo(Long userId, AddDeleteContactInfoRequest request) {
        BankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (request.getPhoneNumbers() != null) {
            for (String phone: request.getPhoneNumbers()) {
                if (!user.getPhoneNumbers().contains(phone)) {
                    throw new IllegalArgumentException("User " + userId + " does not have phone " + request.getPhoneNumbers());
                }
                if (user.getPhoneNumbers().size() == 1) {
                    throw new IllegalArgumentException("User " + userId + " must have at least one phone number");
                }
                user.removePhone(phone);
            }
        }
        if (request.getEmailAddresses() != null) {
            for (String email: request.getEmailAddresses()) {
                if (!user.getEmailAddresses().contains(email)) {
                    throw new IllegalArgumentException("User " + userId + " does not have email " + request.getEmailAddresses());
                }
                if (user.getEmailAddresses().size() == 1) {
                    throw new IllegalArgumentException("User " + userId + " must have at least one email address");
                }
                user.removeEmail(email);
            }
        }

        return userRepository.save(user);
    }

    public List<BankUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<BankUser> searchUsers(LocalDate birthDate, String phone, String fullName, String email, Pageable pageable) {
        return userRepository.findAll(UserSpecification.filterUsers(birthDate, phone, fullName, email), pageable);
    }
}