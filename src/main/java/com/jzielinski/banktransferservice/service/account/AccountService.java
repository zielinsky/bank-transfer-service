package com.jzielinski.banktransferservice.service.account;

import com.jzielinski.banktransferservice.service.account.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    void creditAccount(long id, BigDecimal amount);

    void debitAccount(long id, BigDecimal amount);

    Optional<Account> getAccountById(long id);

    List<Account> getAllAccounts();

    Account createAccount(long id, BigDecimal balance);

    Account createAccount(long id);
}
