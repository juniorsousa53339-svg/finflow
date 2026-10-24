package com.finflow.transfer.application.service;

import com.finflow.transfer.domain.exception.BusinessRuleException;
import com.finflow.transfer.domain.model.Status;
import com.finflow.transfer.domain.model.Transfer;
import com.finflow.transfer.domain.port.out.TransferPersistencePort;
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

/**
 * Testes Unitários para o TransferService.
 * Papel do Agente QA: Garantir que as regras de negócio e o comportamento do serviço
 * sejam testados de forma isolada, rápida e sem depender de banco de dados real.
 */
@ExtendWith(MockitoExtension.class) // Habilita o Mockito para gerenciar os mocks nesta classe de teste
class TransferServiceTest {

    @Mock
    private TransferPersistencePort transferPersistencePort; // Cria um mock (dublê) da porta de persistência

    @InjectMocks
    private TransferService transferService; // Injeta os mocks acima dentro do TransferService

    @Test
    @DisplayName("Deve criar uma transferência com sucesso quando as contas forem diferentes")
    void shouldCreateTransferSuccessfully() {
        // Arrange (Preparação do cenário)
        Transfer transferInput = new Transfer();
        transferInput.setSourceAccount(100);
        transferInput.setDestinationAccount(200);
        transferInput.setAmount(BigDecimal.valueOf(150.00));
        transferInput.setStatus(Status.PENDING);

        Transfer transferSaved = new Transfer();
        transferSaved.setId(1L);
        transferSaved.setSourceAccount(100);
        transferSaved.setDestinationAccount(200);
        transferSaved.setAmount(BigDecimal.valueOf(150.00));
        transferSaved.setStatus(Status.PENDING);

        // Simulando o comportamento do repositório (Mock)
        // Quando o save for chamado com qualquer Transfer, retorne o transferSaved
        when(transferPersistencePort.save(any(Transfer.class))).thenReturn(transferSaved);

        // Act (Execução)
        Transfer result = transferService.createTransfer(transferInput);

        // Assert (Verificações)
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100, result.getSourceAccount());
        assertEquals(200, result.getDestinationAccount());
        assertEquals(BigDecimal.valueOf(150.00), result.getAmount());

        // Verifica se o método save da porta foi chamado exatamente 1 vez
        verify(transferPersistencePort, times(1)).save(any(Transfer.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando conta de origem e destino forem iguais")
    void shouldThrowExceptionWhenAccountsAreSame() {
        // Arrange (Preparação com contas iguais)
        Transfer transferInput = new Transfer();
        transferInput.setSourceAccount(100);
        transferInput.setDestinationAccount(100); // Mesma conta!
        transferInput.setAmount(BigDecimal.valueOf(50.00));

        // Act & Assert (Execução e Verificação de Exceção)
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> transferService.createTransfer(transferInput),
                "Deveria ter lançado BusinessRuleException"
        );

        // Valida a mensagem da exceção
        assertEquals("A conta de origem não pode ser igual à conta de destino.", exception.getMessage());

        // Garante que o banco NUNCA foi chamado já que a validação barrou antes
        verify(transferPersistencePort, never()).save(any(Transfer.class));
    }
}
