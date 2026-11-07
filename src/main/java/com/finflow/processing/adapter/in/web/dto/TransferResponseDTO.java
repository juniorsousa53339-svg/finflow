package com.finflow.processing.adapter.in.web.dto;

import com.finflow.processing.domain.model.Status;
import com.finflow.processing.domain.model.Transfer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferResponseDTO {
    private Long id;
    private Integer sourceAccount;
    private Integer destinationAccount;
    private BigDecimal amount;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TransferResponseDTO fromDomain(Transfer transfer) {
        TransferResponseDTO dto = new TransferResponseDTO();
        dto.setId(transfer.getId());
        dto.setSourceAccount(transfer.getSourceAccount());
        dto.setDestinationAccount(transfer.getDestinationAccount());
        dto.setAmount(transfer.getAmount());
        dto.setStatus(transfer.getStatus());
        dto.setCreatedAt(transfer.getCreatedAt());
        dto.setUpdatedAt(transfer.getUpdatedAt());
        return dto;
    }
}
