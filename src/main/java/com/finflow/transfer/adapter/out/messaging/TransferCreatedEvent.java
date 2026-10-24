package com.finflow.transfer.adapter.out.messaging;

import com.finflow.transfer.domain.model.Transfer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) que representa o Evento publicado no Kafka
 * quando uma transferência é criada com sucesso.
 *
 * Por que usamos um evento separado e não enviamos a entidade de domínio direto?
 * Porque o contrato da mensagem (evento) deve ser estável e desacoplado de detalhes
 * internos do banco de dados ou da entidade de domínio, permitindo evolução independente.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferCreatedEvent {

    private Long transferId;
    private Integer sourceAccount;
    private Integer destinationAccount;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

    /**
     * Método utilitário (Factory Method) para converter o objeto de Domínio (Transfer)
     * em um Evento Kafka (`TransferCreatedEvent`).
     */
    public static TransferCreatedEvent fromDomain(Transfer transfer) {
        return TransferCreatedEvent.builder()
                .transferId(transfer.getId())
                .sourceAccount(transfer.getSourceAccount())
                .destinationAccount(transfer.getDestinationAccount())
                .amount(transfer.getAmount())
                .status(transfer.getStatus() != null ? transfer.getStatus().name() : null)
                .createdAt(transfer.getCreatedAt())
                .build();
    }
}
