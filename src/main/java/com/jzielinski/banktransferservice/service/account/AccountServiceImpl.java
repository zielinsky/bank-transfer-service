package com.jzielinski.banktransferservice.service.account;

import com.jzielinski.banktransferservice.service.account.entity.Account;
import com.jzielinski.banktransferservice.service.account.exception.AccountAlreadyExistsException;
import com.jzielinski.banktransferservice.service.account.exception.AccountDoesNotExistException;
import com.jzielinski.banktransferservice.service.account.repository.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Singleton
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Inject
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void creditAccount(long id, BigDecimal amount) {
        Account account = accountRepository.getById(id)
                .orElseThrow(AccountDoesNotExistException::new);
        account.creditAccount(amount);
        accountRepository.save(account);
    }

    @Override
    public void debitAccount(long id, BigDecimal amount) {
        Account account = accountRepository.getById(id)
                .orElseThrow(AccountDoesNotExistException::new);
        account.debitAccount(amount);
        accountRepository.save(account);
    }

    @Override
    public Optional<Account> getAccountById(long id) {
        return accountRepository.getById(id);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }

    @Override
    public Account createAccount(long id, BigDecimal balance) {
        accountRepository.getById(id).ifPresent(account -> {
            throw new AccountAlreadyExistsException();
        });
        Account account = new Account(id, balance);
        accountRepository.save(account);
        return account;
    }

    @Override
    public Account createAccount(long id) {
        return createAccount(id, BigDecimal.ZERO);
    }
}
