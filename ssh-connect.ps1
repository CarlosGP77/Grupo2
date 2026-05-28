# Script para conectarse al servidor y verificar estado
param(
    [string]$Host = "192.168.35.132",
    [string]$User = "dario",
    [string]$Password = "1234"
)

Write-Host "Conectando a $Host como $User..." -ForegroundColor Cyan

# Crear archivo temporal con comandos
$tempScript = @"
cd ~/grupo2
echo "=== Docker Status ==="
docker compose ps
echo ""
echo "=== Docker Logs (últimas 30 líneas) ==="
docker compose logs --tail=30
echo ""
echo "=== Estado de MariaDB ==="
docker exec grupo2-mariadb mysql -u root -padmin_123 -e "SELECT VERSION();"
echo ""
echo "=== Tablas en BD ==="
docker exec grupo2-mariadb mysql -u root -padmin_123 mourosub -e "SHOW TABLES;"
echo ""
echo "=== Datos de usuarios ==="
docker exec grupo2-mariadb mysql -u root -padmin_123 mourosub -e "SELECT * FROM usuarios LIMIT 5;"
"@

# Guardar en archivo temporal
$tempFile = [System.IO.Path]::GetTempFileName() -replace '\.tmp$', '.sh'
Set-Content -Path $tempFile -Value $tempScript

try {
    # Ejecutar SSH con el archivo de comandos
    & ssh.exe -o StrictHostKeyChecking=no -o UserKnownHostsFile=$null "${User}@${Host}" -i "$env:USERPROFILE\.ssh\id_rsa" "bash < <(cat << 'EOF'`n$tempScript`nEOF`n)"
}
catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
finally {
    Remove-Item -Path $tempFile -Force -ErrorAction SilentlyContinue
}
