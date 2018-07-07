package com.jzielinski.banktransferservice.service.transfer;

import com.jzielinski.banktransferservice.service.transfer.entity.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferService {
    Transfer transferFunds(long fromAccount, long toAccount, BigDecimal amount);

    List<Transfer> getAllTransfers();

    List<Transfer> getAllTransfersForAccount(long accountId);

    List<Transfer> getIncomingTransfers(long accountId);

    List<Transfer> getOutgoingTransfers(long accountId);
}
