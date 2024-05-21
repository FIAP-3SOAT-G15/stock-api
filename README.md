# Microsserviço de estoque

Administração de produtos, componentes de produtos, e estoque de componentes.

Consulte a documentação completa no [repositório principal do grupo](https://github.com/FIAP-3SOAT-G15/tech-challenge).

### Análise estática

Projeto no SonarCloud: [https://sonarcloud.io/project/overview?id=FIAP-3SOAT-G15_stock-api](https://sonarcloud.io/project/overview?id=FIAP-3SOAT-G15_stock-api)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=FIAP-3SOAT-G15_stock-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=FIAP-3SOAT-G15_stock-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=FIAP-3SOAT-G15_stock-api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=FIAP-3SOAT-G15_stock-api)

### BDD / Cucumber

Este microsserviço contém testes de integração implementados com Cucumber:
- [Component.feature](src/test/resources/features/Component.feature): cadastro de componente (e estoque)
- [Product.feature](src/test/resources/features/Product.feature): cadastro de produto

Os testes são executados durante CI e também podem ser executados manualmente conforme comandos a seguir.

### Executar localmente

```bash
docker compose up
```

### Mappers

```
mvn clean compile
```

### Testes

```
mvn clean verify
```

Testes de integração:

```
mvn clean verify -DskipITs=false
```

### ktlint

```
mvn antrun:run@ktlint-format
```
