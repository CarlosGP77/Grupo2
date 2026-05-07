# Estructura de Entidades JPA - Base de Datos de Cursos y Reservas

## Resumen de Cambios Realizados

Se ha actualizado completamente la estructura del proyecto para implementar el diagrama ER de la base de datos de gestión de cursos, instructores y reservas.

## Entidades Creadas

### 1. **Usuario** (Tabla: `usuarios`)
Representa los clientes/usuarios que se pueden registrar en cursos.

**Campos:**
- `dni` (VARCHAR(9)) - PK
- `nombre_completo` (VARCHAR(100))
- `email` (VARCHAR(100))
- `licencia` (VARCHAR(16))
- `titulaciones` (TEXT)
- `poliza_seguro` (VARCHAR(15))
- `telefono` (INT)
- `telefono_de_contacto` (INT)
- `password` (STRING)

**Relaciones:**
- OneToMany con Reserva
- OneToMany con UsuariosCursos

---

### 2. **Curso** (Tabla: `curso`)
Representa los cursos disponibles.

**Campos:**
- `id_curso` (INT, PK, AUTO_INCREMENT)
- `nombre` (VARCHAR(100))
- `descripcion` (TEXT)

**Relaciones:**
- OneToMany con Ubicacion
- OneToMany con UsuariosCursos
- OneToMany con Reserva

---

### 3. **Actividad** (Tabla: `actividades`)
Representa las actividades que se pueden realizar en cada ubicación.

**Campos:**
- `id_actividades` (INT, PK, AUTO_INCREMENT)
- `nombre` (VARCHAR(45))
- `descripcion` (TEXT)

**Relaciones:**
- OneToMany con Ubicacion

---

### 4. **Ubicacion** (Tabla: `ubicaciones`)
Representa los lugares donde se realizan los cursos y sus actividades.

**Campos:**
- `id_ubicacion` (INT, PK, AUTO_INCREMENT)
- `descripcion` (TEXT)
- `actividad` (INT, FK)
- `curso` (INT, FK)

**Relaciones:**
- ManyToOne con Actividad
- ManyToOne con Curso

---

### 5. **Instructor** (Tabla: `instructores`)
Representa los instructores que imparten los cursos.

**Campos:**
- `dni` (VARCHAR(9), PK, UNIQUE)
- `nombre` (VARCHAR(100))
- `disponibilidad` (TINYINT(1) - Boolean)
- `titulaciones` (TEXT)
- `email` (VARCHAR(100))
- `telefono_contacto` (INT)
- `telefono_personal` (INT)

**Relaciones:**
- OneToMany con InstructoresReservas

---

### 6. **Reserva** (Tabla: `reservas`)
Representa las reservas de cursos realizadas por usuarios.

**Campos:**
- `id` (LONG, PK, AUTO_INCREMENT)
- `dni` (VARCHAR(9), FK)
- `curso` (INT, FK)
- `estado` (VARCHAR(50))
- `fecha_hora` (DATETIME)

**Relaciones:**
- ManyToOne con Usuario
- ManyToOne con Curso

---

### 7. **UsuariosCursos** (Tabla: `usuarios_cursos`)
Tabla asociativa que representa la relación N:M entre Usuarios y Cursos.

**Campos:**
- `usuarios_dni` (VARCHAR(9), PK, FK)
- `curso` (INT, PK, FK)
- `precio` (DECIMAL(6,2))
- `fecha_inicio` (DATETIME)
- `fecha_fin` (DATETIME)

**ID Compuesto:** UsuariosCursosId
- usuarios_dni (String)
- curso (Integer)

**Relaciones:**
- ManyToOne con Usuario
- ManyToOne con Curso

---

### 8. **InstructoresReservas** (Tabla: `instructores_reservas`)
Representa la asignación de instructores a reservas de cursos.

**Campos:**
- `id_instructores_curso` (INT, PK, AUTO_INCREMENT)
- `instructores_dni` (VARCHAR(9), FK)
- `curso` (INT, FK)
- `fecha_inicio` (DATETIME)
- `fecha_fin` (DATETIME)

**Relaciones:**
- ManyToOne con Instructor
- ManyToOne con Curso

---

## Repositories Creados

Se han creado los siguientes repositorios para acceder a los datos:

1. **UsuarioRepository** - CRUD + búsqueda por email y DNI
2. **CursoRepository** - CRUD + búsqueda por nombre
3. **ReservaRepository** - CRUD + búsqueda por usuario, curso y estado
4. **InstructorRepository** - CRUD + búsqueda por email, DNI, nombre y disponibilidad
5. **ActividadRepository** - CRUD + búsqueda por nombre
6. **UbicacionRepository** - CRUD + búsqueda por curso y actividad
7. **UsuariosCursosRepository** - CRUD + búsqueda por usuario y curso
8. **InstructoresReservasRepository** - CRUD + búsqueda por instructor y curso

---

## Estructura en el Proyecto

```
src/main/java/com/example/
├── model/
│   ├── Usuario.java
│   ├── Curso.java
│   ├── Actividad.java
│   ├── Ubicacion.java
│   ├── Instructor.java
│   ├── Reserva.java
│   ├── UsuariosCursos.java
│   ├── UsuariosCursosId.java
│   └── InstructoresReservas.java
├── repository/
│   ├── UsuarioRepository.java
│   ├── CursoRepository.java
│   ├── ReservaRepository.java
│   ├── InstructorRepository.java
│   ├── ActividadRepository.java
│   ├── UbicacionRepository.java
│   ├── UsuariosCursosRepository.java
│   └── InstructoresReservasRepository.java
```

---

## Notas Importantes

1. **DNI como String**: Los DNI se han modelado como VARCHAR(9) y se usan como identificadores únicos.
2. **Relaciones Lazy**: Se ha utilizado `FetchType.LAZY` en todas las relaciones ManyToOne para optimizar las consultas.
3. **Cascade**: Las relaciones OneToMany tienen `CascadeType.ALL` y `orphanRemoval = true` para mantener la integridad referencial.
4. **LocalDateTime**: Se ha utilizado `LocalDateTime` para los campos de fechas y horas.
5. **ID Compuesto**: La tabla `usuarios_cursos` utiliza una clave primaria compuesta mediante `IdClass`.

---

## Próximos Pasos

1. Crear los **Services** para la lógica de negocio
2. Crear los **Controllers** para exponer los endpoints REST
3. Crear las migraciones/SQL de inicialización
4. Añadir validaciones y reglas de negocio
5. Implementar autenticación/autorización con Spring Security

