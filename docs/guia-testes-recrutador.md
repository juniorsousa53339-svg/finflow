# 🧪 Guia Passo a Passo: Testando o Ecossistema FinFlow para Recrutadores

Este documento apresenta um roteiro detalhado e profissional para demonstrar e testar o funcionamento completo do ecossistema **FinFlow**, integrando o **`transfer-service`** (produtor REST e mensageria) e o **`processing-service`** (consumidor assíncrono via Apache Kafka e persistência) utilizando **Arquitetura Hexagonal**.

---

## 🏗️ 1. Arquitetura do Fluxo End-to-End (E2E)

```text
[ Cliente / Postman ]
       │
       ▼  HTTP POST /transfers
[ transfer-service ] (Porta 8080)
       │
       ├─► Validação & Persistência (PostgreSQL / H2)
       │
       ▼  Publica Evento (Async)
[ Apache Kafka ] ── Tópico: finflow.transfer.requested
       │
       ▼  Consumo via @KafkaListener
[ processing-service ] (Porta 8081)
       │
       ├─► Processamento de Negócio (Status -> COMPLETED)
       ▼
[ Banco de Dados ] (Persistência Final)
```

---

## 🛠️ 2. Pré-Requisitos

Certifique-se de ter instalado em sua máquina:
- **Java 17 (JDK Temurin)**
- **Maven** (ou use o Maven Wrapper `mvnw` incluso)
- **Docker & Docker Compose** (para rodar o PostgreSQL e o Apache Kafka)
- **PowerShell** (Windows) ou **Bash** (Linux/Mac)
- **Postman**, **cURL** ou ferramenta similar para requisições HTTP

---

## 🚀 3. Roteiro de Demonstração Passo a Passo

### Passo 3.1: Subir a Infraestrutura (PostgreSQL, Zookeeper e Kafka)
Abra o terminal na pasta do **`transfer-service`** onde se encontra o arquivo `docker-compose.yml` e execute:

```powershell
docker compose up -d
```

*Como verificar:*
Execute `docker ps` para garantir que os três containers (`finflow-postgres`, `finflow-zookeeper` e `finflow-kafka`) estão em execução.

---

### Passo 3.2: Executar os Testes Automatizados (Garantia de Qualidade)
Antes de rodar os serviços, demonstre o rigor técnico executando os testes unitários e de integração baseados em **JUnit 5** e **Mockito**:

1. **Testes do `processing-service`:**
   ```powershell
   cd "C:\Users\Luciano Junior\Downloads\processing-service"
   & .\mvnw.cmd test
   ```
   *(Destaque o teste `TransferProcessServiceTest`, que valida de forma isolada as regras de negócio de processamento).*

2. **Testes do `transfer-service`:**
   ```powershell
   cd "C:\Users\Luciano Junior\Downloads\transfer-service"
   & .\mvnw.cmd test
   ```

---

### Passo 3.3: Iniciar o `transfer-service` (Produtor)
No terminal localizado em `transfer-service`, inicie a aplicação conectada ao ambiente Docker ou H2:

```powershell
& .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=docker"
```
*(A aplicação subirá na porta **`8080`**).*

---

### Passo 3.4: Iniciar o `processing-service` (Consumidor)
Abra um **novo terminal** na pasta do `processing-service` e inicie o serviço:

```powershell
cd "C:\Users\Luciano Junior\Downloads\processing-service"
& .\mvnw.cmd spring-boot:run
```
*(A aplicação subirá na porta **`8081`** ou padrão, conectando-se ao broker Kafka para escutar o tópico `finflow.transfer.requested`).*

---

### Passo 3.5: Disparar uma Transferência (Requisição REST)
Com ambos os serviços rodando e o Kafka ativo, envie uma requisição `POST` para o `transfer-service`. Você pode usar o comando abaixo no **PowerShell**:

```powershell
$body = @{
    sourceAccount = 100
    destinationAccount = 200
    amount = 350.75
} | ConvertFrom-Json | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/transfers" -Method Post -Body $body -ContentType "application/json"
```

*Resposta esperada (HTTP 201 Created):*
```json
{
    "id": 1,
    "sourceAccount": 100,
    "destinationAccount": 200,
    "amount": 350.75,
    "status": "PENDING",
    "createdAt": "2026-11-07T10:00:00"
}
```

---

### Passo 3.6: Acompanhar o Fluxo Assíncrono nos Logs 🔍
Observe o comportamento simultâneo nos consoles dos dois microsserviços:

1. **No console do `transfer-service`:**
   - Recebimento da requisição HTTP POST.
   - Validação dos dados (Bean Validation).
   - Persistência inicial no banco com status `PENDING`.
   - **Publicação bem-sucedida** do evento no tópico Kafka `finflow.transfer.requested` (com partition e offset exibidos no log).

2. **No console do `processing-service`:**
   - O `@KafkaListener` intercepta instantaneamente a mensagem do tópico `finflow.transfer.requested`.
   - O caso de uso processa a transferência, atualizando o status para `COMPLETED`.
   - Persistência do estado final no banco de dados do processing service.

---

## 🎯 4. Argumentos Chave para Destacar com o Recrutador

Quando for apresentar este projeto em uma entrevista técnica, enfatize os seguintes pontos:

1. **Arquitetura Hexagonal (Ports & Adapters):**
   > *"O núcleo de domínio de ambos os microsserviços é 100% puro e isolado de frameworks como Spring, JPA ou Kafka. Isso garante alta testabilidade e flexibilidade para trocar tecnologias sem reescrever regras de negócio."*

2. **Mensageria Assíncrona e Resiliência:**
   > *"A comunicação entre os serviços não é acoplada via chamadas HTTP síncronas. O `transfer-service` publica eventos no Apache Kafka, permitindo que o `processing-service` processe as transferências de forma assídua, escalável e tolerante a falhas."*

3. **Engenharia de Software Moderna:**
   > *"Utilizei Java 17, Spring Boot, Bean Validation, tratamentos centralizados de exceções (`GlobalExceptionHandler`), testes unitários com JUnit 5 e Mockito, além de orquestração completa com Docker Compose."*
