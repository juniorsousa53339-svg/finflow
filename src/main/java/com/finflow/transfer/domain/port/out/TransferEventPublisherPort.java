package com.finflow.transfer.domain.port.out;

import com.finflow.transfer.domain.model.Transfer;

/**
 * Porta de Saída (Output Port) responsável por definir o contrato de publicação de eventos de transferência.
 * Seguindo a Arquitetura Hexagonal, o domínio define O QUÊ deve ser feito (publicar evento),
 * mas não sabe COMO (se é via Kafka, RabbitMQ, SQS, etc.).
 */
public interface TransferEventPublisherPort {

    /**
     * Publica um evento indicando que uma nova transferência foi solicitada/criada.
     *
     * @param transfer O objeto de domínio da transferência criada.
     */
    void publishTransferCreated(Transfer transfer);
}

