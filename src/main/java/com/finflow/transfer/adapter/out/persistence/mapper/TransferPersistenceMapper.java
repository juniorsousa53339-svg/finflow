package com.finflow.transfer.adapter.out.persistence.mapper;

import com.finflow.transfer.adapter.out.persistence.entity.TransferJpaEntity;
import com.finflow.transfer.domain.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransferPersistenceMapper{

    // Converte do Domínio para a Entidade JPA (para salvar no banco)
    public TransferJpaEntity toEntity(Transfer transfer) {
        if (transfer == null) {
            return null;
        }
        TransferJpaEntity entity = new TransferJpaEntity();
        entity.setId(transfer.getId());
        entity.setSourceAccount(transfer.getSourceAccount());
        entity.setDestinationAccount(transfer.getDestinationAccount());
        entity.setAmount(transfer.getAmount());
        entity.setStatus(transfer.getStatus());
        entity.setCreatedAt(transfer.getCreatedAt());
        entity.setUpdatedAt(transfer.getUpdatedAt());
        return entity;
    }

    // Converte da Entidade JPA para o Domínio (quando busca do banco)
    public Transfer toDomain(TransferJpaEntity entity) {
        if (entity == null) {
            return null;
        }
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
}
