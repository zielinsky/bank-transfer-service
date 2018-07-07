package com.jzielinski.banktransferservice.api;

import com.jzielinski.banktransferservice.api.handler.*;
import ratpack.func.Action;
import ratpack.handling.Chain;

import javax.inject.Singleton;

@Singleton
public class RoutingChain implements Action<Chain> {

    @Override
    public void execute(Chain chain) throws Exception {
        chain
                .prefix("accounts", accounts -> accounts
                        .get(":id", GetAccountByIdHandler.class)
                        .get(":id/transfers", TransfersByAccountIdHandler.class)
                        .get(":id/transfers/incoming", IncomingTransfersHandler.class)
                        .get(":id/transfers/outgoing", OutgoingTransfersHandler.class)
                        .all(AccountsHandler.class))
                .prefix("transfers", transfers -> transfers
                        .all(TransfersHandler.class));
    }
}
