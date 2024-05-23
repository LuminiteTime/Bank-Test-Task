package com.example.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 0, message = "Balance must be positive")
    private BigDecimal balance;
    @Min(value = 0, message = "Balance must be positive")
    private BigDecimal initialBalance;
    private BigDecimal maxBalance;
}
