# Insertar Usuarios en Base de Datos (Sin Hardcodear en Java)

## ⚠️ Seguridad
Los usuarios **NO** deben estar en el código Java. Se crean directamente en la BD con contraseñas encriptadas.

---

## 1. Generar Hash BCrypt

Necesitas el hash BCrypt de la contraseña. Hay varias formas:

### Opción A: Online (rápido, menos seguro)
Ve a: https://bcrypt-generator.com/
- Contraseña: `admin123` 
- Genera el hash BCrypt
- Copia el resultado (algo como: `$2a$10$...`)

### Opción B: Desde Java (más seguro)
Crea un pequeño programa:

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123";
        String hash = encoder.encode(password);
        System.out.println("Hash BCrypt: " + hash);
    }
}
```

Ejecuta y copia el hash.

### Opción C: Desde MySQL/MariaDB directamente
Algunos drivers permiten generar BCrypt en SQL, pero es más complicado. Usa las opciones A o B.

---

## 2. Insertar Usuario ADMIN en BD

Después de generar el hash BCrypt, inserta en la BD:

```sql
INSERT INTO usuarios (
    dni,
    nombre_completo,
    email,
    licencia,
    password,
    rol,
    Verificar_titulacion
) VALUES (
    'admin',
    'Administrador',
    'admin@example.com',
    'ADMIN',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/1Qq',  -- REEMPLAZA CON TU HASH
    'ADMIN',
    1
);
```

---

## 3. Insertar Usuario VERIFICADOR en BD

```sql
INSERT INTO usuarios (
    dni,
    nombre_completo,
    email,
    licencia,
    password,
    rol,
    Verificar_titulacion
) VALUES (
    'verificador',
    'Verificador de Credenciales',
    'verificador@example.com',
    'VERIFICADOR',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/1Qq',  -- REEMPLAZA CON TU HASH
    'VERIFICADOR',
    1
);
```

---

## 4. Insertar Usuario Normal (sin verificar)

```sql
INSERT INTO usuarios (
    dni,
    nombre_completo,
    email,
    licencia,
    password,
    rol,
    Verificar_titulacion
) VALUES (
    '12345678',
    'Juan Pérez',
    'juan@example.com',
    'LIC-001',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/1Qq',  -- REEMPLAZA CON TU HASH
    'USUARIO',
    0  -- NO verificado
);
```

---

## 5. En Docker/Docker Compose

### Script SQL en volumen

Crea un archivo `init.sql` en la raíz del proyecto:

```sql
-- Usuarios por defecto (sin hardcodear en Java)
INSERT INTO usuarios (dni, nombre_completo, email, licencia, password, rol, Verificar_titulacion) 
VALUES ('admin', 'Administrador', 'admin@example.com', 'ADMIN', '$2a$10$...', 'ADMIN', 1);

INSERT INTO usuarios (dni, nombre_completo, email, licencia, password, rol, Verificar_titulacion) 
VALUES ('verificador', 'Verificador', 'verificador@example.com', 'VERIFICADOR', '$2a$10$...', 'VERIFICADOR', 1);

INSERT INTO usuarios (dni, nombre_completo, email, licencia, password, rol, Verificar_titulacion) 
VALUES ('usuario1', 'Usuario Test', 'usuario@example.com', 'LIC-001', '$2a$10$...', 'USUARIO', 0);
```

Actualiza `docker-compose.yml`:

```yaml
db:
  image: mariadb:10.11
  volumes:
    - db_data:/var/lib/mysql
    - ./init.sql:/docker-entrypoint-initdb.d/init.sql  # ← Añade esto
  environment:
    MARIADB_ROOT_PASSWORD: ${DB_PASSWORD:-admin_123}
    MARIADB_DATABASE: ${DB_NAME:-mourosub}
```

Al arrancar Docker, ejecutará el script SQL automáticamente.

---

## 6. Verificar que los usuarios se crearon

```sql
SELECT dni, nombre_completo, rol, Verificar_titulacion FROM usuarios;
```

Deberías ver algo como:

```
| dni        | nombre_completo              | rol         | Verificar_titulacion |
|------------|------------------------------|-------------|----------------------|
| admin      | Administrador                | ADMIN       | 1                    |
| verificador| Verificador de Credenciales  | VERIFICADOR | 1                    |
| usuario1   | Usuario Test                 | USUARIO     | 0                    |
```

---

## 7. Login en la aplicación

URL: http://localhost/login (puerto 80 en Docker)

### Con usuario ADMIN:
- DNI: `admin`
- Contraseña: (la que usaste para generar el hash BCrypt)
- Acceso a: `/admin/panel`

### Con usuario VERIFICADOR:
- DNI: `verificador`
- Contraseña: (la que usaste para generar el hash BCrypt)
- Acceso a: `/verificador/panel`

### Con usuario USUARIO:
- DNI: `usuario1`
- Contraseña: (la que usaste para generar el hash BCrypt)
- Acceso limitado

---

## ⚠️ Seguridad - Checklist

✅ Usuarios creados en BD (NO en Java)  
✅ Contraseñas encriptadas con BCrypt (NO texto plano)  
✅ Archivo `init.sql` NO contiene contraseñas en claro  
✅ `.env` local NO se sube al repositorio  
✅ BD accesible solo internamente (no expuesta)  
✅ Validaciones en backend (no confiar en cliente)

---

## Endpoints Protegidos

| Endpoints | Acceso | Rol |
|-----------|--------|-----|
| GET `/admin/panel` | Solo ADMIN | ADMIN |
| GET `/admin/api/stats` | Solo ADMIN | ADMIN |
| GET `/verificador/panel` | ADMIN + VERIFICADOR | ADMIN, VERIFICADOR |
| POST `/verificador/api/cambiar-verificacion` | ADMIN + VERIFICADOR | ADMIN, VERIFICADOR |
| GET `/verificador/api/usuarios-sin-verificar` | ADMIN + VERIFICADOR | ADMIN, VERIFICADOR |

---

## Ejemplo Completo (Comando SQL)

```sql
-- Hash BCrypt para "admin123"
-- (generado desde bcrypt-generator.com o desde Java)
SET @hash_admin = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/1Qq';
SET @hash_verificador = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/1Qq';

-- Insertar ADMIN
INSERT INTO usuarios VALUES ('admin', 'Administrador', 'admin@example.com', 'ADMIN', NULL, NULL, 0, NULL, NULL, @hash_admin, 0, 'ADMIN');

-- Insertar VERIFICADOR
INSERT INTO usuarios VALUES ('verificador', 'Verificador', 'verificador@example.com', 'VERIFICADOR', NULL, NULL, 0, NULL, NULL, @hash_verificador, 0, 'VERIFICADOR');
```

---

## Pregunta: ¿Por qué no en Java?

1. **Seguridad**: Las credenciales NO están en el código fuente
2. **Privacidad**: Cada entorno (dev, test, prod) puede tener diferentes usuarios
3. **Flexibilidad**: Cambiar contraseñas sin recompilar
4. **Auditoría**: Se ve claramente quién creó qué usuarios

