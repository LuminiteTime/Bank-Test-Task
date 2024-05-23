package com.example.bank.service;

import com.example.bank.model.Account;
import com.example.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class BalanceUpdateService {

    private final AccountRepository accountRepository;

    @Scheduled(cron = "0 */1 * * * *")
    public void updateBalances() {
        for (Account account: accountRepository.findAll()) {
            account.setBalance(account.getBalance().multiply(BigDecimal.valueOf(1.05)).compareTo(account.getMaxBalance()) < 0 ?
                    account.getBalance().multiply(BigDecimal.valueOf(1.05)) :
                    account.getBalance());
            accountRepository.save(account);
        }
    }
}
