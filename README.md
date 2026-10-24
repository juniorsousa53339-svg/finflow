# FinFlow 💸

> Sistema simulador de transferências financeiras desenvolvido com foco educacional e arquitetural.

---

## 📋 Sobre o Projeto

O **FinFlow** é um projeto de estudo e portfólio criado para simular o fluxo de transferências financeiras entre contas de forma segura, resiliente e escalável. O objetivo principal é proporcionar vivência prática em engenharia de software backend, aplicando conceitos avançados de arquitetura de microsserviços, mensageria assíncrona, transações, idempotência e testes automatizados.

*⚠️ **Aviso:** O FinFlow é um projeto puramente educacional. Nenhuma movimentação financeira real é realizada.*

---

## 🎯 Objetivos de Aprendizado

- Domínio de **Java 17** e **Spring Boot** em aplicações corporativas.
- Construção de **APIs REST** robustas com validação e tratamento de exceções.
- Modelagem de dados relacional com **Spring Data JPA** e **PostgreSQL** (além de H2 para testes).
- Arquitetura orientada a microsserviços com comunicação assíncrona usando **Apache Kafka**.
- Garantia de consistência, transacionabilidade e **idempotência**.
- Testes automatizados rigorosos com **JUnit 5** e **Mockito** (unitários e de integração).
- Containerização e orquestração com **Docker** e **Docker Compose**.
- Automação de CI/CD com **GitHub Actions**.

---

## 🏗️ Arquitetura e Estado Atual

O sistema é construído de forma incremental e dividido em dois microsserviços principais:

1. **`transfer-service`** (Microsserviço atual — **Implementado & Validado ✅**):
   - Recebimento de solicitações de transferência via API REST (`POST /transfers`).
   - Validação de entradas (Bean Validation) e regras de negócio no Domínio.
   - Persistência de dados utilizando Spring Data JPA (suporte a H2 e PostgreSQL).
   - Publicação assíncrona de eventos de transferência criada (`finflow.transfer.requested`) via **Apache Kafka**.
   - Tratamento centralizado de exceções (`GlobalExceptionHandler`).
   - Testes unitários e de contexto automatizados com JUnit 5 e Mockito.

2. **`processing-service`** (Microsserviço futuro — **Próximo Passo 🚀**):
   - Processamento assíncrono das transferências recebidas.
   - Consumo de eventos via `@KafkaListener`.
   - Atualização de status da transferência, regras de antifraude/saldo e políticas de resiliência (Retry / DLQ).

---

## 🛠️ Stack Tecnológica

- **Linguagem & Framework:** Java 17, Spring Boot (v4.1.1)
  - Spring WebMVC & Bean Validation
  - Spring Data JPA
  - Spring for Apache Kafka
  - Spring Boot DevTools
- **Banco de Dados:** PostgreSQL (Docker) & H2 Database (Testes)
- **Mensageria:** Apache Kafka & Zookeeper (via Docker Compose)
- **Build & Ferramentas:** Maven (Maven Wrapper), Project Lombok
- **Testes:** JUnit 5, Mockito, Spring Boot Test

---

## 📁 Estrutura Atual do Repositório (`transfer-service`)

```text
transfer-service/
├── .mvn/                  # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/finflow/transfer/
│   │   │   ├── adapter/in/web/       # Controllers e DTOs REST
│   │   │   ├── adapter/out/          # Adaptadores JPA e Kafka (Publisher)
│   │   │   ├── application/service/  # Casos de uso e serviços de aplicação
│   │   │   ├── domain/               # Modelos de domínio, exceções e portas
│   │   │   └── TransferServiceApplication.java
│   │   └── resources/
│   │       ├── application.properties         # Configuração H2 (Desenvolvimento/Testes)
│   │       └── application-docker.properties  # Configuração PostgreSQL & Docker
│   └── test/
│       └── java/com/finflow/transfer/         # Testes automatizados (JUnit 5 / Mockito)
├── docker-compose.yml     # Orquestração Docker (Postgres, Kafka, Zookeeper)
├── CLAUDE.md              # Diretrizes de desenvolvimento e contexto para IA
├── README.md              # Documentação do projeto
├── pom.xml                # Gerenciamento de dependências e build
└── mvnw / mvnw.cmd        # Scripts de execução do Maven
```

---

## 🚀 Como Executar o Projeto

### 1. Subir a Infraestrutura (Docker Compose)
Na raiz do projeto, suba o PostgreSQL e o Kafka:
```powershell
docker compose up -d
```

### 2. Executar os Testes Automatizados
```powershell
& .\mvnw.cmd test
```

### 3. Rodar a Aplicação
```powershell
# Com H2 em memória (padrão)
& .\mvnw.cmd spring-boot:run

# Com perfil Docker (conectado ao PostgreSQL)
& .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=docker"
```

---

## 🗺️ Roadmap de Evolução

- [x] **Etapa 1:** Configuração inicial do projeto Spring Boot e arquitetura base.
- [x] **Etapa 2:** Implementação do `transfer-service` (Domínio, Portas, Casos de Uso, Adapters REST e Bean Validation).
- [x] **Etapa 3:** Camada de persistência relacional (JPA, H2 e PostgreSQL) e Global Exception Handler.
- [x] **Etapa 4:** Integração com mensageria assíncrona (Apache Kafka Producer).
- [ ] **Etapa 5:** Criação do `processing-service` (Microsserviço de processamento).
- [ ] **Etapa 6:** Consumo de eventos via Kafka `@KafkaListener` no `processing-service` e atualização de status.
- [ ] **Etapa 7:** Documentação da API (OpenAPI/Swagger) e pipeline de CI/CD (GitHub Actions).

---

## 🧭 Próximos Passos para Conclusão
1. **Desenvolvimento do `processing-service`:** Criar o segundo microsserviço Spring Boot dedicado ao consumo assíncrono.
2. **Consumo via Kafka:** Implementar o listener no novo serviço para escutar o tópico `finflow.transfer.requested`.
3. **Evolução de Status:** Atualizar o ciclo de vida da transferência (PENDING -> PROCESSING -> COMPLETED/FAILED).

---

## 🤖 Engenharia Assistida por IA & Arquitetura de Agentes

O **FinFlow** foi concebido utilizando práticas avançadas de **Desenvolvimento Orientado por IA**:
- **Human-in-the-Loop (Tech Lead):** O desenvolvedor (Luciano Junior) atua como o arquiteto principal, tomando decisões técnicas fundamentadas, enquanto a IA atua como mentora sênior.
- **Agentes Especializados:** Uso de agentes customizados para orientação arquitetural (`finflow-mentor`) e qualidade de testes (`finflow-qa`).

---

## 👨‍💻 Autoria e Mentoria

Projeto desenvolvido por **Luciano Junior** (Estudante de Análise e Desenvolvimento de Sistemas) em parceria técnica com assistente de IA especializado em mentoria de desenvolvimento backend em Java e Spring Boot.

---

## 📄 Licença

Este projecto é distribuído sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.
