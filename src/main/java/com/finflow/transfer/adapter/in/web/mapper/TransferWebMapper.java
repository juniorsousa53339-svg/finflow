package com.finflow.transfer.adapter.in.web.mapper;

import com.finflow.transfer.adapter.in.web.dto.TransferRequestDTO;
import com.finflow.transfer.adapter.in.web.dto.TransferResponseDTO;
import com.finflow.transfer.domain.model.Status;
import com.finflow.transfer.domain.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransferWebMapper {
    // Converte o Request DTO da API para o modelo de Dominio
    public Transfer toDomain(TransferRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        Transfer transfer = new Transfer();
        transfer.setSourceAccount(requestDTO.sourceAccount());
        transfer.setDestinationAccount(requestDTO.destinationAccount());
        transfer.setAmount(requestDTO.amount());
        transfer.setStatus(Status.PENDING);  // Toda Transferencia nasce pendente
        return transfer;
    }
    // Converte o modelo de Domínio para Response DTO da API
    public TransferResponseDTO toResponseDTO (Transfer transfer) {
        if (transfer == null) {
            return null;
        }
        return new TransferResponseDTO(
                transfer.getId(),
                transfer.getSourceAccount(),
                transfer.getDestinationAccount(),
                transfer.getAmount(),
                transfer.getStatus(),
                transfer.getCreatedAt(),
                transfer.getUpdatedAt()

        );
    }
}
