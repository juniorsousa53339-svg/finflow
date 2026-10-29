package com.finflow.processing.adapter.out.persistence;

import com.finflow.processing.adapter.out.entity.TransferEntity;
import com.finflow.processing.domain.model.Transfer;
import com.finflow.processing.domain.port.out.TransferRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransferPersistenceAdapter implements TransferRepositoryPort {

    private final SpringDataTransferRepository repository;

    // --- MAPPERS: Tradutores entre o Domínio Puro e a Infraestrutura JPA ---

    public TransferEntity toEntity(Transfer transfer) {
        TransferEntity entity = new TransferEntity();
        entity.setId(transfer.getId());
        entity.setSourceAccount(transfer.getSourceAccount());
        entity.setDestinationAccount(transfer.getDestinationAccount());
        entity.setAmount(transfer.getAmount());
        entity.setStatus(transfer.getStatus());
        entity.setCreatedAt(transfer.getCreatedAt());
        entity.setUpdatedAt(transfer.getUpdatedAt());
        return entity;
    }

    public Transfer toDomain(TransferEntity entity) {
        Transfer transfer = new Transfer();
        transfer.setId(entity.getId());
        transfer.setSourceAccount(entity.getSourceAccount());
        transfer.setDestinationAccount(entity.getDestinationAccount());
        transfer.setAmount(entity.getAmount());
        transfer.setStatus(entity.getStatus());
        transfer.setCreatedAt(entity.getCreatedAt());
        transfer.setUpdatedAt(entity.getUpdatedAt());
        return transfer;
    }

    @Override
    public Transfer save(Transfer transfer) {
        // 1. Converte domínio para entidade JPA, salva no banco e converte o resultado de volta para domínio
        return toDomain(
                repository.save(toEntity(transfer))
        );
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        // Busca no banco e se encontrar a Entity, mapeia (converte) para o modelo de domínio Transfer
        return repository.findById(id)
                .map(this::toDomain);

    }
}
