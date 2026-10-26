package com.finflow.transfer.domain.model;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter @Setter
@NoArgsConstructor
public class Transfer {

    private Long id;
    private Integer sourceAccount;
    private Integer destinationAccount;
    private BigDecimal amount;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
