# CLAUDE.md — Processing Service Development Context & Rules

## 1. Visão Geral
O **`processing-service`** é o segundo microsserviço do ecossistema FinFlow. Sua responsabilidade é consumir assincronamente os eventos de transferência gerados pelo `transfer-service` via Apache Kafka, processar as regras de negócio de validação/efetivação e persistir o estado final no seu próprio banco de dados.

## 2. Escopo e Limitações
- Escopo focado e enxuto: máximo de 3 métodos de negócio principais (ex: consumir evento, processar transferência, atualizar status).
- Sem complexidade desnecessária ou funcionalidades fora do escopo educacional.

## 3. Arquitetura Hexagonal (Ports & Adapters)
- **Domain:** Regras de negócio puras, entidades e portas (sem dependências de Spring ou Kafka).
- **Application:** Orquestração de casos de uso (Services).
- **Adapters:** Adaptadores de entrada (Kafka Consumer) e adaptadores de saída (JPA Persistence).

## 4. Regras de Desenvolvimento
- Seguir os padrões estabelecidos no `transfer-service`.
- Explicar conceitos e decisões técnicas antes de implementar.
- Manter código limpo, testável e bem documentado.
