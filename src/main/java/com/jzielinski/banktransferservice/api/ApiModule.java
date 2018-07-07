package com.jzielinski.banktransferservice.api;

import com.google.inject.AbstractModule;
import com.jzielinski.banktransferservice.api.handler.*;
import ratpack.error.ServerErrorHandler;

public class ApiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GetAccountByIdHandler.class);
        bind(AccountsHandler.class);
        bind(TransfersHandler.class);
        bind(TransfersByAccountIdHandler.class);
        bind(IncomingTransfersHandler.class);
        bind(OutgoingTransfersHandler.class);
        bind(RoutingChain.class);
        bind(ServerErrorHandler.class).to(ExceptionHandler.class);
    }
}
