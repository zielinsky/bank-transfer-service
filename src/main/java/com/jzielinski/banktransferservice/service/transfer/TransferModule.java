package com.jzielinski.banktransferservice.service.transfer;

import com.google.inject.AbstractModule;
import com.jzielinski.banktransferservice.service.transfer.repository.InMemoryTransferRepository;
import com.jzielinski.banktransferservice.service.transfer.repository.TransferRepository;

public class TransferModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TransferRepository.class).to(InMemoryTransferRepository.class);
        bind(TransferService.class).to(TransferServiceImpl.class);
    }
}
