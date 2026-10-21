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
- Modelagem de dados relacional com **Spring Data JPA** e **PostgreSQL**.
- Arquitetura orientada a microsserviços com comunicação assíncrona usando **Apache Kafka**.
- Garantia de consistência, transacionabilidade e **idempotência**.
- Testes automatizados rigorosos com **JUnit 5** e **Mockito** (unitários e de integração).
- Containerização e orquestração com **Docker** e **Docker Compose**.
- Automação de CI/CD com **GitHub Actions**.

---

## 🏗️ Arquitetura Planejada

O sistema é construído de forma incremental e será dividido em dois microsserviços principais:

1. **`transfer-service`** (Microsserviço atual):
   - Responsável por receber requisições de transferência.
   - Validação de regras de negócio de entrada, dados de contas e saldo (futuro).
   - Persistência inicial dos registros de transferência.
2. **`processing-service`** (Microsserviço futuro):
   - Responsável pelo processamento assíncrono das transferências.
   - Consumo de eventos via mensageria (Kafka).
   - Implementação de filas de retry, tratamento de falhas e políticas de resiliência.

---

## 🛠️ Stack Tecnológica

### Tecnologias Utilizadas (Confirmadas no projeto)
- **Java 17**
- **Spring Boot** (v4.1.1)
  - Spring WebMVC
  - Spring Data JPA
  - Spring for Apache Kafka
  - Spring Boot Validation
  - Spring Boot DevTools
- **Maven** (com Maven Wrapper)
- **Project Lombok**
- **JUnit 5 & Mockito** (via Spring Boot Test)

### Tecnologias Planejadas / Em Configuração
- **Banco de Dados:** PostgreSQL
- **Mensageria:** Apache Kafka (com Docker Compose)
- **Documentação de API:** OpenAPI / Swagger UI
- **Containerização:** Docker & Docker Compose
- **Pipeline CI/CD:** GitHub Actions

---

## 📁 Estrutura do Repositório

```text
transfer-service/
├── .mvn/                  # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/finflow/transfer/
│   │   │   └── TransferServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/finflow/transfer/
│           └── TransferServiceApplicationTests.java
├── CLAUDE.md              # Diretrizes de desenvolvimento e contexto para IA
├── README.md              # Documentação do projeto
├── pom.xml                # Gerenciamento de dependências e build
└── mvnw / mvnw.cmd        # Scripts de execução do Maven
```

---

## 🚀 Como Executar o Projeto (Desenvolvimento)

### Pré-requisitos
- **Java JDK 17** instalado e configurado nas variáveis de ambiente.
- Maven (ou utilize o Maven Wrapper incluído `mvnw`).

### 1. Clonar o Repositório
```bash
git clone https://github.com/juniorsousa53339-svg/finflow.git
cd transfer-service
```

### 2. Executar os Testes
```bash
# No Windows (PowerShell)
& .\mvnw.cmd test

# No Linux / macOS
./mvnw test
```

---

## 🗺️ Roadmap de Evolução

- [x] **Etapa 1:** Fundação, configuração inicial do projeto Spring Boot e estruturação do ambiente.
- [ ] **Etapa 2:** Desenvolvimento do `transfer-service` (Domínio, DTOs, Controllers REST, Validações e Persistência H2/PostgreSQL).
- [ ] **Etapa 3:** Camada de banco de dados relacional (PostgreSQL), transações e tratamento robusto de erros.
- [ ] **Etapa 4:** Criação do `processing-service` e definição de contratos entre serviços.
- [ ] **Etapa 5:** Implementação de mensageria com Apache Kafka para processamento assíncrono.
- [ ] **Etapa 6:** Infraestrutura com Docker Compose, documentação OpenAPI e CI/CD com GitHub Actions.
- [ ] **Etapa 7:** Revisão geral de código, segurança, testes de regressão e preparação para apresentação técnica.

---

## 👨‍💻 Autoria e Mentoria

Projeto desenvolvido por **Luciano Junior** (Estudante de Análise e Desenvolvimento de Sistemas) em parceria técnica com assistente de IA especializado em mentoria de desenvolvimento backend em Java e Spring Boot.

---

## 📄 Licença

Este projeto é distribuído sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.
