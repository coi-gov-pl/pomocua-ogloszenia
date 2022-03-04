#!/bin/sh
./gradlew build -x test && \
exec docker-compose -p help-ua --profile all up --force-recreate
