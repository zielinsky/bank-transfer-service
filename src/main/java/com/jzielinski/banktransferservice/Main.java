package com.jzielinski.banktransferservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jzielinski.banktransferservice.api.ApiModule;
import com.jzielinski.banktransferservice.api.RoutingChain;
import com.jzielinski.banktransferservice.service.account.AccountModule;
import com.jzielinski.banktransferservice.service.transfer.TransferModule;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;

public class Main {

    public static void main(String[] args) throws Exception {
        RatpackServer.start(server -> server
                .registry(Guice.registry(x -> x
                        .module(AccountModule.class)
                        .module(TransferModule.class)
                        .module(ApiModule.class)
                        .add(ObjectMapper.class, new ObjectMapper().registerModule(new JavaTimeModule()))))
                .handlers(chain -> chain
                        .insert(RoutingChain.class))
        );
    }
}
