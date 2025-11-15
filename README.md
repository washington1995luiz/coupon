# Coupon API

API de gestão de cupons construída com Spring Boot 3, validação via Bean Validation, eventos de domínio e persistência H2 (memória). Imagens Docker disponíveis em: https://hub.docker.com/r/washington1995luiz/coupon

## Visão Geral
- Camadas: `controller` → `service` → `repository`
- DTOs: `CouponCreateRequest` (entrada), `CouponResponse` (saída)
- VO: `CouponCode` valida e normaliza o código do cupom (6 caracteres alfanuméricos)
- Tratamento de erros: `ProblemDetail` com mapeamento global de exceções
- Eventos: publicação em criação e deleção de cupons
- Banco: H2 em memória com console em `/h2-console`

## Requisitos
- Java 25
- Maven 
- Porta `8080` livre

## Executar Localmente (Maven)
```bash
# Windows
mvnw.cmd clean package
java -jar target/coupon-0.0.1-SNAPSHOT.jar

# Linux/Mac
./mvnw clean package
java -jar target/coupon-0.0.1-SNAPSHOT.jar
```

A aplicação sobe em `http://localhost:8080`. Console H2: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:coupon`, usuário `admin`, sem senha).

## Endpoints

- POST `/coupon` — cria um cupom
```json
{
  "code": "CODE10",
  "description": "DESCRIPTION",
  "discountValue": 10.0,
  "expirationDate": "2025-12-01T10:00:00",
  "published": true
}
```

- GET `/coupon/{id}` — busca por ID
- DELETE `/coupon/{id}` — remoção (soft delete)

Resposta (exemplo):
```json
{
  "id": "b5f8f8a1-3f7d-4c9f-9e5a-5e6c1b2a8f9d",
  "code": "CODE10",
  "description": "DESCRIPTION",
  "discountValue": 10.0,
  "expirationDate": "2025-12-01T10:00:00",
  "status": "ACTIVE",
  "published": true,
  "redeemed": false
}
```

## Erros e Validação
- `400 Bad Request`: validações de entrada, código inválido, desconto menor que 0.5, ID malformatado
- `404 Not Found`: cupom não encontrado
- `409 Conflict`: conflitos de negócio (ex.: código já existente)
- `500 Internal Server Error`: erros não mapeados

O corpo do erro segue `ProblemDetail`:
```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Mensagem descritiva do erro"
}
```

## Testes
```bash
# Windows
mvnw.cmd test

# Linux/Mac
./mvnw test
```

## Docker

Imagens: https://hub.docker.com/r/washington1995luiz/coupon

Baixar imagem:
```bash
docker pull washington1995luiz/coupon:590c
```

Rodar container:
```bash
docker run --rm -p 8080:8080 washington1995luiz/coupon:590c
```

Após subir:
- API: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`

## Configuração (application.yaml)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:coupon
    driver-class-name: org.h2.Driver
    username: admin
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
```
