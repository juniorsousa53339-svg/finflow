# Roadmap de Aprendizado e Desenvolvimento — FinFlow

Este documento detalha o plano de estudos e as etapas práticas do projeto **FinFlow**, alinhado ao objetivo de capacitação técnica para oportunidades de estágio e posições juniores.

---

## 📅 Etapas de Desenvolvimento

### Etapa 1 — Fundação e Configuração do Ambiente *(Atual)*
- **Objetivo:** Compreender a estrutura do projeto, validar o build Maven, configurar ferramentas de IA (mentoria e QA) e documentação base.
- **Conceitos:** Maven, estrutura de projeto Spring Boot, versionamento com Git.
- **Status:** Em andamento / Concluindo fundação.

### Etapa 2 — Desenvolvimento do `transfer-service` (Domínio e REST)
- **Objetivo:** Construir a primeira versão funcional do serviço de transferências.
- **Conceitos:** modelagem de domínio (JPA Entities), DTOs, mapeamento, Bean Validation, Controllers REST e tratamento global de exceções.
- **Atividades práticas:** Criar entidade `Transfer`, DTOs de entrada/saída, Controller REST e testes unitários e de integração básicos.

### Etapa 3 — Persistência e Confiabilidade com PostgreSQL
- **Objetivo:** Conectar a aplicação a um banco de dados relacional robusto e garantir integridade transacional.
- **Conceitos:** Spring Data JPA Repositories, transações (`@Transactional`), isolamento, conexões e perfis de configuração (`application-dev.properties`).
- **Atividades práticas:** Configurar PostgreSQL via Docker, criar migrações/tabelas e implementar testes de repositório.

### Etapa 4 — Evolução para Arquitetura de Microsserviços (`processing-service`)
- **Objetivo:** Separar responsabilidades criando o segundo microsserviço.
- **Conceitos:** Microsserviços, contratos de API, desacoplamento e independência de deploy.
- **Atividades práticas:** Criar o módulo/projeto `processing-service` e estabelecer comunicação inicial.

### Etapa 5 — Mensageria Assíncrona com Apache Kafka
- **Objetivo:** Introduzir processamento assíncrono entre os serviços.
- **Conceitos:** Produtores e consumidores Kafka, tópicos, partições, serialização JSON, idempotência e tratamento de erros de mensageria.
- **Atividades práticas:** Integrar Kafka no `transfer-service` (produtor) e `processing-service` (consumidor).

### Etapa 6 — Infraestrutura, Documentação e CI/CD
- **Objetivo:** Automatizar processos e documentar profissionalmente a aplicação.
- **Conceitos:** Docker Compose para subir serviços integrados (Postgres + Kafka), OpenAPI / Swagger e GitHub Actions para build/test automatizados.
- **Atividades práticas:** Criar `docker-compose.yml`, documentação OpenAPI e workflow do GitHub Actions.

### Etapa 7 — Revisão Técnica, Defesa e Portfólio
- **Objetivo:** Consolidar o projeto para apresentação em entrevistas de emprego.
- **Conceitos:** Revisão de código, boas práticas de Clean Code, segurança básica e capacidade de explicar decisões arquiteturais.
- **Atividades práticas:** Auditoria final, refinamento do README e simulação de entrevista técnica sobre o projeto.
