package com.example.bank.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "Username is not provided")
    private String username;

    @NotNull(message = "Password is not provided")
    private String password;
}
