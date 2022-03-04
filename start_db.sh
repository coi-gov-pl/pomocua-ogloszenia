#!/bin/sh

exec docker-compose -p help-ua --profile db-only up --force-recreate
