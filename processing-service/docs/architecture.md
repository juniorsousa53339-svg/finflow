# Arquitetura do Sistema — FinFlow

## 1. Visão Geral da Arquitetura

O **FinFlow** foi concebido para evoluir de uma aplicação monolítica inicial (focada no `transfer-service`) para uma arquitetura distribuída baseada em microsserviços desacoplados por mensageria assíncrona. Esta evolução permite estudar e aplicar conceitos fundamentais de sistemas distribuídos de alta confiabilidade.

---

## 2. Microsserviços e Responsabilidades

### 2.1. `transfer-service` (Gateway & Entrada de Transações)
- **Papel principal:** Ponto de entrada para as solicitações de transferência financeira dos clientes.
- **Responsabilidades:**
  - Exposição de endpoints REST padronizados.
  - Validação de formato e consistência inicial dos dados (Bean Validation).
  - Validação de regras de negócio síncronas (ex: contas existem, status ativo, saldo suficiente).
  - Persistência inicial da solicitação de transferência com status `PENDING` ou `CREATED`.
  - Publicação de evento de transferência solicitada para o barramento de mensagens (Kafka).

### 2.2. `processing-service` (Processamento Assíncrono & Resiliência)
- **Papel principal:** Processamento de segundo plano das transferências.
- **Responsabilidades:**
  - Consumo assíncrono de eventos de transferência do Apache Kafka.
  - Execução da efetivação da transferência entre contas (débito e crédito atômicos).
  - Tratamento de falhas transacionais, políticas de retry (tentativas) e fila de mensagens mortas (Dead Letter Queue - DLQ).
  - Atualização do status final da transferência (`COMPLETED`, `FAILED`).

---

## 3. Fluxo de Comunicação e Mensageria

1. **Síncrono (HTTP/REST):** O cliente (Frontend ou API Client) envia um `POST /api/transfers` para o `transfer-service`.
2. **Assíncrono (Kafka):** Após validar e persistir a intenção de transferência, o `transfer-service` publica uma mensagem no tópico `transfer-requested`.
3. **Processamento:** O `processing-service` consome a mensagem, executa as regras de consolidação e persiste o resultado.

---

## 4. Padrões Técnicos e Decisões de Design

- **Idempotência:** Garantia de que repetições da mesma requisição (devido a falhas de rede ou retentativas) não gerem duplicação de transferências.
- **Consistência Eventual:** O processamento pesado ocorre de forma assíncrona, desacoplando a resposta ao cliente da execução bancária de fundo.
- **Tratamento Global de Exceções:** Padronização de respostas de erro da API com `ProblemDetails` / `ResponseEntityExceptionHandler`.
