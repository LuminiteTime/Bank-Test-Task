package com.example.bank.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "bank_users")
@Entity
public class BankUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String login;
    private String password;
    private LocalDate birthDate;

    @ElementCollection
    @CollectionTable(name = "phone_numbers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "phone_numbers")
    private Set<String> phoneNumbers = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "email_addresses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "email_addresses")
    private Set<String> emailAddresses = new HashSet<>();

    @OneToOne
    private Account account;

    public void addPhone(String phone) {
        this.phoneNumbers.add(phone);
    }

    public void addEmail(String email) {
        this.emailAddresses.add(email);
    }

    public void removePhone(String phone) {
        this.phoneNumbers.remove(phone);
    }

    public void removeEmail(String email) {
        this.emailAddresses.remove(email);
    }
}



