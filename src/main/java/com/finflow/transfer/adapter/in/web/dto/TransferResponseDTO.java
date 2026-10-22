package com.finflow.transfer.adapter.in.web.dto;

import com.finflow.transfer.domain.model.Status;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponseDTO(
        Long id,
        Integer sourceAccount,
        Integer destinationAccount,
        BigDecimal amount,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {


}
