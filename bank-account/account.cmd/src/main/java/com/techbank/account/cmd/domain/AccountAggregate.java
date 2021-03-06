package com.techbank.account.cmd.domain;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {

    private Boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent
                .builder()
                .id(command.getId())
                .createdDate(new Date())
                .accountHolder(command.getAccountHolder())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build()
        );
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("Funds cant be deposited when inactive");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposite must be greater than 0");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build()
        );
    }

    public void apply(FundsDepositedEvent event) {
        this.id = event.getId();
        this.balance = event.getAmount();
    }

    public void withdrawFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("Funds cant be withdrawn when inactive");
        }
        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build()
        );
    }

    public void apply(FundsWithdrawnEvent event) {
        this.id = event.getId();
        this.balance = event.getAmount();
    }

    public void closeAccount() {
        if (!this.active) {
            throw new IllegalStateException("Account already closed");
        }
        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build()
        );
    }

    public void apply(AccountClosedEvent event) {
        this.id = event.getId();
        this.active = false;
    }

    public double getBalance() {
        return balance;
    }
}
