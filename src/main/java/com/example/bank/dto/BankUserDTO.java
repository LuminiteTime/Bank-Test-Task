package com.example.bank.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BankUserDTO {
    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private BigDecimal balance;
    private Set<String> phoneNumbers;
    private Set<String> emailAddresses;
}
