package com.finflow.processing.application.service;

import com.finflow.processing.domain.model.Status;
import com.finflow.processing.domain.model.Transfer;
import com.finflow.processing.domain.port.in.TransferUseCase;
import com.finflow.processing.domain.port.out.TransferRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferProcessService implements TransferUseCase {

    private final TransferRepositoryPort transferRepositoryPort;

    @Override
    public Transfer transferReceived(Transfer transfer) {
        transfer.setStatus(Status.COMPLETED);
        return transferRepositoryPort.save(transfer);
    }
}
