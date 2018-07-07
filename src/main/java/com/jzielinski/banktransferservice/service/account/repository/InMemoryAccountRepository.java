package com.jzielinski.banktransferservice.service.account.repository;

import com.jzielinski.banktransferservice.service.account.entity.Account;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> getById(long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void save(Account account) {
        accounts.put(account.getId(), account);
    }
}
