package com.example.bank.service;

import com.example.bank.dto.AddDeleteContactInfoRequest;
import com.example.bank.dto.BankUserDTO;
import com.example.bank.dto.RegisterUserRequest;
import com.example.bank.dto.UpdateContactInfoRequest;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.model.Account;
import com.example.bank.model.BankUser;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.BankUserRepository;
import com.example.bank.utils.MappingUtils;
import com.example.bank.utils.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankUserService {
    private final BankUserRepository bankUserRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public BankUser registerUser(RegisterUserRequest request) {
        bankUserRepository.findByLogin(request.getUsername())
                .ifPresent(u -> new IllegalArgumentException("User with login " + request.getUsername() + " already exists."));
        bankUserRepository.findByPhone(request.getPhone())
                .ifPresent(u -> new IllegalArgumentException("User with phone " + request.getPhone() + " already exists."));
        bankUserRepository.findByEmail(request.getEmail()).
                ifPresent(u -> new IllegalArgumentException("User with email " + request.getEmail() + " already exists."));

        BankUser newUser = BankUser.builder()
                .login(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
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
        bankUserRepository.save(newUser);

        log.info("User with id {} saved", newUser.getId());

        return newUser;
    }

    public BankUser addContactInfo(Long userId, AddDeleteContactInfoRequest request) {
        BankUser user = bankUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (request.getPhoneNumbers() != null) {
            for (String phone: request.getPhoneNumbers()) {
                bankUserRepository.findByPhone(phone)
                        .ifPresent(u -> {
                            if (!u.getId().equals(userId)) {
                                log.warn("User with phone {} already exists.", phone);
                                throw new IllegalArgumentException("User with phone " + phone + " already exists.");
                            }
                        });
                user.addPhone(phone);
            }
        }

        if (request.getEmailAddresses() != null) {
            for (String email: request.getEmailAddresses()) {
                bankUserRepository.findByEmail(email)
                        .ifPresent(u -> {
                            if (!u.getId().equals(userId)) {
                                log.warn("User with email {} already exists.", email);
                                throw new IllegalArgumentException("User with email " + email + " already exists.");
                            }
                        });
                user.addEmail(email);
            }
        }

        bankUserRepository.save(user);

        log.info("New contact info for user {} is added", userId);

        return user;
    }

    public BankUser updateContactInfo(Long userId, UpdateContactInfoRequest request) {
        BankUser user = bankUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (request.getPhoneNumbers() != null) {
            for (String phoneToChange: request.getPhoneNumbers().keySet()) {
                if (!user.getPhoneNumbers().contains(phoneToChange)) {
                    log.warn("User {} does not have phone {}", userId, phoneToChange);
                    throw new IllegalArgumentException("User " + userId + " does not have phone " + phoneToChange);
                }
                String newPhone = request.getPhoneNumbers().get(phoneToChange);
                bankUserRepository.findByPhone(newPhone)
                        .ifPresent(u -> {
                            if (!u.getId().equals(userId)) {
                                log.warn("User with phone {} already exists.", newPhone);
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
                    log.warn("User {} does not have email {}", userId, emailToChange);
                    throw new IllegalArgumentException("User " + userId + " does not have email " + emailToChange);
                }
                String newEmail = request.getEmailAddresses().get(emailToChange);
                bankUserRepository.findByEmail(newEmail)
                        .ifPresent(u -> {
                            if (!u.getId().equals(userId)) {
                                log.warn("User with email {} already exists.", newEmail);
                                throw new IllegalArgumentException("User with email " + request.getEmailAddresses().get(emailToChange) + " already exists.");
                            }
                        });
                user.removeEmail(emailToChange);
                user.addEmail(newEmail);
            }
        }

        bankUserRepository.save(user);

        log.info("Contact info for user {} is updated", userId);

        return user;
    }

    public BankUser deleteContactInfo(Long userId, AddDeleteContactInfoRequest request) {
        BankUser user = bankUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (request.getPhoneNumbers() != null) {
            for (String phone: request.getPhoneNumbers()) {
                if (!user.getPhoneNumbers().contains(phone)) {
                    log.warn("User {} does not have phone {}", userId, phone);
                    throw new IllegalArgumentException("User " + userId + " does not have phone " + request.getPhoneNumbers());
                }
                if (user.getPhoneNumbers().size() == 1) {
                    log.warn("User {} must have at least one phone number", userId);
                    throw new IllegalArgumentException("User " + userId + " must have at least one phone number");
                }
                user.removePhone(phone);
            }
        }
        if (request.getEmailAddresses() != null) {
            for (String email: request.getEmailAddresses()) {
                if (!user.getEmailAddresses().contains(email)) {
                    log.warn("User {} does not have email {}", userId, email);
                    throw new IllegalArgumentException("User " + userId + " does not have email " + request.getEmailAddresses());
                }
                if (user.getEmailAddresses().size() == 1) {
                    log.warn("User {} must have at least one email address", userId);
                    throw new IllegalArgumentException("User " + userId + " must have at least one email address");
                }
                user.removeEmail(email);
            }
        }

        bankUserRepository.save(user);

        log.info("Some contact info for user {} is deleted", userId);

        return user;
    }

    public List<BankUser> getAllUsers() {
        return bankUserRepository.findAll();
    }

    public Page<BankUser> searchUsers(LocalDate birthDate, String phone, String fullName, String email, Pageable pageable) {
        return bankUserRepository.findAll(UserSpecification.filterUsers(birthDate, phone, fullName, email), pageable);
    }

    public BankUserDTO getUserById(Long userId) {
        return MappingUtils.mapToBankUserDTO(bankUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId)));
    }
}
