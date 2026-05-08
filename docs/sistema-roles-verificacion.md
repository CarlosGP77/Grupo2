# Sistema de Roles y Verificación de Usuarios

## Descripción General

El sistema implementa control de acceso basado en roles (RBAC) para gestionar usuarios y su estado de verificación de credenciales. Hay tres roles principales:

1. **ADMIN** - Administrador (Control total)
2. **VERIFICADOR** - Verificador de Credenciales (Verifica usuarios)
3. **USUARIO** - Usuario Normal (Solo acceso a su propia información)

---

## Roles y Permisos

### 1. ADMIN (Administrador)
**Credenciales por defecto:**
- DNI: `admin`
- Contraseña: `admin123`

**Permisos:**
- ✓ Crear, editar, eliminar cualquier usuario
- ✓ Cambiar el estado `verificado` de cualquier usuario (Sí/No)
- ✓ Modificar todos los campos de los usuarios
- ✓ Acceso completo al sistema
- ✓ Acceso a `/admin/**`
- ✓ Acceso a `/verificador/**` (panel de verificación)
- ✓ Acceso a `/test` (panel de pruebas)

### 2. VERIFICADOR (Verificador de Credenciales)
**Credenciales por defecto:**
- DNI: `verificador`
- Contraseña: `verificador123`

**Permisos:**
- ✓ Ver lista de usuarios sin verificar
- ✓ Cambiar el estado `verificado` de usuarios (Sí/No)
- ✗ No puede crear/editar usuarios
- ✗ No puede modificar otros campos
- ✗ No tiene acceso a `/admin/**`
- ✓ Acceso a `/verificador/**` (panel de verificación)
- ✓ Acceso a `/test` (panel de pruebas)

### 3. USUARIO (Usuario Normal)
**Permisos:**
- ✓ Ver su propia información
- ✓ Cambiar su contraseña (si se implemente)
- ✗ No puede modificar otros usuarios
- ✗ No puede cambiar estado de verificación
- ✗ No tiene acceso a `/admin/**`
- ✗ No tiene acceso a `/verificador/**`
- ✓ Acceso a `/test` (panel de pruebas)

---

## Flujo de Verificación

1. **Crear usuario nuevo**
   - Se crea con `rol = USUARIO` y `verificado = NO`
   - Solo ADMIN o VERIFICADOR pueden cambiar a `verificado = SÍ`

2. **Panel de verificación** (`/verificador/panel`)
   - Muestra lista de usuarios sin verificar
   - Botón "Verificar Usuario" para cambiar estado
   - Accesible solo a ADMIN y VERIFICADOR

3. **Cambiar estado de verificación**
   - Endpoint: `POST /verificador/cambiar-verificacion`
   - Parámetros: `dni`, `verificado` (true/false)
   - Solo ADMIN y VERIFICADOR pueden hacerlo

---

## Base de Datos

### Campo `rol` en tabla `usuarios`
```sql
ALTER TABLE usuarios ADD COLUMN rol VARCHAR(20) DEFAULT 'USUARIO';
```

### Campo `Verificar_titulacion` en tabla `usuarios`
```sql
-- Ya existe. Por defecto: 0 (NO), 1 (SÍ)
ALTER TABLE usuarios MODIFY COLUMN Verificar_titulacion TINYINT DEFAULT 0;
```

---

## Cómo Usar

### Login

1. Accede a: http://localhost/login (o puerto 8080 si no estás en Docker)
2. Usa las credenciales de tu rol:
   - **ADMIN:** dni=`admin`, contraseña=`admin123`
   - **VERIFICADOR:** dni=`verificador`, contraseña=`verificador123`

### Crear usuario con rol

**Desde el panel de pruebas** (`/test`):
1. Pulsa botón "➕ Crear Usuario"
2. Completa el formulario
3. En el campo "Rol", selecciona:
   - Usuario (normal)
   - Verificador
   - Administrador
4. Pulsa "Guardar Usuario"

**Desde el form HTML:**
```html
<form method="POST" action="/test/usuario/crear">
    <input type="text" name="dni" required>
    <input type="text" name="nombre_completo" required>
    <input type="email" name="email" required>
    <input type="text" name="licencia">
    <input type="password" name="password" required>
    <select name="rol">
        <option value="USUARIO">Usuario (normal)</option>
        <option value="VERIFICADOR">Verificador</option>
        <option value="ADMIN">Administrador</option>
    </select>
    <button type="submit">Guardar Usuario</button>
</form>
```

### Verificar usuario

1. Login con ADMIN o VERIFICADOR
2. Accede a: http://localhost/verificador/panel
3. Verás lista de usuarios sin verificar
4. Pulsa "✓ Verificar Usuario" para cambiar estado
5. O pulsa "Más detalles" para ver más información

---

## Protección de Rutas

Las rutas están protegidas en `SecurityConfig`:

| Ruta | Acceso |
|------|--------|
| `/` | Público |
| `/login` | Público |
| `/test/**` | Público (desarrollo) |
| `/admin/**` | Solo ADMIN |
| `/verificador/**` | ADMIN + VERIFICADOR |
| Otros | Autenticado |

---

## Seguridad Backend

- ✓ Las contraseñas se almacenan con **BCrypt** (no en texto plano)
- ✓ Los roles se validan en cada request
- ✓ Las rutas sensibles requieren autenticación y rol correcto
- ✓ El frontend no confía en datos del cliente (validación en backend)

---

## Implementación Técnica

### Entidad Usuario
```java
public enum Rol {
    ADMIN, VERIFICADOR, USUARIO
}

@Enumerated(EnumType.STRING)
@Column(length = 20)
private Rol rol = Rol.USUARIO;
```

### CustomUserDetailsService
Carga el rol de la base de datos y lo asigna como autoridad:
```java
String role = u.getRol() != null ? u.getRol().name() : "USUARIO";
return User.withUsername(u.getDni())
    .password(password)
    .roles(role)  // ADMIN, VERIFICADOR, USUARIO
    .build();
```

### SecurityConfig
Protege rutas según rol:
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/verificador/**").hasAnyRole("ADMIN", "VERIFICADOR")
    .anyRequest().authenticated()
)
```

### VerificationController
Endpoint para cambiar estado:
```java
@PostMapping("/verificador/cambiar-verificacion")
public String cambiarVerificacion(@RequestParam String dni,
                                   @RequestParam boolean verificado) {
    // Solo ADMIN/VERIFICADOR pueden llegar aquí (protegido en SecurityConfig)
    Usuario u = usuarioRepository.findByDni(dni);
    if (u != null) {
        u.setVerificar_titulacion(verificado);
        usuarioRepository.save(u);
    }
    return "redirect:/verificador/panel";
}
```

---

## Próximas Mejoras (Opcional)

- [ ] Agregar pantalla de edición de usuarios (solo ADMIN)
- [ ] Agregar historial de cambios de verificación (quién, cuándo, por qué)
- [ ] Agregar campos de auditoría (created_at, updated_at, updated_by)
- [ ] Implementar 2FA para cuentas ADMIN
- [ ] Agregar API REST con autenticación JWT para móvil/frontend
- [ ] Exportar reporte de usuarios verificados/no verificados

---

## Preguntas Frecuentes

**¿Qué pasa si un usuario normal intenta acceder a `/verificador/panel`?**
- Spring Security rechaza el acceso con error 403 Forbidden

**¿Puedo cambiar la contraseña del admin?**
- Sí, crea un usuario ADMIN con otro DNI y contraseña en el panel

**¿Los usuarios pueden verificarse a sí mismos?**
- No, solo ADMIN y VERIFICADOR pueden cambiar el estado `verificado`

**¿Qué pasa si alguien intenta enviar un formulario alterado?**
- Spring Security valida en backend, rechazando si el rol no es el correcto

