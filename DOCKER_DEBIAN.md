# 🐳 CONFIGURACIÓN DOCKER PARA DEBIAN - GUÍA COMPLETA

## 📋 Requisitos para Debian 13

### 1. Instalar Docker

```bash
# Actualizar sistema
sudo apt update && sudo apt upgrade -y

# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Verificar instalación
docker --version
sudo docker ps

# Agregar usuario actual al grupo docker (opcional)
sudo usermod -aG docker $USER
newgrp docker
```

### 2. Instalar Docker Compose

```bash
# Opción 1: Con pip (recomendado)
sudo apt install -y docker-compose

# Opción 2: Binario directo
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version

# Opción 3: Con apt (si está disponible)
sudo apt install docker-compose-plugin
docker compose version
```

### 3. Iniciar Docker

```bash
# Iniciar el servicio
sudo systemctl start docker

# Habilitarlo en el arranque
sudo systemctl enable docker

# Verificar estado
sudo systemctl status docker
```

---

## 🏗️ ARQUITECTURA DOCKER PARA MOUROSUBV2

### Dos Contenedores

```
┌─────────────────────────────────────────────────────────┐
│                    Docker Network                       │
│              mourosubv2-network (172.28.0.0/16)         │
└─────────────────────────────────────────────────────────┘
         ↓                                    ↓
┌──────────────────────────┐    ┌──────────────────────────┐
│   mourosubv2-app (8080)  │    │  mourosubv2-mariadb      │
│                          │    │  (3306 interno)          │
│ - Spring Boot 3.3        │    │                          │
│ - Java 21 JDK            │    │ - MariaDB 11.4           │
│ - Puerto: 8080           │    │ - Puerto: 3306           │
│ - Health check activo    │    │ - Persistent volume      │
└──────────────────────────┘    └──────────────────────────┘
```

### Build Process (Multi-Stage)

```
Stage 1: Builder
├─ Imagen: maven:3.9.6-eclipse-temurin-21-jammy
├─ Compilación: mvn clean package
├─ Salida: target/*.jar
└─ Tamaño: ~800 MB (descargada)

Stage 2: Runtime
├─ Imagen: eclipse-temurin:21-jdk-jammy
├─ Copia: COPY --from=builder /build/target/*.jar app.jar
├─ Salida: mourosubv2:latest
└─ Tamaño: ~250-300 MB final (optimizado)
```

---

## 🚀 EJECUCIÓN EN DEBIAN

### Opción 1: Script automático

```bash
cd ~/mourosubv2  # o tu ruta de instalación
chmod +x run.sh
./run.sh
```

El script:
- ✅ Valida Docker y Docker Compose
- ✅ Crea archivo .env si no existe
- ✅ Ofrece menú interactivo
- ✅ Inicia contenedores
- ✅ Muestra credenciales
- ✅ Ver logs en tiempo real

### Opción 2: Comandos manuales

```bash
# Navegar a la carpeta
cd ~/mourosubv2

# Copiar configuración (si no existe .env)
cp .env.example .env

# Construir imágenes (primera vez)
docker-compose build

# Iniciar contenedores
docker-compose up -d

# Ver estado
docker-compose ps

# Ver logs
docker-compose logs -f app
```

---

## 📊 VARIABLES DE ENTORNO (.env)

```bash
# El archivo .env contiene:
ENVIRONMENT=prod
DB_ROOT_PASSWORD=root
DB_NAME=mourosubv2
DB_USER=root
DB_PASSWORD=root
JPA_DDL_AUTO=create-drop
SPRING_PROFILES_ACTIVE=prod
APP_PORT=8080
JAVA_OPTS=-XX:+UseG1GC -XX:MaxRAMPercentage=75.0
```

**Para cambiar valores:**
```bash
# Editar .env
nano .env

# Reiniciar contenedores
docker-compose restart
```

---

## 🔍 COMANDOS ÚTILES EN DEBIAN

### Ver estado

```bash
# Ver contenedores en ejecución
docker-compose ps

# Ver imágenes
docker images | grep mourosubv2

# Ver redes
docker network ls

# Ver volúmenes
docker volume ls | grep mourosubv2
```

### Logs y debugging

```bash
# Logs de la aplicación (en tiempo real)
docker-compose logs app -f

# Logs de MariaDB
docker-compose logs mariadb -f

# Últimas 100 líneas
docker-compose logs --tail=100 app

# Guardar logs en archivo
docker-compose logs app > app.log
```

### Conectar a la base de datos

```bash
# Desde Debian (sin salir del servidor)
docker exec -it mourosubv2-mariadb mariadb -u root -p

# Contraseña: root

# Consultas útiles
SHOW DATABASES;
USE mourosubv2;
SHOW TABLES;
SELECT * FROM users;
```

### Reiniciar y actualizar

```bash
# Reiniciar un servicio
docker-compose restart app
docker-compose restart mariadb

# Detener sin eliminar datos
docker-compose stop

# Reanudar
docker-compose start

# Reiniciar todo (sin perder datos)
docker-compose down && docker-compose up -d

# Limpiar todo (elimina datos)
docker-compose down -v
```

---

## 🔒 CAMBIAR CONTRASEÑAS (PRODUCCIÓN)

```bash
# 1. Editar .env
nano .env

# Cambiar:
DB_ROOT_PASSWORD=tu_nueva_contraseña
DB_PASSWORD=tu_nueva_contraseña

# 2. Recrear volumen (ADVERTENCIA: elimina datos previos)
docker-compose down -v
docker-compose up -d

# O actualizar en marcha (más complejo):
docker exec mourosubv2-mariadb mariadb -u root -p -e "ALTER USER 'root'@'%' IDENTIFIED BY 'nueva_contraseña';"
```

---

## ⚡ OPTIMIZACIÓN PARA DEBIAN

### Memoria RAM

```bash
# Ver disponibilidad
free -h

# Limitar uso de Java a 70% RAM
# En .env:
JAVA_OPTS=-XX:+UseG1GC -XX:MaxRAMPercentage=70.0
```

### CPU

```bash
# Limitar recursos en docker-compose.yml
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
```

### Almacenamiento

```bash
# Ver espacio
df -h

# Ver tamaño de volúmenes Docker
docker volume inspect mourosubv2_mariadb_data
```

---

## 🚨 SOLUCIÓN DE PROBLEMAS EN DEBIAN

### Puerto 8080 en uso

```bash
# Encontrar proceso
sudo lsof -i :8080

# Eliminar (si es seguro)
sudo kill -9 <PID>

# O cambiar puerto en .env
APP_PORT=8081
docker-compose restart
```

### Permisos negados en Docker

```bash
# Solución: Agregar usuario al grupo docker
sudo usermod -aG docker $USER
newgrp docker

# O usar sudo
sudo docker-compose up -d
```

### MariaDB no inicia

```bash
# Ver logs
docker-compose logs mariadb

# Verificar volumen
docker volume inspect mourosubv2_mariadb_data

# Eliminar y recrear (borra datos)
docker-compose down -v
docker-compose up -d
```

### Aplicación no inicia

```bash
# Ver logs detallados
docker-compose logs app -f

# Verificar conectividad con BD
docker exec mourosubv2-app \
  java -jar app.jar --version

# Reiniciar
docker-compose restart app
```

---

## 📦 BACKUP EN DEBIAN

### Backup de datos

```bash
# Copiar volumen de MariaDB
docker run --rm \
  -v mourosubv2_mariadb_data:/source \
  -v /home/user/backups:/backup \
  alpine tar czf /backup/db_$(date +%Y%m%d_%H%M%S).tar.gz -C /source .

# O respaldo SQL directo
docker exec mourosubv2-mariadb \
  mariadb-dump -u root -proot mourosubv2 > \
  /home/user/backups/mourosubv2_$(date +%Y%m%d_%H%M%S).sql
```

### Restaurar datos

```bash
# Desde archivo SQL
docker exec -i mourosubv2-mariadb \
  mariadb -u root -proot mourosubv2 < \
  /home/user/backups/mourosubv2_backup.sql
```

---

## 🔄 AUTO-ARRANQUE EN DEBIAN

### Crear systemd service

```bash
# Crear archivo
sudo nano /etc/systemd/system/mourosubv2.service
```

Contenido:
```ini
[Unit]
Description=MouroSubV2 Application
After=docker.service
Requires=docker.service

[Service]
Type=simple
WorkingDirectory=/home/user/mourosubv2
ExecStart=/usr/bin/docker-compose up
ExecStop=/usr/bin/docker-compose down
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Activar:
```bash
sudo systemctl daemon-reload
sudo systemctl enable mourosubv2
sudo systemctl start mourosubv2
sudo systemctl status mourosubv2
```

---

## 📊 MONITOREO EN DEBIAN

### Ver recursos

```bash
# Consumo en tiempo real
docker stats

# Historia
docker stats --no-stream
```

### Logs persistentes

```bash
# JSON logs
docker-compose logs --tail=100 app | tail -f

# Archivo de log
docker-compose logs app >> mourosubv2.log
```

---

## 🎯 CHECKLIST PARA PRODUCCIÓN EN DEBIAN

- [ ] Docker installado y funcionando
- [ ] Docker Compose v2.0+
- [ ] .env configurado con variables de producción
- [ ] Cambiar contraseñas en .env
- [ ] Firewall abierto para puerto 8080
- [ ] Certificado SSL/HTTPS (si es necesario)
- [ ] Backup automático de BD configurado
- [ ] Monitoreo de logs activado
- [ ] Auto-arranque configurado (systemd)
- [ ] Prueba de acceso http://localhost:8080

---

## 📞 REFERENCIAS

- Docker en Debian: https://docs.docker.com/engine/install/debian/
- Docker Compose: https://docs.docker.com/compose/
- MariaDB Docker: https://hub.docker.com/_/mariadb
- Eclipse Temurin (Java): https://hub.docker.com/_/eclipse-temurin

**¡Tu aplicación está lista para Debian!** 🚀

n 