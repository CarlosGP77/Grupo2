#!/bin/bash
set -e

PROJECT_PATH="/home/dario/Grupo2"

echo "=================================================="
echo "DEPLOYING GRUPO2 APP"
echo "=================================================="
echo ""

echo "[1/6] Apagando Docker containers..."
cd $PROJECT_PATH && docker-compose down 2>/dev/null || echo "No containers running"

echo ""
echo "[2/6] Actualizando código desde Git..."
cd $PROJECT_PATH && git fetch origin && git pull origin main

echo ""
echo "[3/6] Mostrando últimos cambios..."
cd $PROJECT_PATH && git log -1 --oneline

echo ""
echo "[4/6] Iniciando Docker..."
cd $PROJECT_PATH && docker-compose up -d --build

echo ""
echo "[5/6] Esperando inicialización (30 segundos)..."
sleep 30

echo ""
echo "[6/6] Estado de servicios:"
cd $PROJECT_PATH && docker-compose ps

echo ""
echo "✓ DESPLIEGUE COMPLETADO"
echo ""
echo "SERVICIOS DISPONIBLES:"
echo "  • Aplicación:     https://192.168.35.132/"
echo "  • API:            https://192.168.35.132/api/"
echo ""
