package com.jzielinski.banktransferservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class TransferDto {
    private final UUID uuid;
    private final long fromAccount;
    private final long toAccount;
    private final BigDecimal amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final ZonedDateTime createdOn;

    public TransferDto(
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("fromAccount") long fromAccount,
            @JsonProperty("toAccount") long toAccount,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("createdOn") ZonedDateTime createdOn) {
        this.uuid = uuid;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.createdOn = createdOn;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
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
}
