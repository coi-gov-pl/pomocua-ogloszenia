# API for ads portal

## Prerequisites

- Java 17
- PostgreSQL 14.2

## Docker-Compose

Sets up:

- postgresql 14.2-alpine at postgres://127.0.0.1:5432 or postgres://ads-postgres:5432/
  with database "ads" and password "admin"
- pgadmin at http://localhost:8081 (user: admin@admin.pl pass: admin)

## API documentation

In order to enable API documentation, you must run the app with `api-docs` Spring Boot profile.

UI with API documentation will be available under `/swagger-ui.html` path. OpenAPI (v3) specification will be available under `/v3/api-docs` path.
