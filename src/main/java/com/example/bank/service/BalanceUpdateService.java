package com.example.bank.service;

import com.example.bank.model.Account;
import com.example.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
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
            log.info("Balance of account with id {} updated", account.getId());
        }
        log.info("All balances updated");
    }
}
