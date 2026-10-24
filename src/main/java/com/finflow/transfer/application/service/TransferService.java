package com.finflow.transfer.application.service;

import com.finflow.transfer.domain.exception.BusinessRuleException;
import com.finflow.transfer.domain.model.Transfer;
import com.finflow.transfer.domain.port.in.CreateTransferUseCase;
import com.finflow.transfer.domain.port.out.TransferPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferService implements CreateTransferUseCase {

    private final TransferPersistencePort transferPersistencePort;


    private void validarRegrasTransferencia(Transfer transfer) {
        if (transfer.getSourceAccount().equals(transfer.getDestinationAccount())) {
            throw new BusinessRuleException("A conta de origem não pode ser igual à conta de destino.");
        }
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        validarRegrasTransferencia(transfer);
        return transferPersistencePort.save(transfer);
    }
}
