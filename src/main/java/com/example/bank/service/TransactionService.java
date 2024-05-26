package com.example.bank.service;

import com.example.bank.dto.BankUserDTO;
import com.example.bank.model.Account;
import com.example.bank.model.BankUser;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.UserRepository;
import com.example.bank.repository.BankUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    //TODO: Check if the sender is authorized.
    @Transactional
    public BankUser transfer(Long senderId, Long receiverId, BigDecimal amount) {
        BankUser sender = userRepository.findById(senderId)
    public BankUserDTO transfer(Long senderId, Long receiverId, BigDecimal amount) {
        BankUser sender = bankUserRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        BankUser receiver = bankUserRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        Account senderAccount = sender.getAccount();
        Account receiverAccount = receiver.getAccount();

        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        log.info("Transaction completed: {} transferred from {} to {}", amount, senderId, receiverId);

        return MappingUtils.mapToBankUserDTO(sender);
    }
}