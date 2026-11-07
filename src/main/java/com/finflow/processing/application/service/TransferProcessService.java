package com.finflow.processing.application.service;

import com.finflow.processing.domain.model.Status;
import com.finflow.processing.domain.model.Transfer;
import com.finflow.processing.domain.port.in.TransferUseCase;
import com.finflow.processing.domain.port.out.TransferRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferProcessService implements TransferUseCase {

    private final TransferRepositoryPort transferRepositoryPort;

    @Override
    public Transfer transferReceived(Transfer transfer) {
        log.info("Processando transferência ID: {}, Valor: {}", transfer.getId(), transfer.getAmount());
        transfer.setStatus(Status.COMPLETED);
        Transfer saved = transferRepositoryPort.save(transfer);
        log.info("Transferência salva no banco com sucesso!");
        return saved;
    }
}
