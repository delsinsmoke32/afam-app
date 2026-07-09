#!/usr/bin/env bash
echo "Avvio applicazione AFAM..."
echo "Avvio database..."

DB_STATUS=$(docker compose -f docker/docker-compose.yaml ps --status running -q)

if [[ -z "$DB_STATUS"]]; then
    docker compose -f docker/docker-compose.yml up -d
    sleep 3
else
    echo "Database già in esecuzione."
fi

echo "Avvio applicazione Java..."

./mvnw spring-boot:run
