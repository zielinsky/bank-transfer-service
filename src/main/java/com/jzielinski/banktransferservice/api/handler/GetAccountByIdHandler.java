package com.jzielinski.banktransferservice.api.handler;

import com.jzielinski.banktransferservice.api.RequestHandlerException;
import com.jzielinski.banktransferservice.api.dto.AccountDto;
import com.jzielinski.banktransferservice.service.account.AccountService;
import com.jzielinski.banktransferservice.service.account.entity.Account;
import io.netty.handler.codec.http.HttpResponseStatus;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.Jackson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GetAccountByIdHandler implements Handler {

    private final AccountService accountService;

    @Inject
    public GetAccountByIdHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            Account account = accountService.getAccountById(Long.parseLong(ctx.getPathTokens().get("id")))
                    .orElseThrow(() -> new RequestHandlerException(HttpResponseStatus.NOT_FOUND.code(),
                            "The account with given id does not exist."));
            ctx.render(Jackson.json(new AccountDto(account.getId(), account.getBalance())));
        } catch (NumberFormatException e) {
            throw new RequestHandlerException(HttpResponseStatus.BAD_REQUEST.code(),
                    "Account id has to be an integer number.", e);
        }
    }
}
