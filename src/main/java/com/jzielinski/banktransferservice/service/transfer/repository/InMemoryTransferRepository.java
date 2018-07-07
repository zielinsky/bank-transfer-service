package com.jzielinski.banktransferservice.service.transfer.repository;

import com.jzielinski.banktransferservice.service.transfer.entity.Transfer;

import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
public class InMemoryTransferRepository implements TransferRepository {

    private final Map<UUID, Transfer> transfers = new ConcurrentHashMap<>();

    @Override
    public Optional<Transfer> getById(UUID id) {
        return Optional.ofNullable(transfers.get(id));
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return new ArrayList<>(transfers.values());
    }

    @Override
    public List<Transfer> getByFromAccount(long accountId) {
        return transfers.values().stream()
                .filter(transfer -> transfer.getFromAccount() == accountId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> getByToAccount(long accountId) {
        return transfers.values().stream()
                .filter(transfer -> transfer.getToAccount() == accountId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> getByAccount(long accountId) {
        return transfers.values().stream()
                .filter(transfer -> transfer.getToAccount() == accountId
                        || transfer.getFromAccount() == accountId)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Transfer transfer) {
        transfers.put(transfer.getUuid(), transfer);
    }
}
