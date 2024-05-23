package com.example.bank.service;

import com.example.bank.model.Account;
import com.example.bank.model.BankUser;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    //TODO: Check if the sender is authorized.
    @Transactional
    public BankUser transfer(Long senderId, Long receiverId, BigDecimal amount) {
        BankUser sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        BankUser receiver = userRepository.findById(receiverId)
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

        return sender;
    }
}