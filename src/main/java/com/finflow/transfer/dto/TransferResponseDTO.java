package com.finflow.transfer.dto;

import com.finflow.transfer.enums.Status;


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
