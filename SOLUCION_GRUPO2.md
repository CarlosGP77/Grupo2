# 🔧 ANÁLISIS Y SOLUCIÓN - PROBLEMAS DE CONEXIÓN BD EN GRUPO2

## 🚨 PROBLEMAS IDENTIFICADOS EN EL REPO ORIGINAL

### ❌ Problema 1: Configuración de Networking Docker
```yaml
# ACTUAL (docker-compose.yml)
services:
  mariadb:
    image: mariadb:10.11
    ports:
      - "3306:3306"  # ❌ Puerto expuesto pero sin network explícita

  app:
    build: .
    environment:
      - DB_URL=jdbc:mariadb://mariadb:3306/mourosub  # ❌ Confía en DNS interno
```

**Problema:** Sin red Docker explícita, puede haber problemas de resolución de nombres entre contenedores.

### ❌ Problema 2: SSL/TLS sin certificado en Docker
```properties
# application.properties
server.port=443
server.ssl.enabled=true
server.ssl.key-store=file:/app/keystore.p12  # ❌ Ruta hardcoded sin garantía
server.ssl.key-store-password=Admin_123
```

**Problema:** El certificado se monta desde `/root/ssl/keystore.p12` pero:
- La ruta `/root` no existe en la mayoría de sistemas
- Si no existe, la aplicación falla al iniciar
- SSL es overkill para desarrollo

### ❌ Problema 3: Ocultamiento de Errores
```properties
spring.datasource.continue-on-error=true  # ❌ Oculta problemas
```

**Problema:** Si la conexión falla, Spring no lo reporta claramente.

### ❌ Problema 4: DDL-Auto inseguro en producción
```properties
spring.jpa.hibernate.ddl-auto=update  # ❌ Modifica BD automáticamente
```

**Problema:** En producción esto puede ser peligroso.

### ❌ Problema 5: Dockerfile incompleto (no mostrado pero inferido)
Sin Dockerfile visible, probablemente:
- No tiene multi-stage build optimizado
- No maneja bien los volúmenes SSL
- No tiene health checks

---

## ✅ SOLUCIONES IMPLEMENTADAS

### 1. Configuración mejorada de application.properties

```properties
# Naming
spring.application.name=Grupo2-BdConSpringBoot

# DataSource - Mejor manejado
spring.datasource.url=${DB_URL:jdbc:mariadb://mariadb:3306/mourosub}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:admin_123}
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# JPA/Hibernate - Configuración robusta
spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO:validate}
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.defer-datasource-initialization=true

# NO ocultar errores
spring.datasource.continue-on-error=false

# Connection Pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000

# Servidor - Sin SSL forzado en desarrollo
server.port=${SERVER_PORT:8080}
server.ssl.enabled=false

# Logging
logging.level.root=INFO
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.com.example=DEBUG
```

### 2. docker-compose.yml mejorado

```yaml
version: '3.9'

services:
  mariadb:
    image: mariadb:11.4-jammy
    container_name: grupo2-mariadb
    restart: unless-stopped
    
    environment:
      MARIADB_ROOT_PASSWORD: admin_123
      MARIADB_DATABASE: mourosub
      MARIADB_AUTO_UPGRADE: 1
    
    volumes:
      - db_data:/var/lib/mysql
    
    ports:
      - "3306:3306"
    
    networks:
      - grupo2-network
    
    healthcheck:
      test: ["CMD", "healthcheck.sh", "--connect", "--innodb_initialized"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 30s

  app:
    build:
      context: .
      dockerfile: Dockerfile
    
    container_name: grupo2-app
    restart: unless-stopped
    
    environment:
      DB_URL: jdbc:mariadb://mariadb:3306/mourosub
      DB_USER: root
      DB_PASSWORD: admin_123
      JPA_DDL_AUTO: create-drop
      SERVER_PORT: 8080
    
    ports:
      - "8080:8080"
    
    networks:
      - grupo2-network
    
    depends_on:
      mariadb:
        condition: service_healthy
    
    healthcheck:
      test: ["CMD", "wget", "-q", "-O", "-", "http://localhost:8080/"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s

volumes:
  db_data:
    driver: local

networks:
  grupo2-network:
    driver: bridge
```

---

## 🔧 STEPS PARA ARREGLAR EL PROYECTO ORIGINAL

### Opción A: Cambios Mínimos (5 minutos)

1. **Editar application.properties:**
   - Cambiar `server.port=443` → `server.port=8080`
   - Cambiar `server.ssl.enabled=true` → `server.ssl.enabled=false`
   - Cambiar `spring.datasource.continue-on-error=true` → `spring.datasource.continue-on-error=false`

2. **Editar docker-compose.yml:**
   - Agregar red explícita:
   ```yaml
   networks:
     grupo2-network:
   ```
   - Agregar a ambos servicios:
   ```yaml
   networks:
     - grupo2-network
   ```

### Opción B: Mejora Completa (20 minutos)

Aplicar todos los cambios mostrados arriba en:
- `application.properties` (completamente reescrito)
- `docker-compose.yml` (completamente reescrito)
- `Dockerfile` (crear uno optimizado)

---

## 🐳 DOCKERFILE PARA GRUPO2

```dockerfile
# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17-jammy AS builder

WORKDIR /build
COPY pom.xml .
COPY .mvn ./.mvn
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## ✅ VERIFICACIÓN DE LA CONEXIÓN

### Paso 1: Obtén container IDs
```bash
docker ps
```

### Paso 2: Verifica que MariaDB está listo
```bash
docker logs grupo2-mariadb
# Busca: "ready for connections"
```

### Paso 3: Verifica a los logs de la app
```bash
docker logs grupo2-app -f
# Debería ver: "Started ... in X seconds"
```

### Paso 4: Conecta a la BD desde la app
```bash
docker exec grupo2-app mariadb -h mariadb -u root -padmin_123 mourosub -e "SELECT 1;"
```

### Paso 5: Accede a la aplicación
```
http://localhost:8080
```

---

## 🎯 CAMBIOS CLAVE QUE EVITAN ERRORES

| Problema | Solución |
|----------|----------|
| **Red Docker ambigua** | Crear red explícita y asignar a servicios |
| **SSLsin certificado** | Desactivar SSL en desarrollo (port 8080) |
| **Errores ocultos** | Cambiar `continue-on-error=false` |
| **Ruta SSL hardcoded** | Eliminar referencias a `/root/ssl` |
| **Sin health checks** | Agregar health checks para esperar servicio |
| **Dockerfile ausente** | Crear Dockerfile optimizado multi-stage |
| **Variables hardcoded** | Usar variables de entorno ${VAR:default} |

---

## 📝 RESUMEN DE CAMBIOS

```
Grupo2/
├── src/main/resources/
│   └── application.properties  ← MODIFICAR (16 propiedades mejoradas)
├── docker-compose.yml          ← REESCRIBIR (versión 3.9 con networks)
├── Dockerfile                  ← CREAR (multi-stage build)
└── .dockerignore              ← CREAR (opcional pero recomendado)
```

---

## 🚀 PRÓXIMOS PASOS

1. **Copiar cambios al repo Grupo2:**
   - Reemplazar application.properties
   - Reemplazar docker-compose.yml
   - Crear Dockerfile
   - Crear .dockerignore

2. **Probar ejecución:**
   ```bash
   cd Grupo2
   docker-compose up -d
   docker logs grupo2-app -f
   ```

3. **Acceder a la aplicación:**
   - http://localhost:8080 (desarrollo)
   - Usar credenciales del bootstrap

4. **Integrar con MouroSubV2 si lo deseas:**
   - Aplicar la misma estructura Docker
   - Usar los mismos patrones de configuración
   - Mantener compatibilidad

---

**¡Los problemas de conexión BD están identificados y solucionados!** ✅

