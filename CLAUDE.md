# CLAUDE.md — FinFlow Development Context & Rules

## 1. Visão Geral do Projeto
O **FinFlow** é um projeto educacional e de portfólio que simula um sistema de transferências financeiras fictícias. O objetivo principal é proporcionar aprendizado prático e profundo em arquitetura de software, microsserviços, mensageria (Kafka), transações, idempotência, testes automatizados e boas práticas de desenvolvimento backend em Java com Spring Boot.

## 2. Contexto do Desenvolvedor & Método de Mentoria
- **Desenvolvedor:** Estudante de Análise e Desenvolvimento de Sistemas (ADS), preparando-se para estágios e vagas juniores em desenvolvimento de software.
- **Abordagem de Aprendizado:** O desenvolvedor **não** quer apenas copiar código gerado por IA. O objetivo é aprender a pensar, projetar, implementar, testar e tomar decisões técnicas fundamentadas.
- **Papel da IA (Mentoria Sênior):** Atuar como mentor técnico e parceiro de desenvolvimento. Explicar conceitos antes de implementações relevantes, guiar passo a passo em etapas pequenas, revisar código, apontar trade-offs e garantir autonomia nas decisões finais.

## 3. Stack Tecnológica
- **Linguagem:** Java 17
- **Framework:** Spring Boot (v4.1.1)
- **Gerenciamento de Dependências / Build:** Maven (`mvnw`)
- **Persistência / Banco de Dados:** Spring Data JPA, PostgreSQL (runtime)
- **Validação / Web:** Spring WebMVC, Bean Validation (`jakarta.validation`)
- **Mensageria:** Spring for Apache Kafka
- **Testes:** JUnit 5, Mockito, Spring Boot Test
- **Utilitários:** Project Lombok, Spring Boot DevTools
- **Qualidade / Documentação:** OpenAPI / Swagger (planejado), GitHub Actions (planejado)
- **Containerização:** Docker e Docker Compose (planejado)

## 4. Arquitetura e Microsserviços Planejados
O sistema evoluirá gradualmente para uma arquitetura com dois microsserviços:
1. **`transfer-service` (Repositório atual):**
   - Recebimento de solicitações de transferência via API REST.
   - Validação de entradas (Bean Validation) e regras de negócio iniciais.
   - Persistência de dados de transferência.
2. **`processing-service` (Futuro):**
   - Processamento assíncrono de transferências recebidas.
   - Consumo de mensagens via Kafka.
   - Evolução de mecanismos de confiabilidade, reprocessamento e tratamento de falhas.

## 5. Estrutura Atual do Repositório
```text
transfer-service/
├── .mvn/wrapper/          # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/finflow/transfer/
│   │   │   └── TransferServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/finflow/transfer/
│           └── TransferServiceApplicationTests.java
├── pom.xml                # Configuração de dependências Maven
├── mvnw / mvnw.cmd        # Scripts do Maven Wrapper
└── README.md              # Documentação principal
```

## 6. Comandos Úteis
- **Compilar e testar:** `& .\mvnw.cmd test`
- **Compilar sem executar testes:** `& .\mvnw.cmd clean compile`
- **Empacotar:** `& .\mvnw.cmd clean package`

## 7. Estratégia de Git e Segurança
- Commits devem ser descritivos e seguir o padrão do projeto.
- **Nunca** executar force push, resets destrutivos ou exclusões em massa sem autorização explícita.
- **Nunca** efetuar commit ou push sem a autorização do usuário.
- Proteger credenciais e segredos (não hardcodar senhas no código).

## 8. Agentes Configurados
- **Agente 1 — FinFlow Mentor / Developer:** Focado em arquitetura, explicações conceituais, orientação de código e coordenação geral.
- **Agente 2 — FinFlow QA / Test Engineer:** Focado em cenários de teste, JUnit, Mockito, validação de casos positivos/negativos e qualidade.

## 9. Regras para Alterações de Código
1. Sempre explicar o problema e a solução conceitual antes de escrever código.
2. Aguardar a confirmação do desenvolvedor antes de alterar código de aplicação ou executar operações de escrita estruturais.
3. Executar e verificar os testes após qualquer alteração relevante.
