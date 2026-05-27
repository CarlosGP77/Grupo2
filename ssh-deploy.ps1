# Script de despliegue remoto en el servidor
# Ejecutar como: .\ssh-deploy.ps1

param(
    [string]$SSHHost = "dario@192.168.35.132",
    [string]$SSHPassword = "1234",
    [string]$SSHRoot = "root",
    [string]$SSHRootPassword = "admin_123",
    [string]$ProjectPath = "/home/dario/grupo2"
)

Write-Host "`n╔════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║      DESPLIEGUE REMOTO - GRUPO2 APP                   ║" -ForegroundColor Cyan
Write-Host "║      Servidor: $SSHHost" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

Write-Host "PASO 1: Conectar al servidor SSH" -ForegroundColor Yellow
Write-Host "=================================" -ForegroundColor Yellow
Write-Host "Host: $SSHHost" -ForegroundColor Green
Write-Host "Password: (oculta)" -ForegroundColor Green
Write-Host ""

# Comandos a ejecutar en el servidor
$commands = @"
#!/bin/bash

echo "╔════════════════════════════════════════════════════════╗"
echo "║        PASOS DE DESPLIEGUE EN EL SERVIDOR              ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""

# PASO 1: Ir al directorio del proyecto
echo "[PASO 1] Accediendo a directorio del proyecto..."
cd /home/dario/grupo2 || { echo "Error: Directorio no encontrado"; exit 1; }
pwd
echo ""

# PASO 2: Apagar Docker containers
echo "[PASO 2] Apagando Docker containers..."
sudo docker-compose down
echo "✓ Docker detenido"
echo ""

# PASO 3: Verificar estado de Docker
echo "[PASO 3] Verificando Docker..."
docker --version
docker-compose --version
echo ""

# PASO 4: Actualizar código desde Git
echo "[PASO 4] Actualizando código desde Git..."
git status
echo ""
echo "Haciendo pull..."
git pull origin main
echo "✓ Código actualizado"
echo ""

# PASO 5: Mostrar cambios
echo "[PASO 5] Cambios detectados:"
git log -1 --oneline
echo ""

# PASO 6: Verificar orden de arranque en docker-compose.yml
echo "[PASO 6] Verificando orden de arranque en docker-compose.yml..."
echo ""
echo "=== SERVICIOS EN DOCKER-COMPOSE ==="
grep "container_name:" docker-compose.yml
echo ""
echo "=== DEPENDENCIAS ==="
grep -A 5 "depends_on:" docker-compose.yml || echo "No hay dependencias explícitas"
echo ""

# PASO 7: Iniciar Docker containers
echo "[PASO 7] Iniciando Docker containers..."
echo ""
sudo docker-compose up -d --build
echo ""

# PASO 8: Esperar a que inicie
echo "[PASO 8] Esperando a que inicie (30 segundos)..."
sleep 30
echo ""

# PASO 9: Verificar estado
echo "[PASO 9] Estado de servicios:"
sudo docker-compose ps
echo ""

# PASO 10: Verificar logs
echo "[PASO 10] Primeros logs de la aplicación:"
echo ""
sudo docker-compose logs --tail=20 grupo2-app
echo ""

echo "╔════════════════════════════════════════════════════════╗"
echo "║           ✅ DESPLIEGUE COMPLETADO                    ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""
echo "Servicios disponibles:"
echo "  • Aplicación:     http://localhost:8080"
echo "  • Verificador:    http://localhost:8080/verificador.html"
echo "  • Nextcloud:      http://localhost:8888"
echo "  • API REST:       http://localhost:8080/api/verificador"
echo ""
"@

Write-Host "Comandos a ejecutar en el servidor:" -ForegroundColor Magenta
Write-Host $commands -ForegroundColor Gray
Write-Host ""

# Opciones para ejecutar
Write-Host "╔════════════════════════════════════════════════════════╗" -ForegroundColor Yellow
Write-Host "║              OPCIONES DE EJECUCIÓN                   ║" -ForegroundColor Yellow
Write-Host "╚════════════════════════════════════════════════════════╝" -ForegroundColor Yellow
Write-Host ""
Write-Host "OPCIÓN 1: Ejecutar manualmente en el servidor" -ForegroundColor Cyan
Write-Host "  1. Abre una terminal SSH:" -ForegroundColor Gray
Write-Host "     ssh dario@192.168.35.132" -ForegroundColor Green
Write-Host "  2. Ingresa contraseña: 1234" -ForegroundColor Green
Write-Host "  3. Copia y pega los comandos anteriores" -ForegroundColor Gray
Write-Host ""

Write-Host "OPCIÓN 2: Usar sshpass (Linux/WSL)" -ForegroundColor Cyan
Write-Host "  sshpass -p '1234' ssh dario@192.168.35.132 << 'EOF'" -ForegroundColor Green
Write-Host "  (comandos anteriores)" -ForegroundColor Gray
Write-Host "  EOF" -ForegroundColor Green
Write-Host ""

Write-Host "OPCIÓN 3: Clave privada SSH" -ForegroundColor Cyan
Write-Host "  ssh -i C:\ruta\a\clave\privada dario@192.168.35.132" -ForegroundColor Green
Write-Host ""

Write-Host "═════════════════════════════════════════════════════════" -ForegroundColor Yellow

# Guardar comandos en archivo
$comandosFile = "deploy-commands.sh"
$commands | Out-File -FilePath $comandosFile -Encoding UTF8
Write-Host ""
Write-Host "✓ Comandos guardados en: $comandosFile" -ForegroundColor Green
Write-Host ""
Write-Host "Puedes copiar el contenido de ese archivo y ejecutarlo en el servidor" -ForegroundColor Green
