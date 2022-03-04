# API for ads portal

## Prerequisites

- Java 17
- Docker

### Installing Java

You can use [SDKMAN!](https://sdkman.io/) to install Java 17 using this command:
```shell
curl -s https://get.sdkman.io | bash
sdk install java
```

### Installing Docker

Follow instructions on [this page](https://docs.docker.com/get-docker/).

## Running the app

Execute `./start_server.sh` to run the server with running database, pgAdmin and Swagger documentation.

This command will setup:

- API running on [http://localhost:8080](http://localhost:8080)
- Swagger documentation running on [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- pgAdmin running on [http://localhost:8081](http://localhost:8081) (user: `admin@admin.pl`, pass: `admin`)
- PostgreSQL 14.2-alpine running on `http://localhost:5432` (or `http://ads-postgres:5432` internally on docker network)

Execute `./start_db.sh` to run only database with pgAdmin (without the server and Swagger).

## API documentation

UI with API documentation will be available under `/swagger-ui.html` path. OpenAPI (v3) specification will be available under `/v3/api-docs` path.
