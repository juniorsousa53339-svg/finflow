package com.finflow.transfer.adapter.in.web.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;


public record TransferRequestDTO(

        @NotNull(message = "A conta de origem é obrigatória")
        Integer sourceAccount,

        @NotNull(message = "A conta de destino é obrigatória")
        Integer destinationAccount,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor da transferência deve ser maior que zero")
        BigDecimal amount
) {
}
