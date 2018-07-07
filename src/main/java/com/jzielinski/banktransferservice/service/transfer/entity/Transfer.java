package com.jzielinski.banktransferservice.service.transfer.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Transfer {
    private final UUID uuid;
    private final long fromAccount;
    private final long toAccount;
    private final BigDecimal amount;
    private final ZonedDateTime createdOn;

    public Transfer(long fromAccount, long toAccount, BigDecimal amount, ZonedDateTime createdOn) {
        this.uuid = UUID.randomUUID();
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.createdOn = createdOn;
    }

    public long getFromAccount() {
        return fromAccount;
    }

    public long getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public UUID getUuid() {
        return uuid;
    }
}
