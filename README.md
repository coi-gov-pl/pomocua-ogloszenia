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

### Installing docker-compose

Follow instructions on [this page](https://docs.docker.com/compose/install/).

## Running the app

### Running whole environment:

Before you run apps, you have to set variables in .env or .env.{environment} file.

If you want to run the app with keycloak, proxy and db you can do it with (first step is optional for cleaing up):

        $ docker-compose down -v --remove-orphans
        $ docker-compose build
        $ REINIT_KEYCLOAK=1 docker-compose -p help-ua up -d

REINIT\_KEYCLOAK forces the keycloak to reinit the DB to initial state (all changes **WILL GET LOST**)._
After reiniting keycloak you can restart the containers without REINIT\_KEYCLOAK flag:

        $ docker-compose -p help-ua stop
        $ docker-compose -p help-ua up -d

TODO: it would be good to add to nginx the FrontEnd code somehow? For now you
can modify the docker-compose to have volume bind to the web dist folder..

### Running API with database and Swagger (without keycloak):

Execute `./start_server.sh` to run the server with running database, pgAdmin and Swagger documentation.

This command will setup:

- API running on [http://localhost:8080](http://localhost:8080)
- Swagger documentation running on [http://localhost:8080/ogloszenia/swagger-ui.html](http://localhost:8080/ogloszenia/swagger-ui.html)
- pgAdmin running on [http://localhost:8081](http://localhost:8081) (user: `admin@admin.pl`, pass: `admin`)
- PostgreSQL 14.2-alpine running on `http://localhost:5432` (or `http://ads-postgres:5432` internally on docker network)

**NOTE:** if you face some problems during server startup (and the server is not up in result), it may be related to some changes in SQL DDL scripts.
To fix this, run `./cleanup_docker.sh` script, and then `./start_server.sh` again. This will remove Docker database containers (so all data in DB will be erased).

### Running database only:

Execute `./start_db.sh` to run only database with pgAdmin (without the server and Swagger).

## How to use?

You can use PostMan to get token and query the API. You should use in PostMan the following oauth2 token configuration:

Authorization type: oauth2
Header prefix: Bearer
Token name: token
Callback URL: https://local.pomagamukrainie.gov.pl/ogloszenia/test
Auth URL: https://local.pomagamukrainie.gov.pl/auth/realms/POMOCUA/protocol/openid-connect/auth
Access Token URL: https://local.pomagamukrainie.gov.pl/auth/realms/POMOCUA/protocol/openid-connect/token
ClientId: ogloszenia-fe
ClientSecret: 
Scope: openid profile
State: 123

You can use the following sample user to login:
login: znxtfetqfiqsds6wqzbdl6pb4eslbyxx
password: test


## API Swagger documentation

UI with API documentation will be available under `/swagger-ui.html` path. OpenAPI (v3) specification will be available under `/v3/api-docs` path.
