#!/bin/bash

# Script de despliegue remoto para Grupo2 App
# Ejecutar en el servidor remoto

PROJECT_PATH="/home/dario/grupo2"

echo ""
echo "??????????????????????????????????????????????????????????"
echo "?           DEPLOYING GRUPO2 APP TO DOCKER              ?"
echo "??????????????????????????????????????????????????????????"
echo ""

# PASO 1
echo "[1/10] Estado actual:"
echo "========================================================"
cd $PROJECT_PATH && pwd

# PASO 2
echo ""
echo "[2/10] Apagando Docker containers..."
echo "========================================================"
docker-compose down 2>/dev/null || echo "No hab?a containers activos"

# PASO 3
echo ""
echo "[3/10] Verificando Docker..."
echo "========================================================"
docker --version && docker-compose --version

# PASO 4
echo ""
echo "[4/10] Actualizando c?digo desde Git..."
echo "========================================================"
cd $PROJECT_PATH && git fetch origin && git pull origin main

# PASO 5
echo ""
echo "[5/10] ?ltimos cambios:"
echo "========================================================"
cd $PROJECT_PATH && git log -1 --oneline

# PASO 6
echo ""
echo "[6/10] Verificando dependencias en docker-compose..."
echo "========================================================"
cd $PROJECT_PATH && grep -A 10 'depends_on:' docker-compose.yml || echo "Sin dependencias expl?citas"

# PASO 7
echo ""
echo "[7/10] Iniciando Docker (construyendo imagen)..."
echo "========================================================"
cd $PROJECT_PATH && docker-compose up -d --build

# PASO 8
echo ""
echo "[8/10] Esperando inicializaci?n (40 segundos)..."
echo "========================================================"
for i in {40..10..10}; do
    echo "  ? ${i}s restantes..."
    sleep 10
done
echo "  ? Inicializaci?n completada"

# PASO 9
echo ""
echo "[9/10] Estado de servicios:"
echo "========================================================"
cd $PROJECT_PATH && docker-compose ps

# PASO 10
echo ""
echo "[10/10] Logs de la aplicaci?n:"
echo "========================================================"
cd $PROJECT_PATH && docker-compose logs --tail=20 grupo2-app

echo ""
echo "??????????????????????????????????????????????????????????"
echo "?           ? DESPLIEGUE COMPLETADO                    ?"
echo "??????????????????????????????????????????????????????????"
echo ""
echo "?? SERVICIOS DISPONIBLES:"
echo "  ? Aplicaci?n:     http://192.168.35.132:8080"
echo "  ? Verificador:    http://192.168.35.132:8080/verificador.html"
echo "  ? API REST:       http://192.168.35.132:8080/api/verificador"
echo "  ? Nextcloud:      http://192.168.35.132:8888"
echo ""
echo "?? Para ver logs en tiempo real:"
echo "   docker-compose logs -f grupo2-app"
echo ""