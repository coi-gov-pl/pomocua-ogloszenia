#!/bin/sh

exec docker-compose -p pomocua --profile backend-only up --force-recreate
