package com.example.bank.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class RegisterUserRequest {
    @NotBlank
    private String login;
    @NotBlank
    private String password;

    // В ТЗ сказано, что у пользователя должны быть указаны ФИО и дата рождения, но при регистрации пользователя
    // через служебный апи не сказано, что надо передавать ФИО и дату рождения, поэтому я не накладывал на них
    // ограничения NotNull и NotBlank. Однако, всё равно не понятно, как задаются вообще ФИО и дата рождения пользователям.
    private String fullName;
    private LocalDate birthDate;

    @Min(value = 0, message = "Initial balance must be positive")
    private BigDecimal initialBalance;
    @NotBlank
    private String phone;
    @NotBlank
    private String email;
}
