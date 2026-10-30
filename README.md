
## 📋 Sobre o Projeto

# FinFlow — Ecossistema de Transferências Financeiras

> **Projeto Concluído com Sucesso!** 🎉
> O ecossistema **FinFlow** é composto por dois microsserviços principais organizados em branches dedicadas:
> - **`transfer-service`**: Microsserviço de origem responsável por receber e publicar eventos de transferências.
> - **`processing-service`**: Microsserviço de processamento assíncrono (consumo via Apache Kafka), validação de regras de negócio e persistência com Arquitetura Hexagonal.


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

## 📁 Estrutura do Repositório (Monorepo)

```text
finflow/ (transfer-service)
├── transfer-service/          # Microsserviço 1: Entrada REST & Produtor Kafka
│   ├── src/main/java/com/finflow/transfer/
│   │   ├── adapter/           # Adaptadores REST e JPA/Kafka Publisher
│   │   ├── application/       # Serviços de aplicação / Casos de uso
│   │   └── domain/            # Domínio, Modelos, Portas e Exceções
│   └── pom.xml
├── processing-service/        # Microsserviço 2: Consumidor Kafka & Processamento
│   ├── src/main/java/com/finflow/processing/
│   │   ├── adapter/           # Consumidor Kafka e Persistência JPA
│   │   ├── application/       # Casos de uso de processamento
│   │   └── domain/            # Domínio, Portas e Status
│   └── pom.xml
├── .claude/                   # Agentes e configuração de IA
├── docs/                      # Documentação de arquitetura e roadmap
└── README.md
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
- [x] **Etapa 5:** Criação e estruturação do `processing-service` (Arquitetura Hexagonal, Configuração Kafka/JPA).
- [x] **Etapa 6:** Consumo de eventos via Kafka `@KafkaListener` no `processing-service` e atualização de status.
- [x] **Etapa 7:** Documentação da API (OpenAPI/Swagger) e pipeline de CI/CD (GitHub Actions).

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
