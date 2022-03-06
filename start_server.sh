#!/bin/sh
./gradlew build -x test && \
exec docker-compose -p help-ua --profile app-db-only up --force-recreate
