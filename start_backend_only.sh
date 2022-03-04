#!/bin/sh

exec docker-compose -p help-ua --profile backend-only up --force-recreate
