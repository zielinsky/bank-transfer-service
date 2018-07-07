package com.jzielinski.banktransferservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class AccountDto {
    private final long id;
    private final BigDecimal balance;

    public AccountDto(@JsonProperty("id") long id, @JsonProperty("balance") BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
