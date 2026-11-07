package com.finflow.processing.adapter.in.kafka;

import com.finflow.processing.domain.model.Transfer;
import com.finflow.processing.domain.port.in.TransferUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de Entrada (Driving Adapter) responsável por consumir
 * eventos assíncronos do Apache Kafka.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransferKafkaConsumer {

    private final TransferUseCase transferUseCase;

    @KafkaListener(
            topics = "transfer-requested",
            groupId = "processing-group"
    )
    public void listen(Transfer transfer) {
        log.info("Evento recebido do Kafka com sucesso: {}", transfer);
        try {
            Transfer processed = transferUseCase.transferReceived(transfer);
            log.info("Transferência processada e salva com ID={} e Status={}", processed.getId(), processed.getStatus());
        } catch (Exception e) {
            log.error("Erro ao processar transferência recebida do Kafka", e);
        }
    }
}
