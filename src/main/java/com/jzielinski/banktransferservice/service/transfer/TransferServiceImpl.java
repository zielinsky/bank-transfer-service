package com.jzielinski.banktransferservice.service.transfer;

import com.jzielinski.banktransferservice.service.account.entity.Account;
import com.jzielinski.banktransferservice.service.account.exception.AccountDoesNotExistException;
import com.jzielinski.banktransferservice.service.transfer.entity.Transfer;
import com.jzielinski.banktransferservice.service.transfer.repository.TransferRepository;
import com.jzielinski.banktransferservice.service.account.AccountService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Singleton
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final AccountService accountService;

    @Inject
    public TransferServiceImpl(TransferRepository transferRepository, AccountService accountService) {
        this.transferRepository = transferRepository;
        this.accountService = accountService;
    }

    @Override
    public Transfer transferFunds(long fromAccount, long toAccount, BigDecimal amount) {
        // In a "real" system, this of course would be run in a transaction
        Transfer transfer = new Transfer(fromAccount, toAccount, amount, ZonedDateTime.now());
        accountService.debitAccount(fromAccount, amount);
        accountService.creditAccount(toAccount, amount);
        transferRepository.save(transfer);
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return transferRepository.getAllTransfers();
    }

    @Override
    public List<Transfer> getAllTransfersForAccount(long accountId) {
        return accountService.getAccountById(accountId)
                .map(Account::getId)
                .map(transferRepository::getByAccount)
                .orElseThrow(AccountDoesNotExistException::new);
    }

    @Override
    public List<Transfer> getIncomingTransfers(long accountId) {
        return accountService.getAccountById(accountId)
                .map(Account::getId)
                .map(transferRepository::getByToAccount)
                .orElseThrow(AccountDoesNotExistException::new);
    }

    @Override
    public List<Transfer> getOutgoingTransfers(long accountId) {
        return accountService.getAccountById(accountId)
                .map(Account::getId)
                .map(transferRepository::getByFromAccount)
                .orElseThrow(AccountDoesNotExistException::new);
    }
}
