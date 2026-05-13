# MouroSubV2 - Sistema de Gestión de Cursos

## Descripción General

**MouroSubV2** es una aplicación web moderna para gestionar usuarios, cursos, actividades, ubicaciones, reservas y verificación de titulaciones. Está construida con **Spring Boot 3.3**, **Spring Security**, **Thymeleaf**, **MariaDB** y **Docker**.

## Arquitectura

La aplicación sigue una **arquitectura limpia por capas**:

```
org.example/
├── model/           # Entidades JPA (User, Course, Activity, Location, Reservation, Qualification)
├── repository/      # Spring Data JPA Repositories
├── service/         # Lógica de negocio
├── controller/      # Controladores REST/Web
└── config/          # Configuraciones (Security, Database, Data Initialization)
```

## Roles y Permisos

### 1. **ADMIN** - Administrador
- Acceso completo al panel de administración
- Ver estadísticas del sistema
- Gestionar usuarios, cursos, actividades, ubicaciones y reservas
- Supervisión general del sistema

**Credenciales de prueba:**
- Usuario: `admin`
- Contraseña: `admin123`

### 2. **VERIFICADOR** - Verificador de Titulaciones
- Panel de verificación exclusivo
- Revisar usuarios no verificados
- Ver detalles de usuarios
- Cambiar estado de verificación (PENDIENTE → VERIFICADO/RECHAZADO)
- Verificar titulaciones/cualificaciones de usuarios

**Credenciales de prueba:**
- Usuario: `verificador`
- Contraseña: `verificador123`

### 3. **USUARIO** - Usuario Regular
- Registro e inicio de sesión
- Ver catálogo de cursos y actividades
- Hacer reservas en actividades
- Gestionar sus propias reservas
- Añadir y gestionar titulaciones
- Ver ubicaciones disponibles

**Credenciales de prueba:**
- Usuario: `usuario1`
- Contraseña: `usuario123`

## Entidades Principales

### User (Usuario)
- `id` - ID único
- `username` - Nombre de usuario
- `password` - Contraseña (encriptada con BCrypt)
- `email` - Correo electrónico
- `firstName`, `lastName` - Nombre y apellido
- `role` - Rol (ADMIN, VERIFICADOR, USUARIO)
- `verificationStatus` - Estado de verificación (PENDING, VERIFIED, REJECTED)
- `enabled` - Cuenta activa/inactiva
- `createdAt` - Fecha de creación
- Relaciones: reservas y titulaciones

### Course (Curso)
- `id` - ID único
- `code` - Código único del curso
- `name` - Nombre del curso
- `description` - Descripción
- `duration` - Duración en horas
- `price` - Precio del curso
- `createdAt` - Fecha de creación
- Relaciones: actividades

### Activity (Actividad)
- `id` - ID único
- `name` - Nombre de la actividad
- `description` - Descripción
- `startDate`, `endDate` - Fechas de inicio y fin
- `capacity` - Capacidad máxima
- `course` - Curso asociado
- `location` - Ubicación
- `createdAt` - Fecha de creación
- Relaciones: reservas

### Location (Ubicación)
- `id` - ID único
- `name` - Nombre de la ubicación
- `description` - Descripción
- `address`, `city`, `postalCode` - Dirección
- `capacity` - Capacidad total
- `createdAt` - Fecha de creación
- Relaciones: actividades

### Reservation (Reserva)
- `id` - ID único
- `user` - Usuario que reserva
- `activity` - Actividad reservada
- `confirmed` - Si está confirmada
- `createdAt` - Fecha de creación
- Restricción única: Un usuario solo puede tener una reserva por actividad

### Qualification (Titulación)
- `id` - ID único
- `user` - Usuario propietario
- `title` - Título de la titulación
- `issuer` - Institución que expide
- `issueDate` - Fecha de expedición
- `description` - Descripción
- `verified` - Verificada por un VERIFICADOR
- `createdAt` - Fecha de creación

## Cómo Ejecutar el Proyecto

### Opción 1: Con Docker (Recomendado)

**Requisitos:**
- Docker instalado
- Docker Compose instalado

**Pasos:**

```bash
# 1. Navega al directorio del proyecto
cd F:\VS Code\Java\MouroSubV2

# 2. Construye y ejecuta los contenedores
docker-compose up -d

# 3. La aplicación estará disponible en:
# http://localhost:8080

# 4. Detener los contenedores
docker-compose down
```

### Opción 2: Ejecución Local (sin Docker)

**Requisitos:**
- Java 21 instalado
- Maven instalado
- MariaDB 11 instalado y ejecutándose

**Configuración de MariaDB:**
```sql
CREATE DATABASE mourosubv2;
CREATE USER 'mouro_user'@'localhost' IDENTIFIED BY 'mouro_pass';
GRANT ALL PRIVILEGES ON mourosubv2.* TO 'mouro_user'@'localhost';
FLUSH PRIVILEGES;
```

**Pasos:**

```bash
# 1. Navega al directorio del proyecto
cd F:\VS Code\Java\MouroSubV2

# 2. Compila el proyecto
mvn clean install

# 3. Ejecuta la aplicación
mvn spring-boot:run

# 4. La aplicación estará disponible en:
# http://localhost:8080
```

## Archivo application.yml

```yaml
spring:
  application:
    name: MouroSubV2
  datasource:
    url: jdbc:mariadb://mariadb:3306/mourosubv2
    username: root
    password: root
    driver-class-name: org.mariadb.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MariaDBDialect
  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html

server:
  port: 8080
```

## Flujo de Uso

### 1. Registro de Usuario
1. Acceder a http://localhost:8080/register
2. Completar los datos (nombre, apellido, usuario, email, contraseña)
3. El usuario se crea con rol **USUARIO** y estado **PENDIENTE**
4. El **VERIFICADOR** debe verificar al usuario

### 2. Login
1. Acceder a http://localhost:8080/login
2. Introducir usuario y contraseña
3. Redirigirse al dashboard según el rol

### 3. Panel de Admin
- Ver estadísticas del sistema
- Gestionar usuarios, cursos, actividades y ubicaciones
- Monitorear reservas

### 4. Panel de Verificador
- Revisar usuarios con estado PENDIENTE
- Cambiar estado a VERIFICADO o RECHAZADO
- Verificar titulaciones de usuarios

### 5. Panel de Usuario
- Ver cursos y actividades disponibles
- Realizar reservas
- Añadir titulaciones
- Ver sus propias reservas y titulaciones

## Decisiones Técnicas

### 1. **Spring Boot 3.3**
- Última versión estable
- Soporte para Java 21
- Mejoras en rendimiento y seguridad

### 2. **Spring Security**
- Autenticación basada en usuarios de la BD
- Autorización basada en roles (ROLE_ADMIN, ROLE_VERIFICADOR, ROLE_USUARIO)
- Encriptación de contraseñas con BCrypt

### 3. **Spring Data JPA**
- Eliminación de SQL boilerplate
- Queries derivadas de nombres de métodos
- Transacciones automáticas

### 4. **Thymeleaf**
- Integración nativa con Spring Security
- Soporte para el atributo `sec:authorize`
- Rendimiento optimizado

### 5. **MariaDB**
- Base de datos relacional robusta
- Soporte completo para JPA
- Fácil de ejecutar en Docker

### 6. **Docker & Docker Compose**
- Reproducibilidad en cualquier máquina
- Aislamiento de servicios
- Configuración simple con YAML

### 7. **Lombok**
- Reducción de código boilerplate (@Data, @Builder, etc.)
- Mejor legibilidad del código

## Estructura de Carpetas Finales

```
MouroSubV2/
├── src/
│   └── main/
│       ├── java/org/example/
│       │   ├── Main.java (Application entry point)
│       │   ├── model/             (Entidades JPA)
│       │   │   ├── User.java
│       │   │   ├── Course.java
│       │   │   ├── Activity.java
│       │   │   ├── Location.java
│       │   │   ├── Reservation.java
│       │   │   ├── Qualification.java
│       │   │   ├── UserRole.java (Enum)
│       │   │   └── VerificationStatus.java (Enum)
│       │   ├── repository/        (Spring Data JPA)
│       │   │   ├── UserRepository.java
│       │   │   ├── CourseRepository.java
│       │   │   ├── ActivityRepository.java
│       │   │   ├── LocationRepository.java
│       │   │   ├── ReservationRepository.java
│       │   │   └── QualificationRepository.java
│       │   ├── service/           (Lógica de negocio)
│       │   │   ├── UserService.java
│       │   │   ├── CourseService.java
│       │   │   ├── ActivityService.java
│       │   │   ├── LocationService.java
│       │   │   ├── ReservationService.java
│       │   │   ├── QualificationService.java
│       │   │   └── CustomUserDetailsService.java
│       │   ├── controller/        (Controladores)
│       │   │   ├── AuthController.java
│       │   │   ├── AdminController.java
│       │   │   ├── VerificadorController.java
│       │   │   ├── UserController.java
│       │   │   ├── CourseController.java
│       │   │   ├── ActivityController.java
│       │   │   ├── LocationController.java
│       │   │   ├── ReservationController.java
│       │   │   └── QualificationController.java
│       │   └── config/            (Configuraciones)
│       │       ├── SecurityConfig.java
│       │       └── DataInitializer.java
│       └── resources/
│           ├── application.yml
│           └── templates/         (Vistas Thymeleaf)
│               ├── index.html
│               ├── auth/
│               │   ├── login.html
│               │   └── register.html
│               ├── dashboard/
│               │   └── index.html
│               ├── admin/
│               │   ├── dashboard.html
│               │   ├── users.html
│               │   ├── courses.html
│               │   └── ...
│               ├── verificador/
│               │   ├── dashboard.html
│               │   ├── users.html
│               │   └── qualifications.html
│               ├── user/
│               │   ├── dashboard.html
│               │   ├── profile.html
│               │   └── add-qualification.html
│               ├── courses/
│               ├── activities/
│               ├── locations/
│               ├── reservations/
│               └── qualifications/
├── pom.xml
├── docker-compose.yml
├── Dockerfile
└── README.md
```

## Características Implementadas

✅ Autenticación con login/logout
✅ Autorización basada en roles
✅ Panel de administración con estadísticas
✅ Panel de verificación de usuarios y titulaciones
✅ Gestión de cursos, actividades y ubicaciones
✅ Sistema de reservas
✅ Gestión de titulaciones
✅ Vistas Thymeleaf con Bootstrap
✅ Base de datos relacional con JPA
✅ Inicialización de datos de prueba
✅ Ejecución con Docker y Docker Compose
✅ Código limpio y bien estructurado

## Mejoras Futuras

- [ ] API REST adicional para móviles
- [ ] Notificaciones por email
- [ ] Búsqueda avanzada y filtros
- [ ] Exportar reportes en PDF
- [ ] Calificaciones y evaluaciones
- [ ] Chat en tiempo real
- [ ] Integración con calendario
- [ ] Sistema de pagos

## Soporte y Documentación

Para más información sobre Spring Boot:
- https://spring.io/projects/spring-boot
- https://spring.io/projects/spring-security

Para más información sobre Thymeleaf:
- https://www.thymeleaf.org/

## Licencia

Este proyecto es de código abierto para propósitos educativos.

---

**Última actualización:** Mayo 2024
**Versión:** 1.0.0

