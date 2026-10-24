package com.finflow.transfer.adapter.out.messaging;

import com.finflow.transfer.domain.model.Transfer;
import com.finflow.transfer.domain.port.out.TransferEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Adaptador de Saída (Output Adapter) para o Apache Kafka.
 * Implementa a porta {@link TransferEventPublisherPort} definida no domínio.
 *
 * Esta classe traduz a chamada de negócio em uma mensagem publicada em um tópico Kafka,
 * mantendo o núcleo da aplicação (domínio) 100% isolado de tecnologias de mensageria.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTransferPublisherAdapter implements TransferEventPublisherPort {

    // KafkaTemplate é o componente do Spring for Apache Kafka responsável por enviar mensagens.
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Nome do tópico Kafka para onde os eventos de transferência criada serão enviados.
    private static final String TOPIC_TRANSFER_REQUESTED = "finflow.transfer.requested";

    @Override
    public void publishTransferCreated(Transfer transfer) {
        log.info("Iniciando publicação do evento de transferência criada para o ID: {}", transfer.getId());

        // 1. Converte o modelo de domínio para o DTO de evento Kafka
        TransferCreatedEvent event = TransferCreatedEvent.fromDomain(transfer);

        // 2. Definimos a chave da mensagem (usamos o ID da transferência convertido para String).
        // A chave garante que mensagens com a mesma chave possam ir para a mesma partição (ordenação).
        String messageKey = String.valueOf(transfer.getId());

        try {
            // 3. Envia a mensagem de forma assíncrona para o Kafka
            kafkaTemplate.send(TOPIC_TRANSFER_REQUESTED, messageKey, event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Evento publicado com sucesso no tópico [{}] | Partition: {} | Offset: {} | Transfer ID: {}",
                                    TOPIC_TRANSFER_REQUESTED,
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset(),
                                    transfer.getId());
                        } else {
                            log.error("Falha ao publicar evento de transferência ID [{}] no tópico [{}]",
                                    transfer.getId(), TOPIC_TRANSFER_REQUESTED, ex);
                            // Em um cenário de produção avançado, aqui poderíamos registrar em tabela de "Outbox"
                            // ou disparar um mecanismo de retry/compensação.
                        }
                    });

        } catch (Exception e) {
            log.error("Erro inesperado ao tentar enviar mensagem para o Kafka para a transferência ID: {}", transfer.getId(), e);
            throw new RuntimeException("Erro ao publicar evento de transferência", e);
        }
    }
}
