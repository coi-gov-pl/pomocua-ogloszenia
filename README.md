# API for ads portal

## Prerequisites

- Java 17
- PostgreSQL 14.2

### Docker prerequisities

- some form of working docker
- docker-compose

Docker will setup:

For profile backend-only:
- PostgreSQL 14.2-alpine at localhost:5432 (or ogloszenia-postgres internally on docker network)
- pgAdmin 4 on http://localhost:8081

For profile all:
- optionally will run application on port 8080 

pgAdmin4 credentials:

user: admin@admin.pl
pass: admin

## API documentation

In order to enable API documentation, you must run the app with `api-docs` Spring Boot profile.

UI with API documentation will be available under `/swagger-ui.html` path. OpenAPI (v3) specification will be available under `/v3/api-docs` path.
