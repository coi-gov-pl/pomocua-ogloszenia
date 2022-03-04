#!/bin/sh

docker-compose -p help-ua --profile backend-only down -v
docker rm -fv ads-pgadmin ads-postgres
docker volume rm help-ua_pgadmin help-ua_postgres
