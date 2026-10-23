package com.finflow.transfer.application.service;

import com.finflow.transfer.domain.model.Transfer;
import com.finflow.transfer.domain.port.in.CreateTransferUseCase;
import com.finflow.transfer.domain.port.out.TransferPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferService implements CreateTransferUseCase {

    private final TransferPersistencePort transferPersistencePort;

    @Override
    public Transfer createTransfer(Transfer transfer) {

        return transferPersistencePort.save(transfer);
    }
}
