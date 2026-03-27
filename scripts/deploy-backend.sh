#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker is not installed on this machine."
  exit 1
fi

if docker compose version >/dev/null 2>&1; then
  COMPOSE_CMD=(docker compose)
elif command -v docker-compose >/dev/null 2>&1; then
  COMPOSE_CMD=(docker-compose)
else
  echo "Docker Compose is not available. Install docker compose or docker-compose first."
  exit 1
fi

if [ ! -f .env.backend ]; then
  echo "Missing .env.backend"
  echo "Copy .env.backend.example to .env.backend and set FACEIT_API_KEY first."
  exit 1
fi

echo "Pulling latest code..."
git pull --ff-only

echo "Building and starting backend..."
"${COMPOSE_CMD[@]}" --env-file .env.backend -f docker-compose.backend.yml up -d --build

echo
echo "Backend status:"
"${COMPOSE_CMD[@]}" -f docker-compose.backend.yml ps

echo
echo "Recent backend logs:"
"${COMPOSE_CMD[@]}" -f docker-compose.backend.yml logs --tail=40 backend