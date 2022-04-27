package com.techbank.account.query.infrastructure.handlers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AccountEventHandler implements EventHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        BankAccount bankAccount = BankAccount.builder()
                .id(event.getId())
                .creationDate(new Date())
                .accountType(event.getAccountType())
                .accountHolder(event.getAccountHolder())
                .balance(event.getOpeningBalance())
                .build();

        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        Optional<BankAccount> bankAccount = accountRepository.findById(event.getId());
        if (!bankAccount.isPresent()) {
            return;
        }

        double currentBalance = bankAccount.get().getBalance();
        double latestBalance = currentBalance + event.getAmount();

        bankAccount.get().setBalance(latestBalance);
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        Optional<BankAccount> bankAccount = accountRepository.findById(event.getId());
        if (!bankAccount.isPresent()) {
            return;
        }

        double currentBalance = bankAccount.get().getBalance();
        double latestBalance = currentBalance - event.getAmount();

        bankAccount.get().setBalance(latestBalance);
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }
}
