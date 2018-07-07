package com.jzielinski.banktransferservice.service.account;

import com.google.inject.AbstractModule;
import com.jzielinski.banktransferservice.service.account.repository.AccountRepository;
import com.jzielinski.banktransferservice.service.account.repository.InMemoryAccountRepository;

public class AccountModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountRepository.class).to(InMemoryAccountRepository.class);
        bind(AccountService.class).to(AccountServiceImpl.class);
    }
}
