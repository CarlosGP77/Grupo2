# Instrucciones de Instalación y Ejecución - MouroSubV2

## 1. Requisitos Previos

### Para Docker (Recomendado)
- Docker instalado (https://www.docker.com/products/docker-desktop)
- Docker Compose instalado
- Al menos 4GB de RAM disponible

### Para Ejecución Local (Sin Docker)
- Java 21 JDK instalado
- Maven 3.8.0 o superior
- MariaDB 11 instalado y ejecutándose

## 2. Ejecución con Docker (Recomendado)

### En Windows
```bash
# Opción 1: Ejecutar el script batch
run.bat

# Opción 2: Ejecutar manualmente
docker-compose up -d
```

### En Mac/Linux
```bash
# Opción 1: Ejecutar el script bash
chmod +x run.sh
./run.sh

# Opción 2: Ejecutar manualmente
docker-compose up -d
```

### Verificar que está ejecutándose
```bash
docker-compose ps
```

### Acceder a la aplicación
```
http://localhost:8080
```

### Detener la aplicación
```bash
docker-compose down
```

### Ver logs en tiempo real
```bash
docker-compose logs -f
```

## 3. Ejecución Local (sin Docker)

### Paso 1: Preparar la Base de Datos MariaDB

#### Windows (con MySQL/MariaDB instalado)
```sql
-- Abrir cliente MySQL
mysql -u root -p

-- Ejecutar
CREATE DATABASE mourosubv2;
CREATE USER 'mouro_user'@'localhost' IDENTIFIED BY 'mouro_pass';
GRANT ALL PRIVILEGES ON mourosubv2.* TO 'mouro_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Mac/Linux
```bash
mysql -u root -p
```

Luego ejecutar las mismas sentencias SQL

### Paso 2: Compilar el Proyecto

```bash
cd F:\VS Code\Java\MouroSubV2
mvn clean install
```

### Paso 3: Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

### Paso 4: Acceder a la Aplicación

```
http://localhost:8080
```

## 4. Credenciales de Prueba

### Administrador
- **Usuario:** admin
- **Contraseña:** admin123
- **Rol:** ADMIN
- **Acceso:** Panel de administración completo

### Verificador
- **Usuario:** verificador
- **Contraseña:** verificador123
- **Rol:** VERIFICADOR
- **Acceso:** Verificación de usuarios y titulaciones

### Usuario Normal
- **Usuario:** usuario1
- **Contraseña:** usuario123
- **Rol:** USUARIO
- **Estado:** PENDIENTE (requiere verificación)

### Usuario Normal (Verificado)
- **Usuario:** usuario2
- **Contraseña:** usuario123
- **Rol:** USUARIO
- **Estado:** VERIFIED

## 5. Funcionalidades Disponibles

### Panel de Administración (/admin)
- Dashboard con estadísticas
- Gestión de usuarios
- Gestión de cursos
- Gestión de actividades
- Gestión de ubicaciones
- Gestión de reservas

### Panel de Verificador (/verificador)
- Dashboard con tareas pendientes
- Revisión de usuarios sin verificar
- Verificación de titulaciones
- Cambio de estado de usuarios

### Panel de Usuario (/user)
- Dashboard personal
- Ver perfil
- Mis reservas
- Mis titulaciones
- Añadir titulaciones

### Funcionalidades Generales
- Catálogo de cursos (/courses)
- Listado de actividades (/activities)
- Listado de ubicaciones (/locations)
- Reservas de actividades
- Gestión de titulaciones

## 6. Solución de Problemas

### Puerto 8080 en uso
```bash
# Cambiar el puerto en application.yml
server:
  port: 8081
```

### MariaDB no conecta
```bash
# Verificar que MariaDB está ejecutándose
# Windows: Services (servicios) -> MariaDB
# Mac/Linux: sudo systemctl status mariadb
```

### Docker no funciona
```bash
# Reiniciar Docker
docker-compose down
docker-compose up --build
```

### Limpiar completamente
```bash
docker-compose down -v
docker system prune -a
docker-compose up -d
```

## 7. Estructura de Carpetas

```
MouroSubV2/
├── src/main/java/org/example/
│   ├── Main.java
│   ├── model/              (Entidades JPA)
│   ├── repository/         (Spring Data JPA)
│   ├── service/            (Lógica de negocio)
│   ├── controller/         (Controladores)
│   └── config/             (Configuraciones)
├── src/main/resources/
│   ├── application.yml     (Configuración)
│   └── templates/          (Vistas Thymeleaf)
├── pom.xml                 (Dependencias Maven)
├── Dockerfile
├── docker-compose.yml
├── run.sh
├── run.bat
└── README.md
```

## 8. Desarrollo y Debugging

### Logs de la aplicación (Docker)
```bash
docker-compose logs app -f
```

### Logs de MariaDB (Docker)
```bash
docker-compose logs mariadb -f
```

### Connecting a MariaDB (desde terminal)
```bash
docker exec -it mourosubv2-mariadb mariadb -u root -p
```

Contraseña: `root`

## 9. Próximos Pasos

1. **Explorar la aplicación:**
   - Login con diferentes roles
   - Crear nuevo usuario y esperar verificación
   - Hacer reservas en actividades
   - Agregar titulaciones

2. **Personalizar:**
   - Modificar vistas en `templates/`
   - Agregar más funcionalidades en servicios
   - Cambiar colores y estilos

3. **Producción:**
   - Configurar HTTPS
   - Usar variables de entorno
   - Implementar backups de BD
   - Configurar logging avanzado

## 10. Documentación Adicional

- **Spring Boot:** https://spring.io/projects/spring-boot
- **Spring Security:** https://spring.io/projects/spring-security
- **Thymeleaf:** https://www.thymeleaf.org/
- **MariaDB:** https://mariadb.com/kb/
- **Docker:** https://docs.docker.com/

---

**¿Necesitas ayuda?** Revisa README.md para la documentación completa

