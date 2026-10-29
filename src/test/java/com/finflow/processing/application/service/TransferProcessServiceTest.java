package com.finflow.processing.application.service;
import com.finflow.processing.domain.model.Status;
import com.finflow.processing.domain.model.Transfer;
import com.finflow.processing.domain.port.out.TransferRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TransferProcessServiceTest {

    @Mock
    private TransferRepositoryPort transferRepositoryPort;

    @InjectMocks
    private TransferProcessService transferProcessService;

    // TODO: Escreva seu primeiro teste unitário aqui!

    @Test
    @DisplayName("Deve processar a  transferência com sucesso")
    void shouldProcessTransferSuccessfully () {
        // ---- 1. ARRANGE (Preparação) ----
        Transfer transferInput = new Transfer();
        transferInput.setId(1L);
        transferInput.setSourceAccount(100);
        transferInput.setDestinationAccount(200);
        transferInput.setAmount(new BigDecimal(200));
        transferInput.setStatus(Status.PENDING);

        Transfer transferSaved = new Transfer();
        transferSaved.setId(1L);
        transferSaved.setSourceAccount(100);
        transferSaved.setDestinationAccount(200);
        transferSaved.setAmount(BigDecimal.valueOf(250.00));
        transferSaved.setStatus(Status.COMPLETED);

        // Ensinando o mock do repositório a retornar a transferência salva
        when(transferRepositoryPort.save(any(Transfer.class))).thenReturn(transferSaved);

        // ---- 2. ACT (Ação) ----
        Transfer result = transferProcessService.transferReceived(transferInput);

        // ---- 3. ASSERT (Verificação) ----
        assertNotNull(result);
        assertEquals(Status.COMPLETED, result.getStatus());
        assertEquals(BigDecimal.valueOf(250.00), result.getAmount());

        // Valida se o repositório foi chamado exatamente 1 vez
        verify(transferRepositoryPort, times(1)).save(any(Transfer.class));
    }
}

