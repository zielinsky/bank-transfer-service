package com.jzielinski.banktransferservice.service.transfer.repository;

import com.jzielinski.banktransferservice.service.transfer.entity.Transfer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransferRepository {
    Optional<Transfer> getById(UUID id);
    List<Transfer> getAllTransfers();
    List<Transfer> getByFromAccount(long accountId);
    List<Transfer> getByToAccount(long accountId);
    List<Transfer> getByAccount(long accountId);
    void save(Transfer transfer);
}
