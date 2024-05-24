package com.example.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class BankUserDTO {
    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private Set<String> phoneNumbers;
    private Set<String> emailAddresses;
}
