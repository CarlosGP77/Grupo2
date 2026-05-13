# 🚀 MOUROSUBV2 EN DEBIAN 13 - GUÍA DEFINITIVA

## 📋 RESUMEN DE LA CONFIGURACIÓN DOCKER

Tu proyecto ahora tiene una configuración completa para Debian con **2 contenedores Docker**:

```
┌──────────────────────────────────────────────────────────┐
│              MOUROSUBV2 EN DEBIAN 13                     │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  Contenedor 1: Maven + Java (Compilación)               │
│  ├─ Imagen: maven:3.9.6-eclipse-temurin-21-jammy      │
│  ├─ Build stage: Compila JAR con Maven                │
│  └─ Tamaño: ~800 MB (descargado, no usado después)    │
│                                                          │
│  Contenedor 2: Spring Boot + JDK (Runtime)              │
│  ├─ Imagen: eclipse-temurin:21-jdk-jammy               │
│  ├─ Runtime: Ejecuta la aplicación                     │
│  └─ Tamaño: ~250 MB final (optimizado)                │
│                                                          │
│  Contenedor 3: MariaDB 11.4 (Base de Datos)             │
│  ├─ Imagen: mariadb:11.4-jammy                         │
│  ├─ Database: mourosubv2                               │
│  └─ Port: 3306 (interno)                               │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

---

## 📁 ARCHIVOS DOCKER CREADOS

```
MouroSubV2/
├── Dockerfile                 (Desarrollo - build rápido)
├── Dockerfile.prod            (Producción - build optimizado)
├── docker-compose.yml         (Desarrollo)
├── docker-compose.prod.yml    (Producción con recursos limitados)
├── .env.example               (Variables de entorno)
├── config/
│   └── mariadb.cnf           (Configuración MariaDB optimizada)
├── run.sh                     (Script para Linux/Mac - interactivo)
├── deploy.sh                  (Script de producción - menú avanzado)
└── DOCKER_DEBIAN.md          (Documentación completa Docker)
```

---

## ⚡ INICIO RÁPIDO EN DEBIAN

### 1. Instalar Docker (una sola vez)

```bash
# Actualizar sistema
sudo apt update && sudo apt upgrade -y

# Instalar Docker
curl -fsSL https://get.docker.com | sudo sh

# Instalar Docker Compose
sudo apt install docker-compose

# Iniciar Docker
sudo systemctl start docker
sudo systemctl enable docker

# Verificar
docker --version
docker-compose --version
```

### 2. Descargar y ejecutar el proyecto

```bash
# Clonar/descargar proyecto
cd ~/
git clone <tu-repo>/MouroSubV2.git
# o copiar carpeta existente
cd MouroSubV2

# Hacer scripts ejecutables
chmod +x run.sh deploy.sh

# Opción A: Ejecución simple (desarrollo)
./run.sh

# Opción B: Ejecución avanzada (producción)
sudo ./deploy.sh
```

---

## 🎯 TRES FORMAS DE EJECUTAR

### FORMA 1: Script Interactivo (Fácil) ✅ RECOMENDADO

```bash
./run.sh

# Menú:
# 1) Iniciar aplicación
# 2) Ver logs
# 3) Detener
# 4) Reiniciar
# 5) Limpiar todo
```

### FORMA 2: Script Avanzado (Producción)

```bash
sudo ./deploy.sh

# Menú:
# 1) Instalación inicial completa
# 2) Compilar imágenes
# 3) Iniciar servicios
# 4) Detener servicios
# 5) Reiniciar servicios
# 6) Backup BD (SQL)
# 7) Backup volumen
# 8) Ver estado y recursos
# 9) Ver logs
# 10) Conectar a MariaDB
```

### FORMA 3: Comandos manuales

```bash
# Copiar configuración
cp .env.example .env

# Desarrollo (--build primera vez)
docker-compose up -d --build

# Producción
docker-compose -f docker-compose.prod.yml up -d --build

# Ver estado
docker-compose ps

# Ver logs
docker-compose logs -f app

# Detener
docker-compose down
```

---

## 📊 DIFERENCIAS: DESARROLLO vs PRODUCCIÓN

| Aspecto | Desarrollo | Producción |
|---------|-----------|-----------|
| **Archivo** | docker-compose.yml | docker-compose.prod.yml |
| **DDL-Auto** | create-drop | validate |
| **Cache BD** | No | Sí (32M) |
| **Buffer Pool** | Auto | 1.4 GB |
| **Max Connections** | 500 | 500 |
| **Logs** | JSON (10m, 3 archivos) | JSON (50m, 10 archivos) |
| **Memory Limit** | No limitado | 2 GB |
| **CPU Limit** | No limitado | 2 cores |
| **Health Check** | Básico | Avanzado |

---

## 🔐 CONFIGURACIÓN DE PRODUCCIÓN

### Variables de entorno (.env)

```bash
# BD
DB_ROOT_PASSWORD=tu_contraseña_segura
DB_NAME=mourosubv2
DB_USER=appuser
DB_PASSWORD=otra_contraseña_segura

# Spring
SPRING_PROFILES_ACTIVE=prod
JPA_DDL_AUTO=validate

# Java
JAVA_OPTS=-XX:+UseG1GC -XX:MaxRAMPercentage=70.0
```

### Estructura de directorios en Debian

```
/home/tu_usuario/mourosubv2/
├── src/
├── pom.xml
├── Dockerfile*
├── docker-compose*.yml
├── deploy.sh
├── .env                    ← CREAR DESDE .env.example
├── data/
│   ├── mariadb/           ← Datos BD (persistentes)
│   └── logs/
│       ├── app/           ← Logs aplicación
│       └── mariadb/       ← Logs BD
├── backups/               ← Backups automáticos
└── config/
    └── mariadb.cnf        ← Configuración BD
```

---

## 🔄 FLUJO TÍPICO EN DEBIAN

### Primera ejecución (Instalación)

```bash
# 1. Descargar
git clone <repo> && cd MouroSubV2

# 2. Configurar
cp .env.example .env
nano .env                # Cambiar contraseñas

# 3. Construir
chmod +x deploy.sh
sudo ./deploy.sh
# Seleccionar opción 1 (Instalación inicial)

# 4. Verificar
docker-compose -f docker-compose.prod.yml ps

# 5. Acceder
# http://localhost:8080
# admin / admin123
```

### Operación diaria

```bash
# Ver estado
docker-compose -f docker-compose.prod.yml ps

# Ver logs
docker-compose -f docker-compose.prod.yml logs app -f

# Backup
sudo ./deploy.sh
# Opción 6 o 7

# Restart (si necesario)
docker-compose -f docker-compose.prod.yml restart
```

---

## 🐛 SOLUCIÓN DE PROBLEMAS EN DEBIAN

### Puerto 8080 ocupado

```bash
# Encontrar proceso
sudo lsof -i :8080
sudo netstat -tulpn | grep 8080

# Cambiar puerto en docker-compose
# Cambiar: ports: - "9000:8080"
docker-compose restart
```

### Permiso denegado con Docker

```bash
# Opción 1: Usar sudo
sudo docker-compose up -d

# Opción 2: Agregar usuario a grupo docker
sudo usermod -aG docker $USER
newgrp docker
docker ps  # Verificar
```

### MariaDB no inicia

```bash
# Ver logs
docker-compose logs mariadb

# Verificar volumen
docker volume ls | grep mourosubv2

# Reiniciar (Atención: borra datos)
docker-compose down -v
docker-compose up -d
```

### Conectarse a MariaDB desde Debian

```bash
# Opción 1: Desde dentro del contenedor
docker exec -it mourosubv2-mariadb-prod mariadb -u root -p<PASSWORD>

# Opción 2: Instalar cliente MariaDB en Debian
sudo apt install mariadb-client
mariadb -h localhost -u root -p<PASSWORD>

# Opción 3: Usar deploy.sh
sudo ./deploy.sh
# Opción 10 (Conectar a MariaDB)
```

---

## 📦 BACKUP Y RESTAURACIÓN

### Backup automático (desde deploy.sh)

```bash
sudo ./deploy.sh
# Opción 6: Backup SQL
# Opción 7: Backup volumen

# Los backups se guardan en: ./backups/
ls -lh ./backups/
```

### Backup manual

```bash
# SQL dump
docker exec mourosubv2-mariadb-prod mariadb-dump \
  -u root -p<PASSWORD> mourosubv2 > backup.sql

# Restaurar
docker exec -i mourosubv2-mariadb-prod mariadb \
  -u root -p<PASSWORD> mourosubv2 < backup.sql
```

---

## 📊 MONITOREO EN DEBIAN

### Ver consumo de recursos

```bash
# En tiempo real
docker stats

# Solo aplicación
docker stats mourosubv2-app-prod

# Solo BD
docker stats mourosubv2-mariadb-prod
```

### Ver logs con filtros

```bash
# Úlitmas 50 líneas
docker-compose logs app --tail=50

# Desde hace 5 minutos
docker-compose logs app --since 5m

# Guardar en archivo
docker-compose logs app > app_log.txt tail -f

# Logs de error
docker-compose logs app | grep -i error
```

---

## 🔐 SEGURIDAD PARA PRODUCCIÓN

### 1. Cambiar contraseñas

```bash
# Editar .env
nano .env

# Cambiar:
DB_ROOT_PASSWORD=contraseña_fuerte_123
DB_PASSWORD=contraseña_fuerte_456

# Aplicar
docker-compose down
docker-compose up -d
```

### 2. Firewall (ufw en Debian)

```bash
# Habilitar firewall
sudo ufw enable

# Permitir SSH
sudo ufw allow 22/tcp

# Permitir HTTP
sudo ufw allow 80/tcp

# Permitir HTTPS
sudo ufw allow 443/tcp

# Puerto 8080 solo internamente
sudo ufw allow from 127.0.0.1 to 127.0.0.1 port 8080

# Estado
sudo ufw status verbose
```

### 3. HTTPS con Nginx (opcional)

```bash
# Instalar Nginx
sudo apt install nginx certbot python3-certbot-nginx

# Obtener certificado Let's Encrypt
sudo certbot certonly --standalone -d tu-dominio.com

# Configurar proxy en Nginx
# /etc/nginx/sites-enabled/mourosubv2.conf
# (información en DOCKER_DEBIAN.md)
```

---

## 🎯 OPTIMIZACIÓN PARA DEBIAN

### Memoria

```bash
# Limitar a 70% de RAM disponible
JAVA_OPTS=-XX:+UseG1GC -XX:MaxRAMPercentage=70.0

# Ver disponible
free -h

# Cambiar en .env y reiniciar
```

### Almacenamiento

```bash
# Espacio total
df -h

# Tamaño de contenedores
docker system df

# Limpiar imágenes no usadas
docker system prune -a
```

### CPU

```bash
# Ver cores
nproc

# Limitar a 2 cores en docker-compose.prod.yml
deploy:
  resources:
    limits:
      cpus: '2'
```

---

## 🔄 AUTO-ARRANQUE EN DEBIAN

### Crear servicio systemd

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
WorkingDirectory=/home/tu_usuario/MouroSubV2
ExecStart=/usr/bin/docker-compose -f docker-compose.prod.yml up
ExecStop=/usr/bin/docker-compose -f docker-compose.prod.yml down
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Activar:
```bash
# Recargar systemd
sudo systemctl daemon-reload

# Habilitar
sudo systemctl enable mourosubv2

# Iniciar
sudo systemctl start mourosubv2

# Ver estado
sudo systemctl status mourosubv2

# Ver logs
journalctl -u mourosubv2 -f
```

---

## ✅ CHECKLIST FINAL

- [ ] Docker instalado y funcionando
- [ ] Docker Compose v2.0+
- [ ] Proyecto descargado/clonado
- [ ] .env creado desde .env.example
- [ ] Contraseñas cambiadas en .env
- [ ] Scripts (run.sh, deploy.sh) ejecutables
- [ ] Primera ejecución exitosa
- [ ] Acceso a http://localhost:8080
- [ ] Login funciona
- [ ] BD conectada y funcionando
- [ ] Backups automáticos configurados (si producción)
- [ ] Firewall configurado (si producción)
- [ ] Auto-arranque configurado (si producción)

---

## 📞 REFERENCIAS

- **Docker en Debian:** https://docs.docker.com/engine/install/debian/
- **Docker Compose:** https://docs.docker.com/compose/
- **MariaDB:** https://mariadb.com/kb/
- **Spring Boot:** https://spring.io/projects/spring-boot
- **Ufw Firewall:** https://wiki.ubuntu.com/UncomplicatedFirewall

---

## 🎉 ¡LISTO!

Tu aplicación MouroSubV2 está completamente configurada para **Debian 13** con:

✅ Dockerfile optimizado (multi-stage)  
✅ Docker Compose para desarrollo y producción  
✅ Scripts automáticos (run.sh, deploy.sh)  
✅ Configuración MariaDB producción-ready  
✅ Variables de entorno configurables  
✅ Backup y restauración automática  
✅ Documentación completa  

**Próximo paso:** Ejecuta `./run.sh` o `sudo ./deploy.sh` 🚀

