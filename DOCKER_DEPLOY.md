# Despliegue en Docker - Grupo2 App

## ✅ Servicios Configurados

Este `docker-compose.yml` está **100% configurado para Docker** con:

- **MariaDB 11** - Base de datos
- **Nextcloud** - Almacenamiento WebDAV para imágenes
- **Grupo2 App** - Aplicación Spring Boot con Verificador de Imágenes

## 🚀 Inicio Rápido

### 1. **Clonar/Actualizar el Código**
```bash
git pull origin main
```

### 2. **Construir y Desplegar**
```bash
docker-compose up -d --build
```

### 3. **Verificar que todo está corriendo**
```bash
docker-compose ps
```

Deberías ver:
```
CONTAINER ID   IMAGE                    NAMES
xxxxx          mariadb:11               grupo2-mariadb
xxxxx          nextcloud:latest         grupo2-nextcloud
xxxxx          grupo2:latest            grupo2-app
```

### 4. **Acceder a la Aplicación**
- **Aplicación**: http://localhost:8080
- **Verificador**: http://localhost:8080/verificador.html
- **Nextcloud**: http://localhost:8888 (admin / admin_123)

## 📊 URLs de Servicios

```
Aplicación         http://localhost:8080
Verificador        http://localhost:8080/verificador.html
API REST           http://localhost:8080/api/verificador/*
Nextcloud (UI)     http://localhost:8888
MariaDB            localhost:3306
```

## 🔑 Credenciales por Defecto

```
Nextcloud Admin:
  Usuario: admin
  Contraseña: admin_123

Database:
  Usuario: root
  Contraseña: admin_123
  Base de datos: mourosub
```

## 📁 Estructura de Carpetas en Nextcloud

Al subir imágenes, se crearán en:
```
/Verificador/imagenes/
```

## 🐳 Comandos Docker Útiles

### Ver logs de la aplicación
```bash
docker-compose logs -f grupo2-app
```

### Ver logs de la BD
```bash
docker-compose logs -f grupo2-mariadb
```

### Ver logs de Nextcloud
```bash
docker-compose logs -f grupo2-nextcloud
```

### Detener todos los servicios
```bash
docker-compose down
```

### Detener y eliminar volúmenes (CUIDADO: elimina datos)
```bash
docker-compose down -v
```

### Reiniciar un servicio específico
```bash
docker-compose restart grupo2-app
```

## 📝 Variables de Entorno

Todas las variables de entorno están en `.env`:

```env
DB_HOST=mariadb
DB_PORT=3306
DB_NAME=mourosub
DB_USERNAME=root
DB_PASSWORD=admin_123

SERVER_PORT=8080
SERVER_SSL_ENABLED=false

NEXTCLOUD_PASSWORD=admin_123
```

Para cambiar contraseñas, edita `.env` y ejecuta:
```bash
docker-compose up -d --build
```

## ✅ API REST Verificador

```
POST   /api/verificador/upload           Subir imagen
GET    /api/verificador/pendientes       Listar por verificar
GET    /api/verificador/verificadas      Listar verificadas
POST   /api/verificador/verificar/{id}   Verificar imagen
GET    /api/verificador/ver/{id}         Ver imagen
GET    /api/verificador/descargar/{id}   Descargar imagen
DELETE /api/verificador/{id}             Eliminar imagen
GET    /api/verificador/estadisticas     Estadísticas
GET    /api/verificador/health           Health check
```

## 🔧 Troubleshooting

### La aplicación no conecta a la BD
```bash
docker-compose logs grupo2-mariadb
docker-compose logs grupo2-app
```

### Nextcloud no está disponible
```bash
docker-compose restart grupo2-nextcloud
```

### Puerto 8080 en uso
Cambia en `.env`:
```env
SERVER_PORT=8081
```

Y en `docker-compose.yml`:
```yaml
ports:
  - "8081:8080"
```

### Recrear volúmenes
```bash
docker-compose down -v
docker-compose up -d --build
```

## 📋 Checklist de Despliegue

- [ ] `docker-compose.yml` con MariaDB, Nextcloud y App
- [ ] `.env` con credenciales configuradas
- [ ] `application.properties` con variables de entorno
- [ ] `Dockerfile` exponiendo puerto 8080
- [ ] `pom.xml` con dependencias necesarias (commons-codec, JPA, etc.)
- [ ] Clases Java del Verificador creadas
- [ ] HTML `/verificador.html` en static
- [ ] Base de datos creada automáticamente en MariaDB
- [ ] Nextcloud iniciado y accesible

## 🎯 Próximos Pasos

1. Ejecutar: `docker-compose up -d --build`
2. Esperar ~1-2 min a que inicie todo
3. Acceder a http://localhost:8080/verificador.html
4. Subir una imagen de prueba
5. Verificarla en el tab "Pendientes"

## 📞 Soporte

Si tienes problemas:
1. Revisa los logs: `docker-compose logs -f`
2. Verifica que todos los contenedores estén corriendo: `docker-compose ps`
3. Reinicia todo: `docker-compose down && docker-compose up -d --build`
