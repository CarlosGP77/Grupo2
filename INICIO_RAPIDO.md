# ⚡ INICIO RÁPIDO - MOUROSUBV2

## 🚀 Ejecutar en 1 minuto (Con Docker)

### Windows
```bash
cd F:\VS Code\Java\MouroSubV2
run.bat
```

### Mac/Linux
```bash
cd F:\VS Code\Java\MouroSubV2
./run.sh
```

### Manual
```bash
docker-compose up -d
```

✅ **Espera 30 segundos** → Accede a http://localhost:8080

---

## 🔐 Credenciales de Prueba

### Admin (Panel de Administración)
```
Usuario: admin
Contraseña: admin123
```

### Verificador (Panel de Verificación)
```
Usuario: verificador
Contraseña: verificador123
```

### Usuario Regular
```
Usuario: usuario1
Contraseña: usuario123
```

---

## 📱 URLs Principales

| URL | Descripción |
|-----|-------------|
| http://localhost:8080/ | Página principal |
| http://localhost:8080/login | Login |
| http://localhost:8080/register | Registro |
| http://localhost:8080/admin/dashboard | Panel Admin |
| http://localhost:8080/verificador/dashboard | Panel Verificador |
| http://localhost:8080/user/dashboard | Panel Usuario |
| http://localhost:8080/courses | Catálogo de cursos |
| http://localhost:8080/activities | Actividades disponibles |
| http://localhost:8080/locations | Ubicaciones |

---

## 🧪 Pruebas Sugeridas

### 1. Login como Admin
```
1. Ir a http://localhost:8080/login
2. Usuario: admin, Contraseña: admin123
3. Verás el panel de administración con estadísticas
```

### 2. Ver Cursos
```
1. Click en "Cursos" en la navegación
2. Verás el listado de cursos disponibles
3. Click en un curso para ver sus actividades
```

### 3. Ver Actividades
```
1. Click en "Actividades" en la navegación
2. Verás las próximas actividades programadas
3. Click en una actividad para ver detalles
```

### 4. Registrar Nuevo Usuario
```
1. Ir a http://localhost:8080/register
2. Llenar el formulario con datos nuevos
3. Hacer click en "Registrarse"
4. Intentar login con las nuevas credenciales
   (Nota: Usuario nuevo estará en estado PENDIENTE)
```

### 5. Verificar Usuario como Verificador
```
1. Login como verificador
2. Ir a "Panel de Verificación" → "Usuarios Pendientes"
3. Click en "Ver Detalles" del usuario nuevo
4. Seleccionar "Verificado" y hacer click en "Verificar"
5. El usuario ahora está verificado
```

### 6. Hacer una Reserva como Usuario
```
1. Login como usuario1 (está verificado)
2. Ir a "Actividades"
3. Click en una actividad
4. Click en botón "Reservar"
5. Ir a "Mis Reservas" para ver la reserva
```

### 7. Agregar Titulación
```
1. Login como usuario
2. Click en "Mi Perfil" o "Mis Titulaciones"
3. Click en "Añadir Titulación"
4. Llenar datos (título, institución, fecha)
5. Click en "Guardar Titulación"
```

### 8. Verificar Titulación como Verificador
```
1. Login como verificador
2. Ir a "Panel de Verificación" → "Titulaciones"
3. Click en "Verificar" para cada titulación pendiente
```

---

## 🛑 Detener la Aplicación

### Docker
```bash
docker-compose down
```

### Ver logs en tiempo real
```bash
docker-compose logs app -f
```

### Reiniciar completamente
```bash
docker-compose down -v
docker-compose up -d
```

---

## 🐛 Problemática Común

### Puerto 8080 ya está en uso
```bash
# Solución: Cambiar puerto en application.yml
server:
  port: 8081
```

### MariaDB no conecta
```bash
# Solución: Reiniciar Docker
docker-compose down -v
docker-compose up -d --build
```

### Base de datos vacía
```bash
# Es normal - Los datos se cargan automáticamente al iniciar
# Si no ves datos, revisa logs: docker-compose logs app
```

---

## 📁 Archivos Principales a Explorar

### Empezar por aquí:
1. **README.md** - Documentación completa
2. **RESUMEN_PROYECTO.md** - Arquitectura y decisiones técnicas
3. **ESTRUCTURA_ARCHIVOS.md** - Ubicación de cada componente

### Entender el código:
```
src/main/java/org/example/
├── model/              ← Las entidades (datos)
├── repository/         ← Acceso a datos
├── service/            ← Lógica de negocio
├── controller/         ← Manejo de peticiones
└── config/             ← Configuraciones de seguridad
```

### Modificar vistas:
```
src/main/resources/templates/
└── *.html              ← Las páginas web (Bootstrap 5)
```

---

## 🎓 Learning Path Recomendado

### Nivel 1: Exploración (Día 1)
- [ ] Ejecutar la aplicación con Docker
- [ ] Probar login con diferentes roles
- [ ] Explorar cada sección (Admin, Verificador, Usuario)
- [ ] Leer README.md

### Nivel 2: Entendimiento (Día 2-3)
- [ ] Leer RESUMEN_PROYECTO.md
- [ ] Entender la arquitectura (Model → Repository → Service → Controller)
- [ ] Ubicar los 6 servicios principales
- [ ] Entender el flujo de autenticación

### Nivel 3: Modificación (Día 4-5)
- [ ] Cambiar colores en vistas HTML
- [ ] Agregar nuevos campos a un formulario
- [ ] Crear un nuevo endpoint simple
- [ ] Agregar datos iniciales

### Nivel 4: Extensión (Semana 2)
- [ ] Crear nueva entidad
- [ ] Crear nuevo servicio
- [ ] Crear nuevo controlador
- [ ] Agregar nuevas vistas

---

## 💡 Tips de Desarrollo

### IDE (IntelliJ IDEA)
```
Ctrl+Shift+F    → Buscar en todo el proyecto
Ctrl+N          → Ir a clase
Alt+Left        → Volver atrás
F7              → En debug, entrar en método
F8              → Siguiente línea en debug
```

### Git (Control de versiones)
```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin <tu-repo>
git push -u origin main
```

### Maven (Local sin Docker)
```bash
mvn spring-boot:run      # Ejecutar
mvn clean install        # Compilar
mvn test                 # Tests
```

---

## 🔧 Estructura de Seguridad (para entender)

```
FLUJO DE LOGIN:
├── Usuario entra credenciales
├── AuthenticationManager.authenticate()
├── CustomUserDetailsService.loadUserByUsername()
├── Compare contraseña con BCrypt
├── Si OK → Spring crea sesión HTTP
├── Si error → Redirect a /login?error
└── Cada request valida la sesión

CONTROL DE ACCESO:
├── SecurityConfig.filterChain() define roles requeridos
├── @PreAuthorize valida permisos en métodos
├── Thymeleaf sec:authorize muestra/oculta elementos
└── Si sin privilegios → 403 Forbidden
```

---

## 📊 Base de Datos (Visualización)

```
USUARIOS
├── id, username, password (encriptada), email
├── firstName, lastName, role (ADMIN/VERIFICADOR/USUARIO)
├── verificationStatus (PENDING/VERIFIED/REJECTED)
└── Relaciones: reservations[], qualifications[]

CURSOS
├── id, code, name, description, duration, price
└── Relaciones: activities[]

UBICACIONES
├── id, name, description, address, city, capacity
└── Relaciones: activities[]

ACTIVIDADES
├── id, name, startDate, endDate, capacity
├── course_id (FK), location_id (FK)
└── Relaciones: reservations[]

RESERVAS
├── id, user_id (FK), activity_id (FK), confirmed
└── Restricción: (user, activity) es única

TITULACIONES
├── id, user_id (FK), title, issuer, issueDate, verified
└── Para verificación en panel de verificador
```

---

## 🎯 Qué Hace Cada Rol

### ADMIN (/admin)
```
Ver estadísticas del sistema
  ↓
Gestionar usuarios (crear, editar, cambiar rol)
  ↓
Gestionar cursos, actividades, ubicaciones
  ↓
Monitorear reservas
  ↓
Supervisión general
```

### VERIFICADOR (/verificador)
```
Ver tareas pendientes
  ↓
Revisar usuarios sin verificar
  ↓
Cambiar estado (VERIFIED/REJECTED)
  ↓
Verificar titulaciones de usuarios
  ↓
Control de calidad de usuarios
```

### USUARIO (/user)
```
Ver catálogo de cursos
  ↓
Ver actividades programadas
  ↓
Reservar actividades
  ↓
Agregar titulaciones
  ↓
Gestionar propios datos
```

---

## ✨ Funcionalidades Principales

✅ Autenticación por usuario/contraseña
✅ Autorización basada en roles
✅ Encriptación BCrypt
✅ Gestión de usuarios (CRUD)
✅ Gestión de cursos (CRUD)
✅ Gestión de actividades (CRUD)
✅ Gestión de ubicaciones (CRUD)
✅ Sistema de reservas
✅ Gestión de titulaciones
✅ Panel de administración
✅ Panel de verificación
✅ Inicialización automática de datos
✅ UI responsivo (Bootstrap)
✅ Base de datos MariaDB
✅ Docker ready

---

## 🎬 Video Mental: Primer Minuto

```
1. Ejecutas: docker-compose up -d
   ↓
2. Esperas 30 segundos
   ↓
3. Accedes: http://localhost:8080
   ↓
4. Ves: Página principal bonita (hero section)
   ↓
5. Haces click: "Iniciar Sesión"
   ↓
6. Escribes: admin / admin123
   ↓
7. Ves: Panel admin con estadísticas
   ↓
8. Exploras: Usuarios, Cursos, Actividades, Ubicaciones
```

---

## 🎉 ¡FELICIDADES!

Tu aplicación Spring Boot está lista para:
- ✅ Ejecutar
- ✅ Probar
- ✅ Modificar
- ✅ Extender
- ✅ Desplegar

**Tiempo para tener todo en funcionamiento: < 1 minuto**

---

## 📞 Soporte

Para dudas sobre:
- **Spring Boot** → https://spring.io
- **Thymeleaf** → https://www.thymeleaf.org
- **Docker** → https://docs.docker.com
- **MariaDB** → https://mariadb.com

## 📝 Checklist After First Run

- [ ] Aplicación ejecutándose en http://localhost:8080
- [ ] Login funciona con admin/admin123
- [ ] Dashboard muestra estadísticas
- [ ] Puedo ver cursos, actividades, ubicaciones
- [ ] Puedo navegar entre diferentes secciones
- [ ] Logout funciona

Si todos los checks pasan → **¡ÉXITO! Todo está listo.** 🎉

---

**Proyecto:** MouroSubV2
**Versión:** 1.0.0
**Estado:** ✅ LISTO
**Fecha:** Mayo 2024

