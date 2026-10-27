# 🏛️ Arquitetura Hexagonal — Guia de Referência e Estudo do FinFlow

> **Material de Estudo Definitivo:** Este documento foi estruturado para ser utilizado lado a lado com o código-fonte do `transfer-service`. O objetivo é servir como um mapa conceitual e prático para você entender o papel de cada pacote, cada classe, as decisões de design e o fluxo de execução do sistema.

---

## 1. O que é Arquitetura Hexagonal?

Criada por **Alistair Cockburn**, a Arquitetura Hexagonal também é conhecida como **Ports & Adapters** (Portas e Adaptadores).

### O Problema que Resolve
Em aplicações tradicionais em camadas (Controller → Service → Repository), a lógica de negócio costuma ficar acoplada a frameworks e tecnologias externas (como anotações do Spring Data JPA, Hibernate, Jackson ou drivers de mensageria). Se você precisar trocar o banco de dados relacional por um NoSQL, ou alterar a forma de comunicação, acaba tendo que mexer nas regras de negócio. Além disso, testar a regra de negócio exige subir o contexto do framework inteiro.

### A Solução da Arquitetura Hexagonal
A divisão do sistema em zonas concêntricas orientadas por uma **regra de ouro**:

> **O Domínio (regras de negócio puras) é o centro absoluto da aplicação. Ele não conhece NADA do mundo externo (frameworks, banco de dados, mensageria, HTTP). O mundo externo é que se adapta ao Domínio através de Contratos (Ports).**

---

## 2. As 3 Zonas da Arquitetura


<img width="707" height="551" alt="Arquitetura" src="https://github.com/user-attachments/assets/88385d04-48d0-4a01-a7bb-cde455713b29" />


| Zona | Responsabilidade Principal | Depende de Frameworks? |
| :--- | :--- | :--- |
| **Domain** | Entidades de negócio, enums, exceções de negócio, interfaces de portas (in/out). | ❌ **NUNCA** (Java puro) |
| **Application** | Orquestração de casos de uso, implementação das portas de entrada. | ⚠️ Neutro / Pode usar `@Service` |
| **Adapter** | Implementações concretas de tecnologia (Controllers REST, JPA entities/repositories, Kafka producers). | ✅ **Sim** (Spring, JPA, Kafka, etc.) |

---

## 3. Estrutura Atual de Pacotes e Classes (`transfer-service`)

Abaixo está o mapeamento fiel da árvore de diretórios do projeto:

```text
src/main/java/com/finflow/transfer/
│
├── TransferServiceApplication.java          ← Ponto de entrada do Spring Boot
│
├── domain/                                  ← 🧠 NÚCLEO DA APLICAÇÃO (Regras Puras)
│   ├── model/
│   │   ├── Transfer.java                    ← POJO puro de domínio
│   │   └── Status.java                      ← Enum de estados da transferência
│   │
│   ├── port/
│   │   ├── in/
│   │   │   └── CreateTransferUseCase.java   ← Porta de Entrada (Caso de uso)
│   │   └── out/
│   │       ├── TransferPersistencePort.java  ← Porta de Saída (Persistência)
│   │       └── TransferEventPublisherPort.java ← Porta de Saída (Mensageria/Kafka)
│   │
│   └── exception/
│       ├── BusinessRuleException.java       ← Exceção para regras de negócio violadas
│       └── ResourceNotFoundException.java   ← Exceção para recursos não encontrados
│
├── application/                             ← ⚙️ CASOS DE USO E ORQUESTRAÇÃO
│   └── service/
│       └── TransferService.java             ← Implementa o CreateTransferUseCase
│
├── adapter/                                 ← 🔌 ADAPTADORES (Mundo Externo / Frameworks)
│   ├── in/
│   │   └── web/
│   │       ├── TransferController.java      ← Endpoint REST POST /transfers
│   │       ├── GlobalExceptionHandler.java  ← Tratamento global de exceções HTTP
│   │       ├── dto/
│   │       │   ├── TransferRequestDTO.java  ← DTO de entrada validados (Bean Validation)
│   │       │   └── TransferResponseDTO.java ← DTO de saída da API
│   │       └── mapper/
│   │           └── TransferWebMapper.java   ← Tradutor RequestDTO ↔ Domain ↔ ResponseDTO
│   │
│   └── out/
│       ├── persistence/
│       │   ├── TransferJpaAdapter.java      ← Implementa TransferPersistencePort (JPA)
│       │   ├── entity/
│       │   │   └── TransferJpaEntity.java   ← Entidade JPA (@Entity, @Table)
│       │   ├── mapper/
│       │   │   └── TransferPersistenceMapper.java ← Tradutor Domain ↔ JPA Entity
│       │   └── repository/
│       │       └── TransferJpaRepository.java ← Spring Data JPA Repository
│       │
│       └── messaging/
│           ├── KafkaTransferPublisherAdapter.java ← Implementa TransferEventPublisherPort (Kafka)
│           └── TransferCreatedEvent.java    ← DTO/Payload do evento enviado ao Kafka
│
└── config/                                  ← 🛠️ CONFIGURAÇÕES DE INFRAESTRUTURA
    └── BeanConfiguration.java               ← Configuração adicional de beans (se necessário)
```

---

## 4. Guia Detalhado: Pacotes, Classes e Responsabilidades

### 4.1 Pacote `domain/` (O Coração)
O domínio contém a essência do negócio. Nenhuma classe aqui possui anotações do Spring (`@Service`, `@Entity`, `@RestController`) ou bibliotecas de infraestrutura.

*   **`domain/model/Transfer.java`**:
    *   *O que faz:* Representa o objeto de negócio Transferência no seu estado mais puro.
    *   *Por que existe:* Contém os atributos fundamentais (`id`, `sourceAccount`, `destinationAccount`, `amount`, `status`, `createdAt`, `updatedAt`) e métodos de negócio, sem acoplamento com banco de dados.
*   **`domain/model/Status.java`**:
    *   *O que faz:* Enum que define os estados possíveis de uma transferência (`PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`).
*   **`domain/port/in/CreateTransferUseCase.java`**:
    *   *O que faz:* **Porta de Entrada (Input Port)**. É uma interface que define o contrato do caso de uso de criação de transferência.
    *   *Assinatura:* `Transfer createTransfer(Transfer transfer);`
    *   *Por que existe:* O Controller interage exclusivamente com esta interface, ignorando totalmente quem é o executor real.
*   **`domain/port/out/TransferPersistencePort.java`**:
    *   *O que faz:* **Porta de Saída (Output Port)** para persistência. Define o contrato de o que o domínio precisa para salvar ou buscar dados.
    *   *Assinatura:* `Transfer save(Transfer transfer);`
    *   *Por que existe:* O serviço de domínio chama essa interface quando precisa persistir dados, sem saber se por trás há PostgreSQL, MongoDB ou arquivo texto.
*   **`domain/port/out/TransferEventPublisherPort.java`**:
    *   *O que faz:* **Porta de Saída (Output Port)** para mensageria. Define o contrato para publicação de eventos de domínio.
    *   *Assinatura:* `void publishTransferCreated(Transfer transfer);`
    *   *Por que existe:* Permite que o domínio solicite a publicação de um evento (ex: "transferência criada") sem saber que a tecnologia utilizada é o Apache Kafka.
*   **`domain/exception/BusinessRuleException.java` & `ResourceNotFoundException.java`**:
    *   *O que faz:* Exceções customizadas de domínio para representar falhas de regras de negócio (ex: conta de origem igual à de destino) ou recursos ausentes.

---

### 4.2 Pacote `application/` (Orquestração)
*   **`application/service/TransferService.java`**:
    *   *O que faz:* Implementa a porta de entrada (`CreateTransferUseCase`). É o orquestrador do caso de uso.
    *   *Como funciona:*
        1. Valida regras de negócio puras (ex: origem ≠ destino).
        2. Chama a porta de saída de persistência (`TransferPersistencePort`) para salvar a transferência.
        3. Chama a porta de saída de mensageria (`TransferEventPublisherPort`) para notificar o sistema via Kafka.
    *   *Por que existe:* Centraliza o fluxo da transação de negócio sem se prender aos detalhes HTTP ou detalhes de driver de banco/kafka.

---

### 4.3 Pacote `adapter/in/web/` (Entrada REST)
Responsável por expor a aplicação via API HTTP REST.

*   **`TransferController.java`**:
    *   *O que faz:* `@RestController` mapeado em `/transfers`. Recebe requisições HTTP POST.
    *   *Relacionamento:* Injeta o `CreateTransferUseCase` (porta de entrada) e o `TransferWebMapper`. **Nunca** chama repositórios diretamente.
*   **`GlobalExceptionHandler.java`**:
    *   *O que faz:* `@RestControllerAdvice` que intercepta exceções lançadas pelo domínio/serviço (como `BusinessRuleException` e `ResourceNotFoundException`) e as traduz em respostas HTTP adequadas (`409 Conflict`, `404 Not Found`).
*   **`dto/TransferRequestDTO.java` & `TransferResponseDTO.java`**:
    *   *O que faz:* Objetos de transferência de dados (Records/Classes Java) usados na borda da API. Contêm anotações de validação (`@NotNull`, `@Positive`, etc.).
*   **`mapper/TransferWebMapper.java`**:
    *   *O que faz:* Traduz o `TransferRequestDTO` em objeto de Domínio (`Transfer`), e o objeto de Domínio em `TransferResponseDTO`.

---

### 4.4 Pacote `adapter/out/persistence/` (Saída - Banco de Dados)
Responsável pela persistência relacional utilizando Spring Data JPA.

*   **`entity/TransferJpaEntity.java`**:
    *   *O que faz:* A entidade JPA (`@Entity`, `@Table(name = "transfers")`). Contém todas as anotações de banco de dados. Fica estritamente no adapter, isolada do domínio.
*   **`repository/TransferJpaRepository.java`**:
    *   *O que faz:* Interface que estende `JpaRepository<TransferJpaEntity, Long>`, fornecendo métodos prontos de CRUD do Spring Data.
*   **`mapper/TransferPersistenceMapper.java`**:
    *   *O que faz:* Converte a entidade de domínio (`Transfer`) para entidade JPA (`TransferJpaEntity`) e vice-versa.
*   **`TransferJpaAdapter.java`**:
    *   *O que faz:* Implementa a porta de saída `TransferPersistencePort`. Utiliza internamente o `TransferJpaRepository` e o `TransferPersistenceMapper` para realizar as operações no banco.

---

### 4.5 Pacote `adapter/out/messaging/` (Saída - Mensageria Kafka)
Responsável pela integração assíncrona via Apache Kafka.

*   **`TransferCreatedEvent.java`**:
    *   *O que faz:* DTO/Payload que representa o evento publicado no broker de mensageria. Desacopla a estrutura da mensagem Kafka da entidade de domínio ou de banco.
*   **`KafkaTransferPublisherAdapter.java`**:
    *   *O que faz:* Implementa a porta de saída `TransferEventPublisherPort`. Utiliza o `KafkaTemplate<String, Object>` do Spring for Apache Kafka para enviar o `TransferCreatedEvent` para o tópico `finflow.transfer.requested`.

---

### 4.6 Pacote `config/`
*   **`BeanConfiguration.java`**:
    *   *O que faz:* Local para configurações adicionais de Beans Spring (caso necessário para injeções específicas ou configuração de componentes customizados).

---

## 5. A Regra de Ouro das Dependências

As setas de dependência na arquitetura hexagonal **sempre apontam para o centro (Domínio)**.

```
adapter/in/web  ──────►  domain/port/in  ◄──────  application/service
                                                          │
                                                          ▼
                         domain/port/out  ◄──────  adapter/out/persistence
                         domain/port/out  ◄──────  adapter/out/messaging
```

### O que é permitido vs. Proibido:
*   ✅ **Controller** conhece o **Port de Entrada** (UseCase).
*   ✅ **Service** implementa o **Port de Entrada** e injeta os **Ports de Saída**.
*   ✅ **Adapters** (Web, Persistence, Messaging) dependem das interfaces do **Domínio**.
*   ❌ **Domínio** **NUNCA** importa classes de `adapter` ou `application`.
*   ❌ **Domínio** **NUNCA** possui anotações do Spring (`@Entity`, `@Service`, `@Autowired`).
*   ❌ **Service** **NUNCA** importa bibliotecas de JPA (`JpaRepository`) ou Kafka (`KafkaTemplate`).

---

## 6. Fluxo de Execução: O Ciclo de uma Transferência

Para entender o código na prática, acompanhe o passo a passo de uma requisição de criação de transferência:

1.  **Requisição HTTP:** O cliente envia um `POST /transfers` com um JSON contendo `sourceAccount`, `destinationAccount` e `amount`.
2.  **Validação na Borda:** O Spring valida o `TransferRequestDTO` usando Bean Validation (`jakarta.validation`). Se inválido, retorna `400 Bad Request`.
3.  **Controller & Mapper:** O `TransferController` recebe o DTO válido e chama o `TransferWebMapper` para converter o DTO em um objeto de Domínio (`Transfer`).
4.  **Caso de Uso (Port In):** O Controller chama o método `createTransfer(Transfer)` na interface `CreateTransferUseCase` (implementada pelo `TransferService`).
5.  **Regras de Negócio:** O `TransferService` executa as validações de negócio (ex: `validarRegrasTransferencia` verifica se a conta origem é diferente da destino). Se falhar, lança `BusinessRuleException`.
6.  **Persistência (Port Out):** O `TransferService` chama `TransferPersistencePort.save(transfer)`.
    *   O `TransferJpaAdapter` recebe o pedido, converte para `TransferJpaEntity` via `TransferPersistenceMapper`.
    *   Salva no PostgreSQL usando o `TransferJpaRepository`.
    *   Converte a entidade salva de volta para o modelo de domínio `Transfer`.
7.  **Mensageria (Port Out):** O `TransferService` chama `TransferEventPublisherPort.publishTransferCreated(savedTransfer)`.
    *   O `KafkaTransferPublisherAdapter` converte o domínio em `TransferCreatedEvent`.
    *   Envia a mensagem de forma assíncrona para o tópico Kafka `finflow.transfer.requested` usando o `KafkaTemplate`.
8.  **Resposta HTTP:** O `TransferService` retorna o objeto `Transfer` criado para o Controller. O Controller o converte em `TransferResponseDTO` via mapper e retorna `201 Created` com os dados da transferência.
9.  **Tratamento de Exceções:** Caso ocorra qualquer exceção de negócio (`BusinessRuleException`) ou recurso não encontrado (`ResourceNotFoundException`) em qualquer camada, o `GlobalExceptionHandler` intercepta e retorna o status HTTP adequado (`409` ou `404`).

---

## 7. Como Estudar Usando este Documento

1.  **Abra o documento de um lado e a IDE (com o código) do outro.**
2.  **Navegue pacote por pacote** listados na Seção 3 e Seção 4.
3.  **Verifique a ausência de acoplamento:** Abra `domain/model/Transfer.java` e certifique-se de que não há nenhuma anotação de banco de dados ou framework.
4.  **Inspecione os contratos:** Olhe `CreateTransferUseCase.java` e perceba como ele é uma interface pura.
5.  **Rastreie o fluxo:** Siga a execução desde o `TransferController` até o `TransferService`, passando pelo `TransferJpaAdapter` e `KafkaTransferPublisherAdapter`.

---
*Documento atualizado para refletir a arquitetura atual do FinFlow contendo Persistência JPA e Mensageria Kafka.*
