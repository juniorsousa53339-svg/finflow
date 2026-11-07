# 🧪 Guia Passo a Passo: Testando o Ecossistema FinFlow para Recrutadores

Este documento apresenta um roteiro detalhado e profissional para demonstrar e testar o funcionamento completo do ecossistema **FinFlow**, integrando o **`transfer-service`** (produtor REST e Swagger) e o **`processing-service`** (consumidor assíncrono via Apache Kafka e Swagger na porta 8081) utilizando **Arquitetura Hexagonal**.

---

## 🏗️ 1. Arquitetura do Fluxo End-to-End (E2E)

```text
[ Swagger UI transfer-service ] (Porta 8080)
       │
       ▼  HTTP POST /transfers
[ transfer-service ] 
       │
       ▼  Publica Evento (Async)
[ Apache Kafka ] ── Tópico: transfer-requested
       │
       ▼  Consumo via @KafkaListener
[ processing-service ] (Porta 8081)
       │
       ├─► Processamento de Negócio (Status -> COMPLETED)
       ▼
[ Swagger UI processing-service ] (Porta 8081 - GET /transfers)
```

---

## 🛠️ 2. Pré-Requisitos

Certifique-se de ter instalado em sua máquina:
- **Java 17 (JDK Temurin)**
- **Maven** (ou use o Maven Wrapper `mvnw` incluso)
- **Docker & Docker Compose** (para rodar o PostgreSQL e o Apache Kafka)
- **PowerShell** (Windows) ou **Bash** (Linux/Mac)
- **Navegador Web** (para acessar as interfaces Swagger UI)

---

## 🚀 3. Roteiro de Demonstração Passo a Passo com Swagger

### Passo 3.1: Subir a Infraestrutura (Zookeeper e Kafka)
Abra o terminal na pasta do **`transfer-service`** onde se encontra o arquivo `docker-compose.yml` e execute:

```powershell
docker compose up -d
```

*Como verificar:*
Execute `docker ps` para garantir que os containers do Kafka e Zookeeper estão ativos.

---

### Passo 3.2: Iniciar o `transfer-service` (Porta 8080)
No terminal localizado em `transfer-service`, inicie a aplicação:

```powershell
& .\mvnw.cmd spring-boot:run
```
*(A aplicação subirá na porta **`8080`**).*

---

### Passo 3.3: Iniciar o `processing-service` (Porta 8081)
Abra um **novo terminal** na pasta do `processing-service` e inicie o serviço:

```powershell
cd "C:\Users\Luciano Junior\Downloads\processing-service"
& .\mvnw.cmd spring-boot:run
```
*(A aplicação subirá na porta **`8081`**, conectando-se ao broker Kafka e disponibilizando o Swagger).*

---

### Passo 3.4: Demonstrar o `processing-service` Vazio pelo Swagger
1. Abra o navegador e acesse o Swagger do `processing-service`:
   👉 **`http://localhost:8081/swagger-ui/index.html`**
2. Expanda o endpoint `GET /transfers` e clique em **Try it out** -> **Execute**.
3. Mostre ao recrutador que a lista está vazia (`[]`), provando que nenhuma transferência foi processada ainda.

---

### Passo 3.5: Fazer o POST no Swagger do `transfer-service`
1. Abra uma nova aba no navegador e acesse o Swagger do `transfer-service`:
   👉 **`http://localhost:8080/swagger-ui/index.html`**
2. Encontre o endpoint `POST /transfers`, clique em **Try it out** e envie o JSON de transferência:
   ```json
   {
     "sourceAccount": 100,
     "destinationAccount": 200,
     "amount": 350.75
   }
   ```
3. Clique em **Execute** (Status `201 Created` retornado).

---

### Passo 3.6: Comprovar a Chegada e o Processamento no `processing-service`
1. Volte para a aba do Swagger do `processing-service` (`http://localhost:8081/swagger-ui/index.html`).
2. Clique novamente em **Execute** no endpoint `GET /transfers`.
3. **Resultado:** A transferência aparecerá instantaneamente na lista com o status atualizado para **`COMPLETED`**, demonstrando o fluxo assíncrono de ponta a ponta via Apache Kafka!

---

## 🎯 4. Argumentos Chave para Destacar com o Recrutador

1. **Arquitetura Hexagonal (Ports & Adapters):**
   > *"O núcleo de domínio é 100% puro e isolado de frameworks, garantindo alta testabilidade e flexibilidade."*

2. **Mensageria Assíncrona e Resiliência (Kafka):**
   > *"A comunicação é desacoplada. O `transfer-service` publica o evento no Kafka e o `processing-service` consome e processa de forma assíncrona."*

3. **Documentação Integrada via Swagger UI:**
   > *"Ambos os microsserviços possuem documentação OpenAPI/Swagger interativa (porta 8080 para origem e porta 8081 para processamento), facilitando a demonstração E2E em tempo real."*
