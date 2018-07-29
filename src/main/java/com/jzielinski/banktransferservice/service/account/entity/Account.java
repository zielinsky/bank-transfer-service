package com.jzielinski.banktransferservice.service.account.entity;

import com.jzielinski.banktransferservice.service.account.exception.NotEnoughFundsException;

import java.math.BigDecimal;

public class Account {
    // For the sake of simplicity, the account id is just a plain number and it is assigned manually
    private final long id;
    // Also to keep it simple, there are no currencies, and the account balance is stored as a BigDecimal
    private BigDecimal balance;

    public Account(long id) {
        this.id = id;
        this.balance = BigDecimal.ZERO;
    }

    /*
    To make testing easier, I allow creation of the account with initial balance other than zero.
    In a "real" system, this probably could be an event sourced object, where the initial state always has zero
    balance, and the state could be modified only by domain events.
    */
    public Account(long id, BigDecimal initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public synchronized void creditAccount(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public synchronized void debitAccount(BigDecimal amount) {
        if(!hasEnoughFunds(amount)) {
            throw new NotEnoughFundsException();
        }
        balance = balance.subtract(amount);
    }

    private boolean hasEnoughFunds(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public long getId() {
        return id;
    }
}
