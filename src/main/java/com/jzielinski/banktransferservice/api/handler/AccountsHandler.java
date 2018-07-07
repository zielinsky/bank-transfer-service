package com.jzielinski.banktransferservice.api.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.jzielinski.banktransferservice.api.RequestHandlerException;
import com.jzielinski.banktransferservice.api.dto.AccountDto;
import com.jzielinski.banktransferservice.service.account.AccountService;
import com.jzielinski.banktransferservice.service.account.entity.Account;
import com.jzielinski.banktransferservice.service.account.exception.AccountAlreadyExistsException;
import io.netty.handler.codec.http.HttpResponseStatus;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.Jackson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class AccountsHandler implements Handler {
    private final AccountService accountService;

    @Inject
    public AccountsHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.byMethod(x -> x
                .get(this::getAccounts)
                .post(this::createAccount));
    }

    private void getAccounts(Context ctx) {
        List<AccountDto> accountDtoList = accountService.getAllAccounts().stream()
                .map(account -> new AccountDto(account.getId(), account.getBalance()))
                .collect(Collectors.toList());
        ctx.render(Jackson.json(accountDtoList));
    }

    private void createAccount(Context ctx) {
        ctx.parse(Jackson.fromJson(AccountDto.class))
                .mapError(JsonParseException.class, e -> {
                    throw new RequestHandlerException(HttpResponseStatus.BAD_REQUEST.code(),
                            "There was an error in the request body.", e);
                })
                .then(accountDto -> createAccount(ctx, accountDto));
    }

    private void createAccount(Context ctx, AccountDto accountDto) {
        try {
            Account account = accountDto.getBalance() == null ?
                    accountService.createAccount(accountDto.getId()) :
                    accountService.createAccount(accountDto.getId(), accountDto.getBalance());

            ctx.getResponse().status(HttpResponseStatus.CREATED.code());
            ctx.render(Jackson.json(new AccountDto(
                    account.getId(),
                    account.getBalance()
            )));
        } catch (AccountAlreadyExistsException e) {
            throw new RequestHandlerException(HttpResponseStatus.FORBIDDEN.code(),
                    "Account with the given id already exists.", e);
        }
    }
}
