package com.finflow.transfer.adapter.out.persistence;

import com.finflow.transfer.adapter.out.persistence.entity.TransferJpaEntity;
import com.finflow.transfer.adapter.out.persistence.mapper.TransferPersistenceMapper;
import com.finflow.transfer.adapter.out.persistence.repository.TransferJpaRepository;
import com.finflow.transfer.domain.model.Transfer;
import com.finflow.transfer.domain.port.out.TransferPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferJpaAdapter implements TransferPersistencePort {

    private final TransferJpaRepository transferJpaRepository; // Ferramenta do Spring Data JPA para falar com o banco
    private final TransferPersistenceMapper transferPersistenceMapper; // Tradutor entre Domínio e Entidade JPA

    @Override
    public Transfer save(Transfer transfer) {

        // Passo A: O Domínio entrega um objeto 'Transfer'. O banco não entende 'Transfer',
        // ele entende 'TransferJpaEntity'. Então usamos o mapper para traduzir.
        TransferJpaEntity entity = transferPersistenceMapper.toEntity(transfer);

        // Passo B: Salvamos a entidade no banco usando o Spring Data JPA.
        // O repositório nos devolve a entidade já salva (com ID gerado, datas, etc.).
        TransferJpaEntity savedEntity = transferJpaRepository.save(entity);

        // Passo C: O Domínio não pode receber 'TransferJpaEntity' de volta,
        // ele só fala a língua de Domínio ('Transfer').
        // Então usamos o mapper novamente para traduzir de volta para 'Transfer'.
        return transferPersistenceMapper.toDomain(savedEntity);
    }
}
