package com.example.bank.controller;

import com.example.bank.dto.AddDeleteContactInfoRequest;
import com.example.bank.dto.RegisterUserRequest;
import com.example.bank.dto.UpdateContactInfoRequest;
import com.example.bank.model.BankUser;
import com.example.bank.service.TransactionService;
import com.example.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<BankUser>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<BankUser> registerUser(@RequestBody RegisterUserRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @DeleteMapping("/{userId}/contact-info")
    public ResponseEntity<BankUser> deleteContactInfo(@PathVariable Long userId, @RequestBody AddDeleteContactInfoRequest request) {
        return ResponseEntity.ok(userService.deleteContactInfo(userId, request));
    }

    @PatchMapping("/{userId}/update-contact-info")
    public ResponseEntity<BankUser> updateContactInfo(@PathVariable Long userId, @RequestBody UpdateContactInfoRequest request) {
        return ResponseEntity.ok(userService.updateContactInfo(userId, request));
    }

    @PatchMapping("/{userId}/add-contact-info")
    public ResponseEntity<BankUser> addContactInfo(@PathVariable Long userId, @RequestBody AddDeleteContactInfoRequest request) {
        return ResponseEntity.ok(userService.addContactInfo(userId, request));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BankUser>> searchUsers(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String sortOrder: sort) {
            String[] _sort = sortOrder.split(",");
            orders.add(new Sort.Order(Sort.Direction.fromString(_sort[1]), _sort[0]));
        }

        return ResponseEntity.ok(userService.searchUsers(birthDate, phone, fullName, email, PageRequest.of(page, size, Sort.by(orders))));
    }

    @PatchMapping("/{userIdSender}/transfer-to/{userIdReceiver}/{amount}")
    public ResponseEntity<BankUser> transfer(@PathVariable Long userIdSender,
                                             @PathVariable Long userIdReceiver,
                                             @PathVariable BigDecimal amount) {
        return ResponseEntity.ok(transactionService.transfer(userIdSender, userIdReceiver, amount));
    }
}
