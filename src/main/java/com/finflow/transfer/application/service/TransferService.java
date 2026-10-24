package com.finflow.transfer.application.service;

import com.finflow.transfer.domain.exception.BusinessRuleException;
import com.finflow.transfer.domain.model.Transfer;
import com.finflow.transfer.domain.port.in.CreateTransferUseCase;
import com.finflow.transfer.domain.port.out.TransferEventPublisherPort;
import com.finflow.transfer.domain.port.out.TransferPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService implements CreateTransferUseCase {

    private final TransferPersistencePort transferPersistencePort;
    private final TransferEventPublisherPort transferEventPublisherPort; // Porta de publicação de eventos (Kafka)

    private void validarRegrasTransferencia(Transfer transfer) {
        if (transfer.getSourceAccount().equals(transfer.getDestinationAccount())) {
            throw new BusinessRuleException("A conta de origem não pode ser igual à conta de destino.");
        }
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        // 1. Valida as regras de negócio
        validarRegrasTransferencia(transfer);

        // 2. Persiste a transferência no banco de dados (status inicial PENDING/CREATED)
        Transfer savedTransfer = transferPersistencePort.save(transfer);
        log.info("Transferência salva com sucesso no banco de dados com ID: {}", savedTransfer.getId());

        // 3. Publica o evento no Kafka de forma assíncrona para que o microsserviço de processamento assuma
        transferEventPublisherPort.publishTransferCreated(savedTransfer);

        return savedTransfer;
    }
}

