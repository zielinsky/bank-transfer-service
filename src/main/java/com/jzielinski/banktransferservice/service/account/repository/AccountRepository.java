package com.jzielinski.banktransferservice.service.account.repository;

import com.jzielinski.banktransferservice.service.account.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> getById(long id);
    List<Account> getAllAccounts();
    void save(Account account);
}
