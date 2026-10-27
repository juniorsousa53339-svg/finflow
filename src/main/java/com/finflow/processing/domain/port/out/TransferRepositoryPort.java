package com.finflow.processing.domain.port.out;

import com.finflow.processing.domain.model.Transfer;

import java.util.Optional;

public interface TransferRepositoryPort {
    Transfer save(Transfer transfer);
    Optional<Transfer> findById(Long id);
}
