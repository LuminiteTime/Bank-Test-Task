package com.example.bank.controller;

import com.example.bank.dto.*;
import com.example.bank.model.BankUser;
import com.example.bank.service.TransactionService;
import com.example.bank.service.BankUserService;
import com.example.bank.utils.MappingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class BankUserController {
    private final BankUserService bankUserService;
    private final TransactionService transactionService;

    @DeleteMapping("/{userId}/contact-info/remove")
    public ResponseEntity<BankUserDTO> deleteContactInfo(@PathVariable Long userId,
                                                         @RequestBody AddDeleteContactInfoRequest request) {
        log.debug("Deleting some contact info for user {}...", userId);
        return ResponseEntity.ok(MappingUtils.mapToBankUserDTO(bankUserService.deleteContactInfo(userId, request)));
    }

    @PatchMapping("/{userId}/contact-info/update")
    public ResponseEntity<BankUserDTO> updateContactInfo(@PathVariable Long userId,
                                                         @RequestBody UpdateContactInfoRequest request) {
        log.debug("Updating contact info for user {}...", userId);
        return ResponseEntity.ok(MappingUtils.mapToBankUserDTO(bankUserService.updateContactInfo(userId, request)));
    }

    @PatchMapping("/{userId}/contact-info/add")
    public ResponseEntity<BankUserDTO> addContactInfo(@PathVariable Long userId,
                                                      @RequestBody AddDeleteContactInfoRequest request) {
        log.debug("Adding new contact info for user {}...", userId);
        return ResponseEntity.ok(MappingUtils.mapToBankUserDTO(bankUserService.addContactInfo(userId, request)));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BankUserDTO>> searchUsers(
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
        log.debug("Searching for users with birthDate={}, phone={}, fullName={}, email={}", birthDate, phone, fullName, email);
        Page<BankUser> users = bankUserService.searchUsers(birthDate, phone, fullName, email, PageRequest.of(page, size, Sort.by(orders)));
        return ResponseEntity.ok(users.map(MappingUtils::mapToBankUserDTO));
    }

    @PatchMapping("/{userIdSender}/transfer-to/{userIdReceiver}/{amount}")
    public ResponseEntity<BankUserDTO> transfer(@PathVariable Long userIdSender,
                                             @PathVariable Long userIdReceiver,
                                             @PathVariable BigDecimal amount) {
        log.debug("Transferring {} from user {} to user {}...", amount, userIdSender, userIdReceiver);
        return ResponseEntity.ok(transactionService.transfer(userIdSender, userIdReceiver, amount));
    }
}
