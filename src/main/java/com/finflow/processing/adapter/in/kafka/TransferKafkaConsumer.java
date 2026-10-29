package com.finflow.processing.adapter.in.kafka;

import com.finflow.processing.domain.model.Transfer;
import com.finflow.processing.domain.port.in.TransferUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de Entrada (Driving Adapter) responsável por consumir
 * eventos assíncronos do Apache Kafka.
 */
@Component
@RequiredArgsConstructor
public class TransferKafkaConsumer {

    // Porta de entrada (Use Case) injetada para manter o desacoplamento da Arquitetura Hexagonal
    private final TransferUseCase transferUseCase;

    /**
     * Ouve o tópico do Kafka e processa a transferência recebida.
     *
     * @param_topics: Tópico onde o transfer-service publica os pedidos de transferência.
     * @param_groupId: Grupo de consumidores que garante balanceamento de carga no Kafka.
     */
    @KafkaListener(
            topics = "transfer-requested"
            ,groupId = "processing-group"
    )
    public void listen(Transfer transfer) {
        // Delega a execução para o Caso de Uso da aplicação
        transferUseCase.transferReceived(transfer);
    }
}
