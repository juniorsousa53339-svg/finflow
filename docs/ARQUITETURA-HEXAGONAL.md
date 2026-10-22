# 🏛️ Arquitetura Hexagonal — Guia de Referência do FinFlow

> Documento criado para estudo e revisão. Explica cada camada, cada pacote e o passo a passo de implementação.

---

## 1. O que é Arquitetura Hexagonal?

Criada por **Alistair Cockburn**, também chamada de **Ports & Adapters**.

**Problema que resolve:** Em projetos tradicionais, a lógica de negócio fica misturada com frameworks (JPA, Spring MVC, Kafka). Se você trocar o banco de dados, precisa reescrever regras de negócio. Se quiser testar uma regra, precisa subir o Spring inteiro.

**Solução:** Separar o sistema em 3 zonas com uma regra simples:

> **O domínio (regras de negócio) não conhece NADA do mundo externo. O mundo externo é que se adapta ao domínio.**

---

## 2. As 3 Zonas

```
┌──────────────────────────────────────────────────────┐
│               ADAPTERS (Mundo Externo)                │
│                                                       │
│   ┌────────────┐                  ┌────────────────┐  │
│   │ Controller │                  │ JPA Repository │  │
│   │   (REST)   │                  │  (Banco)       │  │
│   └─────┬──────┘                  └───────┬────────┘  │
│         │                                 │           │
│   ┌─────▼─────────────────────────────────▼────────┐  │
│   │               PORTS (Contratos)                │  │
│   │                                                │  │
│   │   Port IN (interface)      Port OUT (interface) │  │
│   │   "o que posso pedir"      "o que preciso       │  │
│   │                             de fora"             │  │
│   └─────┬──────────────────────────────────┬───────┘  │
│         │                                  │          │
│         │       ┌──────────────────┐       │          │
│         └──────►│     DOMAIN       │◄──────┘          │
│                 │  (Regras Puras)  │                  │
│                 └──────────────────┘                  │
└──────────────────────────────────────────────────────┘
```

| Zona | Responsabilidade | Depende de framework? |
|------|------------------|-----------------------|
| **Domain** | Regras de negócio, entidades, enums | ❌ NUNCA |
| **Port** | Contratos (interfaces) entre domínio e mundo externo | ❌ NUNCA |
| **Adapter** | Implementações concretas (REST, JPA, Kafka...) | ✅ Sim |

---

## 3. Estrutura de Pacotes do FinFlow

```
com.finflow.transfer/
│
├── domain/                              ← 🧠 NÚCLEO
│   ├── model/
│   │   ├── Transfer.java                ← POJO puro (sem @Entity!)
│   │   └── Status.java                  ← Enum de domínio
│   ├── port/
│   │   ├── in/
│   │   │   └── CreateTransferUseCase.java   ← Interface de entrada
│   │   └── out/
│   │       └── TransferPersistencePort.java  ← Interface de saída
│   └── exception/
│       ├── BusinessRuleException.java
│       └── ResourceNotFoundException.java
│
├── application/                         ← ⚙️ ORQUESTRAÇÃO
│   └── service/
│       └── TransferService.java         ← Implementa ports IN, usa ports OUT
│
├── adapter/                             ← 🔌 MUNDO EXTERNO
│   ├── in/
│   │   └── web/
│   │       ├── TransferController.java
│   │       ├── dto/
│   │       │   ├── TransferRequestDTO.java
│   │       │   └── TransferResponseDTO.java
│   │       └── mapper/
│   │           └── TransferWebMapper.java
│   └── out/
│       └── persistence/
│           ├── TransferJpaAdapter.java       ← Implementa port OUT
│           ├── entity/
│           │   └── TransferJpaEntity.java    ← @Entity do JPA
│           ├── mapper/
│           │   └── TransferPersistenceMapper.java
│           └── repository/
│               └── TransferJpaRepository.java  ← JpaRepository
│
└── config/
    └── BeanConfiguration.java           ← Cola tudo com @Bean
```

---

## 4. Explicação de cada pacote

### 4.1 `domain/model/` — O Coração

Classes que representam **o que o sistema É**, sem nenhuma dependência de framework.

- **`Transfer.java`** — Um POJO puro com os atributos: id, sourceAccount, destinationAccount, amount, status, createdAt, updatedAt.
- **`Status.java`** — Enum com os estados: PENDING, PROCESSING, COMPLETED, FAILED.

**Regra de ouro:** Se você deletar o Spring Boot, o Maven, tudo — esse pacote continua compilando com `javac` puro.

### 4.2 `domain/port/in/` — Portas de Entrada

Interfaces que definem **o que o mundo externo pode pedir ao domínio**.

```java
public interface CreateTransferUseCase {
    Transfer createTransfer(Transfer transfer);
}
```

O Controller chama essa interface. Ele não sabe (e não precisa saber) quem implementa.

**Analogia:** O cardápio do restaurante. O cliente faz o pedido sem saber quem é o cozinheiro.

### 4.3 `domain/port/out/` — Portas de Saída

Interfaces que definem **o que o domínio precisa do mundo externo**.

```java
public interface TransferPersistencePort {
    Transfer save(Transfer transfer);
    Optional<Transfer> findById(Long id);
    List<Transfer> findAll();
}
```

O Service usa essa interface. Não sabe se por trás é JPA, MongoDB, ou um arquivo de texto.

**Analogia:** A porta USB-C do celular. O celular não sabe se está na tomada ou no carro.

### 4.4 `domain/exception/` — Exceções de Negócio

Exceções que representam erros do **negócio**, não do framework:
- `BusinessRuleException` — conta de origem igual à de destino, por exemplo.
- `ResourceNotFoundException` — transferência não encontrada.

### 4.5 `application/service/` — O Orquestrador

```java
public class TransferService implements CreateTransferUseCase {

    private final TransferPersistencePort persistencePort;

    // Recebe o port de saída por construtor (injeção de dependência)
    // Implementa o port de entrada
    // Contém regras de orquestração
}
```

**Analogia:** O gerente do restaurante. Recebe o pedido (port in), coordena o preparo, e manda entregar (port out).

### 4.6 `adapter/in/web/` — Adapter REST (Entrada)

Recebe requisições HTTP e **traduz** para o domínio:
- **Controller** — `@RestController`, recebe DTOs, chama o use case
- **DTOs** — `TransferRequestDTO`, `TransferResponseDTO` (records Java)
- **Mapper** — Converte DTO → modelo de domínio e vice-versa

**Regra:** O Controller NUNCA chama o repository diretamente. Ele só conhece o port de entrada.

### 4.7 `adapter/out/persistence/` — Adapter JPA (Saída)

Implementa o port de saída usando JPA:
- **`TransferJpaEntity.java`** — A classe com `@Entity`, `@Table` (anotações JPA ficam AQUI, não no domínio!)
- **`TransferJpaRepository.java`** — O `JpaRepository<TransferJpaEntity, Long>` do Spring Data
- **`TransferPersistenceMapper.java`** — Converte JPA Entity ↔ modelo de domínio
- **`TransferJpaAdapter.java`** — Implementa `TransferPersistencePort`, usa o repository internamente

### 4.8 `config/` — Configuração Spring

O `TransferService` não tem `@Service` (pra não depender do Spring). Então usamos um `@Configuration` com `@Bean` para dizer ao Spring: "quando alguém pedir um `CreateTransferUseCase`, use o `TransferService`".

```java
@Configuration
public class BeanConfiguration {

    @Bean
    public CreateTransferUseCase createTransferUseCase(TransferPersistencePort port) {
        return new TransferService(port);
    }
}
```

---

## 5. Regra das Dependências (a mais importante!)

As setas de dependência SEMPRE apontam para o domínio. O domínio NUNCA aponta para fora.

```
adapter/in/web  ───►  domain (ports/in)  ◄───  application/service
                                                      │
                                                      ▼
                       domain (ports/out)  ◄───  adapter/out/persistence
```

- ✅ Controller conhece o Port de entrada → OK
- ✅ Service implementa o Port de entrada → OK
- ✅ Service usa o Port de saída → OK (usa a interface, não a implementação)
- ✅ JpaAdapter implementa o Port de saída → OK
- ❌ Service importa JpaRepository → PROIBIDO
- ❌ Domain importa Spring/JPA → PROIBIDO

---

## 6. Passo a passo da implementação

### Fase 1 — Domínio (criar primeiro, é independente)
- [ ] `domain/model/Status.java` — copiar enum, mudar package
- [ ] `domain/model/Transfer.java` — POJO puro (sem @Entity), com Getters/Setters
- [ ] `domain/exception/BusinessRuleException.java` — copiar, mudar package
- [ ] `domain/exception/ResourceNotFoundException.java` — copiar, mudar package

### Fase 2 — Ports (interfaces, sem implementação)
- [ ] `domain/port/in/CreateTransferUseCase.java` — interface com método createTransfer()
- [ ] `domain/port/out/TransferPersistencePort.java` — interface com save(), findById(), findAll()

### Fase 3 — Application (implementa os use cases)
- [ ] `application/service/TransferService.java` — implementa CreateTransferUseCase, usa TransferPersistencePort

### Fase 4 — Adapters de Saída (persistência)
- [ ] `adapter/out/persistence/entity/TransferJpaEntity.java` — @Entity com anotações JPA
- [ ] `adapter/out/persistence/repository/TransferJpaRepository.java` — JpaRepository
- [ ] `adapter/out/persistence/mapper/TransferPersistenceMapper.java` — Entity ↔ Domain
- [ ] `adapter/out/persistence/TransferJpaAdapter.java` — implementa TransferPersistencePort

### Fase 5 — Adapters de Entrada (web/REST)
- [ ] `adapter/in/web/dto/TransferRequestDTO.java` — copiar/mover, mudar package
- [ ] `adapter/in/web/dto/TransferResponseDTO.java` — copiar/mover, mudar package
- [ ] `adapter/in/web/mapper/TransferWebMapper.java` — DTO ↔ Domain
- [ ] `adapter/in/web/TransferController.java` — @RestController

### Fase 6 — Configuração
- [ ] `config/BeanConfiguration.java` — @Configuration com @Bean

### Fase 7 — Limpeza
- [ ] Deletar pacotes antigos (enums/, dto/, repository/, domain/Transfer.java antigo)
- [ ] Deletar arquivos .gitkeep
- [ ] Rodar testes: `.\mvnw.cmd test`

---

## 7. Benefícios para mencionar em entrevistas

1. **Testabilidade** — Testo regras de negócio sem subir Spring, sem banco, sem nada.
2. **Independência de framework** — Posso trocar JPA por MongoDB mudando só o adapter de saída.
3. **Clareza** — Qualquer dev novo entende a estrutura em 5 minutos.
4. **Manutenibilidade** — Mudanças ficam isoladas na camada certa.
5. **Evolução** — Adicionar Kafka? É só criar um novo adapter de saída.

---

## 8. Perguntas comuns em entrevistas

**P: Qual a diferença entre Arquitetura Hexagonal e Clean Architecture?**
R: São muito parecidas. A Hexagonal foca em Ports & Adapters. A Clean Architecture (Uncle Bob) adiciona mais camadas e a "Regra da Dependência". Na prática, a ideia central é a mesma: proteger o domínio.

**P: Quando NÃO usar Hexagonal?**
R: Em projetos muito pequenos (um CRUD simples), pode ser over-engineering. O custo de manter tantos pacotes e mappers pode não compensar. Use quando o domínio tem regras complexas ou quando precisa de flexibilidade para trocar tecnologias.

**P: O TransferService pode ter @Service?**
R: Pode, mas aí ele depende do Spring. Na hexagonal "pura", usamos @Bean no config. Em projetos reais, muitos times usam @Service por praticidade — e tudo bem. O importante é entender o trade-off.
