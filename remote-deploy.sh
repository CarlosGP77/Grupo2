#!/bin/bash

# Script de despliegue remoto
# Uso: ./remote-deploy.sh dario@192.168.35.132 1234

SSH_HOST="${1:-dario@192.168.35.132}"
SSH_PASS="${2:-1234}"
PROJECT_PATH="${3:-/home/dario/grupo2}"

echo "╔════════════════════════════════════════════════════════╗"
echo "║      DESPLIEGUE REMOTO - GRUPO2 APP                   ║"
echo "║      Servidor: $SSH_HOST"
echo "╚════════════════════════════════════════════════════════╝"
echo ""

# Función para ejecutar comandos remotos
execute_remote() {
    if command -v sshpass &> /dev/null; then
        echo "[SSH] Usando sshpass para autenticación..."
        sshpass -p "$SSH_PASS" ssh "$SSH_HOST" "$1"
    else
        echo "[SSH] sshpass no disponible. Usa:"
        echo "  ssh $SSH_HOST"
        echo "  (ingresa contraseña: $SSH_PASS)"
        echo ""
        echo "Luego ejecuta estos comandos en el servidor:"
        echo "$1"
        return 1
    fi
}

# Script a ejecutar en el servidor
REMOTE_SCRIPT='
#!/bin/bash
set -e

echo "╔════════════════════════════════════════════════════════╗"
echo "║        PASOS DE DESPLIEGUE EN EL SERVIDOR              ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""

# PASO 1: Ir al directorio
echo "[1/10] Accediendo a directorio del proyecto..."
cd '"$PROJECT_PATH"' || { echo "✗ Error: Directorio no encontrado"; exit 1; }
pwd
echo "✓ Directorio correcto"
echo ""

# PASO 2: Apagar Docker
echo "[2/10] Deteniendo Docker containers..."
sudo docker-compose down 2>/dev/null || docker-compose down 2>/dev/null || true
echo "✓ Docker detenido"
echo ""

# PASO 3: Verificar Docker
echo "[3/10] Verificando instalación de Docker..."
docker --version
docker-compose --version
echo "✓ Docker disponible"
echo ""

# PASO 4: Actualizar Git
echo "[4/10] Actualizando código desde Git..."
git fetch origin
git status
echo ""
git pull origin main || { echo "✗ Error en git pull"; exit 1; }
echo "✓ Código actualizado"
echo ""

# PASO 5: Mostrar últimos cambios
echo "[5/10] Últimos cambios:"
git log -1 --oneline
echo ""

# PASO 6: Verificar docker-compose.yml
echo "[6/10] Verificando configuración de Docker..."
echo ""
echo "=== SERVICIOS ==="
grep "container_name:" docker-compose.yml
echo ""
echo "=== ORDEN DE ARRANQUE ==="
grep -A 10 "depends_on:" docker-compose.yml || echo "  (Sin dependencias explícitas - arranque paralelo)"
echo ""

# PASO 7: Limpiar Docker (opcional)
echo "[7/10] Limpiando Docker (imágenes no usadas)..."
docker image prune -af --filter "until=24h" || true
echo "✓ Limpieza completada"
echo ""

# PASO 8: Iniciar Docker
echo "[8/10] Construyendo e iniciando Docker containers..."
sudo docker-compose up -d --build || docker-compose up -d --build
echo "✓ Docker iniciado"
echo ""

# PASO 9: Esperar inicialización
echo "[9/10] Esperando inicialización (40 segundos)..."
sleep 40
echo "✓ Servicios iniciándose"
echo ""

# PASO 10: Verificar estado
echo "[10/10] Estado final de servicios:"
echo ""
sudo docker-compose ps || docker-compose ps
echo ""

# Información adicional
echo "╔════════════════════════════════════════════════════════╗"
echo "║           ✅ DESPLIEGUE COMPLETADO                    ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""
echo "📊 SERVICIOS DISPONIBLES:"
echo "  • Aplicación:     http://localhost:8080"
echo "  • Verificador:    http://localhost:8080/verificador.html"
echo "  • Nextcloud:      http://localhost:8888"
echo "  • API REST:       http://localhost:8080/api/verificador"
echo ""
echo "📋 LOGS (últimas líneas):"
sudo docker-compose logs --tail=5 grupo2-app || docker-compose logs --tail=5 grupo2-app
echo ""
echo "💡 Para ver logs en tiempo real:"
echo "   docker-compose logs -f grupo2-app"
echo ""
'

echo "Ejecutando despliegue remoto..."
echo ""
execute_remote "$REMOTE_SCRIPT"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Despliegue completado exitosamente"
else
    echo ""
    echo "⚠️  No se pudo ejecutar automáticamente"
    echo "Copiar el script anterior y ejecutarlo manualmente en el servidor"
fi
