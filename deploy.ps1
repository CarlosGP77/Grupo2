# Script de despliegue para servidor remoto
# Requiere acceso SSH configurado

param(
    [string]$Host = "192.168.35.132",
    [string]$User = "dario",
    [string]$Password = "1234",
    [string]$ProjectPath = "/home/dario/grupo2"
)

Write-Host "`n╔════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║     DESPLIEGUE REMOTO - GRUPO2 APP                        ║" -ForegroundColor Cyan
Write-Host "║     Servidor: $Host" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

Write-Host "🔑 CREDENCIALES:" -ForegroundColor Yellow
Write-Host "  Host:       $Host" -ForegroundColor Green
Write-Host "  Usuario:    $User" -ForegroundColor Green
Write-Host "  Proyecto:   $ProjectPath" -ForegroundColor Green
Write-Host ""

# Comandos a ejecutar
$commands = @"
#!/bin/bash
set -e

cd $ProjectPath || { echo "Error: directorio no encontrado"; exit 1; }

echo "╔════════════════════════════════════════════════════════╗"
echo "║        PASOS DE DESPLIEGUE EN EL SERVIDOR              ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""

# [1] Estado actual
echo "[1/10] Estado actual:"
echo "========================================================"
pwd
echo ""

# [2] Apagar Docker
echo "[2/10] Apagando Docker containers..."
echo "========================================================"
docker-compose down 2>/dev/null || echo "✓ Sin containers previos"
echo ""

# [3] Verificar Docker
echo "[3/10] Verificando Docker..."
echo "========================================================"
docker --version
docker-compose --version
echo ""

# [4] Actualizar Git
echo "[4/10] Actualizando código desde Git..."
echo "========================================================"
git fetch origin
git pull origin main || { echo "✗ Error en git pull"; exit 1; }
echo ""

# [5] Mostrar cambios
echo "[5/10] Últimos cambios:"
echo "========================================================"
git log -1 --oneline
echo ""

# [6] Verificar docker-compose
echo "[6/10] Verificando orden de arranque..."
echo "========================================================"
grep -A 10 "depends_on:" docker-compose.yml || echo "Sin dependencias explícitas"
echo ""

# [7] Limpiar docker
echo "[7/10] Limpiando Docker (imágenes no usadas)..."
echo "========================================================"
docker image prune -af --filter "until=24h" || true
echo ""

# [8] Iniciar Docker
echo "[8/10] Iniciando Docker..."
echo "========================================================"
docker-compose up -d --build
echo ""

# [9] Esperar
echo "[9/10] Esperando inicialización (40 segundos)..."
echo "========================================================"
sleep 40
echo ""

# [10] Estado final
echo "[10/10] Estado final de servicios:"
echo "========================================================"
docker-compose ps
echo ""

echo "╔════════════════════════════════════════════════════════╗"
echo "║           ✅ DESPLIEGUE COMPLETADO                    ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""
echo "📊 SERVICIOS:"
echo "  • App:        http://192.168.35.132:8080"
echo "  • Verificador: http://192.168.35.132:8080/verificador.html"
echo "  • Nextcloud:  http://192.168.35.132:8888"
echo ""
echo "📋 LOGS:"
docker-compose logs --tail=10 grupo2-app
"@

Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

# Guardar script
$scriptFile = "remote-deploy.sh"
$commands | Out-File -FilePath $scriptFile -Encoding UTF8
Write-Host "✓ Script guardado en: $scriptFile" -ForegroundColor Green
Write-Host ""

# Mostrar opciones
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Yellow
Write-Host ""
Write-Host "INSTRUCCIONES:" -ForegroundColor Yellow
Write-Host ""
Write-Host "  OPCIÓN A: Si tienes OpenSSH en Windows" -ForegroundColor Cyan
Write-Host "    ssh $User@$Host" -ForegroundColor Green
Write-Host "    (Contraseña: $Password)" -ForegroundColor Green
Write-Host ""
Write-Host "  OPCIÓN B: Si tienes Git Bash o WSL" -ForegroundColor Cyan
Write-Host "    wsl bash remote-deploy.sh" -ForegroundColor Green
Write-Host ""
Write-Host "  OPCIÓN C: Con PuTTY" -ForegroundColor Cyan
Write-Host "    1. Abre PuTTY" -ForegroundColor Green
Write-Host "    2. Host: $Host" -ForegroundColor Green
Write-Host "    3. Usuario: $User" -ForegroundColor Green
Write-Host "    4. Copia y pega el script" -ForegroundColor Green
Write-Host ""

# Crear URL para abrir terminal
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
Write-Host "🚀 COPIAR COMANDO PARA SSH:" -ForegroundColor Magenta
Write-Host ""
Write-Host "ssh $User@$Host" -ForegroundColor Green -BackgroundColor Black
Write-Host ""
Write-Host "Luego copia y pega el contenido de remote-deploy.sh" -ForegroundColor Yellow
Write-Host ""

# Intentar abrir SSH automáticamente si es posible
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

$try_ssh = Read-Host "¿Intentar conexión SSH automática? (s/n)"

if ($try_ssh -eq "s" -or $try_ssh -eq "S") {
    Write-Host ""
    Write-Host "Abriendo terminal SSH..." -ForegroundColor Yellow
    Write-Host ""

    # Crear archivo de contraseña temporal (NO RECOMENDADO EN PRODUCCIÓN)
    $sshCommand = "ssh -o 'StrictHostKeyChecking=no' $User@$Host"

    Write-Host "Ejecutando: $sshCommand" -ForegroundColor Gray
    Write-Host "Ingresa la contraseña cuando se solicite: $Password" -ForegroundColor Yellow
    Write-Host ""

    # Intentar abrir SSH
    & cmd /c "echo. | ssh -o 'BatchMode=no' -o 'StrictHostKeyChecking=no' $User@$Host"
}

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║  📖 Para más información, ver:                         ║" -ForegroundColor Green
Write-Host "║     SSH_DEPLOY_INSTRUCCIONES.md                        ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════════════════╝" -ForegroundColor Green
Write-Host ""
