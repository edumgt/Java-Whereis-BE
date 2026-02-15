#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

if ! command -v docker >/dev/null 2>&1; then
  echo "[ERROR] docker 가 설치되어 있지 않습니다."
  exit 1
fi

if ! command -v mvn >/dev/null 2>&1; then
  echo "[ERROR] mvn 이 설치되어 있지 않습니다."
  exit 1
fi

echo "[1/3] MariaDB 컨테이너 시작"
docker compose up -d mariadb

echo "[2/3] MariaDB healthcheck 대기"
until [ "$(docker inspect --format='{{json .State.Health.Status}}' whereis-mariadb 2>/dev/null || echo '"starting"')" = '"healthy"' ]; do
  echo " - DB 준비 중..."
  sleep 2
done

echo "[3/3] Spring Boot 실행"
SPRING_DATASOURCE_URL="${SPRING_DATASOURCE_URL:-jdbc:mariadb://127.0.0.1:3306/homedb?serverTimezone=UTC}" \
SPRING_DATASOURCE_USERNAME="${SPRING_DATASOURCE_USERNAME:-root}" \
SPRING_DATASOURCE_PASSWORD="${SPRING_DATASOURCE_PASSWORD:-123456}" \
mvn spring-boot:run
