# 🚀 Despliegue Remoto en Servidor SSH

## Información del Servidor

```
Host: 192.168.35.132
Usuario: dario
Contraseña: 1234
Usuario root: root
Contraseña root: admin_123
Proyecto: /home/dario/grupo2 (asumido)
```

## Opción 1: Despliegue Automático (Recomendado - WSL/Linux)

Si tienes **Windows Subsystem for Linux (WSL)** o estás en **Linux/Mac**:

### Paso 1: Instalar sshpass (solo la primera vez)

```bash
# En WSL/Ubuntu
sudo apt-get update
sudo apt-get install -y sshpass

# En macOS (Homebrew)
brew install sshpass
```

### Paso 2: Ejecutar despliegue remoto

```bash
# Navega al directorio del proyecto
cd F:\VS Code\Java\Grupo2

# Ejecuta el script
./remote-deploy.sh dario@192.168.35.132 1234 /home/dario/grupo2
```

**Esto hace automáticamente:**
1. ✅ Detiene los Docker containers
2. ✅ Verifica Docker está instalado
3. ✅ Actualiza código con `git pull`
4. ✅ Verifica orden de arranque en docker-compose.yml
5. ✅ Inicia Docker con `docker-compose up -d --build`
6. ✅ Muestra el estado de servicios

---

## Opción 2: Despliegue Manual (Cualquier Windows)

### Paso 1: Conectar por SSH

```bash
ssh dario@192.168.35.132
# Ingresa contraseña: 1234
```

O usa **PuTTY** en Windows:
- Host: 192.168.35.132
- Usuario: dario
- Contraseña: 1234

### Paso 2: Ejecutar comandos en el servidor

Una vez conectado, ejecuta estos comandos en orden:

```bash
# Ir al proyecto
cd /home/dario/grupo2

# 1. VERIFICAR ESTADO ACTUAL
echo "=== ESTADO ACTUAL ==="
docker-compose ps
echo ""

# 2. APAGAR DOCKER
echo "[1/8] Apagando Docker..."
docker-compose down

# 3. VERIFICAR DOCKER
echo "[2/8] Verificando Docker..."
docker --version
docker-compose --version

# 4. ACTUALIZAR GIT
echo "[3/8] Actualizando código..."
git fetch origin
git pull origin main

# 5. VERIFICAR CAMBIOS
echo "[4/8] Últimos cambios:"
git log -1 --oneline

# 6. VERIFICAR ORDEN ARRANQUE
echo "[5/8] Verificando docker-compose.yml..."
grep -A 5 "depends_on:" docker-compose.yml

# 7. INICIAR DOCKER
echo "[6/8] Iniciando Docker..."
docker-compose up -d --build

# 8. ESPERAR INICIALIZACIÓN
echo "[7/8] Esperando 40 segundos..."
sleep 40

# 9. VERIFICAR ESTADO
echo "[8/8] Estado de servicios:"
docker-compose ps

# 10. VER LOGS
echo "Primeros logs:"
docker-compose logs --tail=10 grupo2-app
```

---

## Opción 3: Script PowerShell en Windows

Si prefieres PowerShell:

```powershell
# Ejecutar el script
.\ssh-deploy.ps1 -SSHHost "dario@192.168.35.132" -SSHPassword "1234"
```

Esto mostrará todos los comandos que necesitas copiar/pegar en SSH.

---

## 📋 Checklist de Verificación

Después de desplegar, verifica que todo funciona:

### En el servidor (SSH):

```bash
# 1. Ver estado de todos los servicios
docker-compose ps

# Debería mostrar:
# grupo2-mariadb    UP
# grupo2-nextcloud  UP  
# grupo2-app        UP

# 2. Ver logs de la aplicación
docker-compose logs grupo2-app | tail -20

# Busca: "Started" y "Spring Boot Application Started Successfully"

# 3. Verificar conectividad a BD
docker-compose exec grupo2-app curl http://mariadb:3306 || echo "BD accesible"

# 4. Verificar Nextcloud
docker-compose logs grupo2-nextcloud | tail -10

# 5. Ver últimas líneas de errores
docker-compose logs --tail=5 2>&1 | grep -i error || echo "Sin errores"
```

### Desde tu máquina local:

```bash
# Acceder a la aplicación
http://192.168.35.132:8080

# Acceder a verificador
http://192.168.35.132:8080/verificador.html

# Acceder a Nextcloud
http://192.168.35.132:8888

# Probar API
curl http://192.168.35.132:8080/api/verificador/health
```

---

## 🔍 Orden de Arranque en Docker

El `docker-compose.yml` debe tener el siguiente orden:

```yaml
services:
  mariadb:           # Se inicia primero (BD)
    healthcheck:     # Verifica que esté lista
    
  nextcloud_app:     # Se inicia segundo (almacenamiento)
    depends_on:
      mariadb: 
        condition: service_healthy
    
  app:               # Se inicia tercero (app)
    depends_on:
      mariadb:
        condition: service_healthy
      nextcloud_app:
        condition: service_started
```

**Este orden asegura que:**
1. MariaDB está disponible antes de que app intente conectar
2. Nextcloud está disponible para WebDAV
3. No hay fallos de conexión

---

## ⚡ Troubleshooting

### La aplicación no inicia
```bash
docker-compose logs grupo2-app | tail -50
```

### No conecta a BD
```bash
docker-compose logs grupo2-mariadb
```

### Nextcloud no disponible
```bash
docker-compose restart grupo2-nextcloud
docker-compose logs grupo2-nextcloud
```

### Puerto en uso
```bash
# Ver qué está usando el puerto 8080
lsof -i :8080

# Liberar puerto (mata el proceso)
kill -9 <PID>
```

### Reiniciar todo limpio
```bash
# Detener y eliminar volúmenes (CUIDADO: elimina datos)
docker-compose down -v

# Iniciar de nuevo
docker-compose up -d --build
```

---

## 📝 Pasos Rápidos (Resumen)

### SSH Manual (más fácil si es la primera vez)
```
1. ssh dario@192.168.35.132
2. Contraseña: 1234
3. cd /home/dario/grupo2
4. git pull origin main
5. docker-compose down
6. docker-compose up -d --build
7. sleep 40
8. docker-compose ps
```

### Con Script (una línea)
```
./remote-deploy.sh dario@192.168.35.132 1234
```

---

## 🎯 URLs después de desplegar

```
Aplicación:     http://192.168.35.132:8080
Verificador:    http://192.168.35.132:8080/verificador.html
API REST:       http://192.168.35.132:8080/api/verificador
Nextcloud:      http://192.168.35.132:8888
```

Credenciales:
- Nextcloud: admin / admin_123
- MariaDB: root / admin_123

---

## 💡 Consejos

- Usa la **Opción 1** (script automático) si tienes WSL/Linux
- Usa la **Opción 2** (manual) si tienes dudas o quieres ver qué pasa
- Siempre verifica `docker-compose ps` después de desplegar
- Revisa los logs si algo no funciona

¡Listo! 🚀
