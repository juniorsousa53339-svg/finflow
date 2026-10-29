# FinFlow — Ecossistema de Transferências Financeiras

> **Projeto Concluído com Sucesso!** 🎉
> O ecossistema **FinFlow** é composto por dois microsserviços principais organizados em branches dedicadas:
> - **`transfer-service`**: Microsserviço de origem responsável por receber e publicar eventos de transferências.
> - **`processing-service`**: Microsserviço de processamento assíncrono (consumo via Apache Kafka), validação de regras de negócio e persistência com Arquitetura Hexagonal.

---

# Processing Service — FinFlow

> Microsserviço de processamento assíncrono de transferências financeiras do ecossistema **FinFlow**, construído com base nos princípios da **Arquitetura Hexagonal (Ports & Adapters)** e mensageria com **Apache Kafka**.

---

## 🛠️ Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 4.x** (ou 3.x/4.x compatível)
- **Spring for Apache Kafka** (Consumo assíncrono de eventos)
- **Spring Data JPA & PostgreSQL / H2** (Persistência de dados)
- **JUnit 5 & Mockito** (Testes unitários isolados)
- **Docker & Docker Compose** (Infraestrutura isolada)
- **GitHub Actions** (Pipeline de Integração Contínua / CI)

---

## 🏛️ Arquitetura Hexagonal
O projeto segue estritamente a separação de responsabilidades da Arquitetura Hexagonal:

```text
src/main/java/com/finflow/processing/
├── domain/                  # Núcleo de Negócio Puro (Entidades, Enums e Portas)
│   ├── model/               # Transfer, Status
│   └── port/                # TransferUseCase (In) e TransferRepositoryPort (Out)
├── application/             # Casos de Uso (Orquestração de Regras de Negócio)
│   └── service/             # TransferProcessService
└── adapter/                 # Adaptadores de Entrada e Saída
    ├── in/kafka/            # Consumidor Kafka (TransferKafkaConsumer)
    └── out/persistence/     # Repositório JPA e Adaptador de Banco de Dados
```

---

## 🧪 Testes Automatizados
Os testes unitários focam em validar as regras de negócio de forma isolada e rápida, utilizando **JUnit 5** e **Mockito** para simular o comportamento das portas de persistência.

Para rodar os testes localmente:
```bash
mvn test
```

---

## 🤖 Integração Contínua (CI/CD)
O repositório possui uma pipeline configurada via **GitHub Actions** (`.github/workflows/ci.yml`) que executa automaticamente todos os testes a cada `push` ou `pull_request` na branch `processing-service`.

---

## 🧠 Desenvolvimento Auxiliado por IA (AI Pair Programming)
Este microsserviço foi construído através de **Programação em Par com Inteligência Artificial**, utilizando o **Claude Code** da Anthropic e agentes especializados integrados ao ambiente de desenvolvimento (`FinFlow Mentor` e `FinFlow QA`). A IA atuou como orientadora técnica em tempo real, auxiliando na aplicação rigorosa da Arquitetura Hexagonal, na estruturação de testes unitários com JUnit 5 e Mockito, e nas boas práticas de engenharia de software.

---

## 👨‍💻 Autor
Desenvolvido por **Luciano Junior** em parceria com os agentes de mentoria e IA do FinFlow.
