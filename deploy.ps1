# Script de despliegue remoto para Grupo2 App - PowerShell Version
# Conecta por SSH y ejecuta el despliegue de Docker

param(
    [string]$Host = "192.168.35.132",
    [string]$User = "dario",
    [string]$Password = "1234",
    [string]$ProjectPath = "/home/dario/grupo2"
)

Write-Host "`n╔════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║     CONEXIÓN SSH - $Host" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

function Execute-RemoteCommand {
    param(
        [string]$Command,
        [string]$Description,
        [int]$StepNumber
    )

    if ($Description) {
        Write-Host "[$StepNumber/10] $Description" -ForegroundColor Yellow
        Write-Host ("=" * 58)
    }

    try {
        # Create SSH command with sshpass
        $sshCommand = "sshpass -p '$Password' ssh -o StrictHostKeyChecking=no $User@$Host 'cd $ProjectPath && $Command'"
        
        # Execute through cmd
        $output = cmd /c $sshCommand 2>&1
        Write-Host $output
        return $true
    }
    catch {
        Write-Host "✗ Error: $_" -ForegroundColor Red
        return $false
    }
}

# PASO 1
Execute-RemoteCommand -Command "pwd" -Description "Estado actual:" -StepNumber 1

# PASO 2
Execute-RemoteCommand -Command "docker-compose down 2>/dev/null || echo 'No había containers activos'" -Description "Apagando Docker containers..." -StepNumber 2

# PASO 3
Execute-RemoteCommand -Command "docker --version && docker-compose --version" -Description "Verificando Docker..." -StepNumber 3

# PASO 4
Execute-RemoteCommand -Command "git fetch origin && git pull origin main" -Description "Actualizando código desde Git..." -StepNumber 4

# PASO 5
Execute-RemoteCommand -Command "git log -1 --oneline" -Description "Últimos cambios:" -StepNumber 5

# PASO 6
Execute-RemoteCommand -Command "grep -A 10 'depends_on:' docker-compose.yml || echo 'Sin dependencias'" -Description "Verificando dependencias..." -StepNumber 6

# PASO 7
Execute-RemoteCommand -Command "docker-compose up -d --build" -Description "Iniciando Docker..." -StepNumber 7

# PASO 8
Write-Host "[8/10] Esperando inicialización (40 segundos)..." -ForegroundColor Yellow
for ($i = 40; $i -gt 0; $i -= 10) {
    Write-Host "  ⏳ ${i}s restantes..."
    Start-Sleep -Seconds 10
}
Write-Host "  ✓ Inicialización completada" -ForegroundColor Green

# PASO 9
Execute-RemoteCommand -Command "docker-compose ps" -Description "Estado de servicios:" -StepNumber 9

# PASO 10
Execute-RemoteCommand -Command "docker-compose logs --tail=20 grupo2-app" -Description "Logs de la aplicación:" -StepNumber 10

Write-Host "`n✅ DESPLIEGUE COMPLETADO`n" -ForegroundColor Green
Write-Host "📊 SERVICIOS DISPONIBLES:" -ForegroundColor Cyan
Write-Host "  • Aplicación:     http://192.168.35.132:8080"
Write-Host "  • Verificador:    http://192.168.35.132:8080/verificador.html"
Write-Host "  • API REST:       http://192.168.35.132:8080/api/verificador"
Write-Host "  • Nextcloud:      http://192.168.35.132:8888`n"
