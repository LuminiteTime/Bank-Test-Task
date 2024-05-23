package com.example.bank.controller;

import com.example.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    // TODO: Add accounts endpoints management.
}
