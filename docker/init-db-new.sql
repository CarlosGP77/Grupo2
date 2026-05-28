-- Tabla USUARIOS
CREATE TABLE IF NOT EXISTS usuarios (
    dni VARCHAR(9) PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    licencia VARCHAR(16),
    titulaciones TEXT,
    poliza_seguro VARCHAR(15),
    telefono INT,
    telefono_de_contacto INT,
    password VARCHAR(255),
    rol VARCHAR(20) NOT NULL DEFAULT 'USUARIO',
    Verificar_titulacion TINYINT(1) NOT NULL DEFAULT 0,
    INDEX idx_email (email)
);

-- Asegurar columnas nuevas en usuarios si la tabla ya existía
SET @db_name := DATABASE();

SET @rol_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db_name
      AND TABLE_NAME = 'usuarios'
      AND COLUMN_NAME = 'rol'
);
SET @sql := IF(
    @rol_exists = 0,
    'ALTER TABLE usuarios ADD COLUMN rol VARCHAR(20) NOT NULL DEFAULT ''USUARIO''',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @verif_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db_name
      AND TABLE_NAME = 'usuarios'
      AND COLUMN_NAME = 'Verificar_titulacion'
);
SET @sql := IF(
    @verif_exists = 0,
    'ALTER TABLE usuarios ADD COLUMN Verificar_titulacion TINYINT(1) NOT NULL DEFAULT 0',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Tabla ACTIVIDADES
CREATE TABLE IF NOT EXISTS actividades (
    id_actividad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tipo ENUM('Curso', 'Paquete', 'Actividad') DEFAULT 'Actividad',
    precio DECIMAL(6,2),
    INDEX idx_nombre (nombre)
);

-- Tabla UBICACIONES
CREATE TABLE IF NOT EXISTS ubicaciones (
    id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contenido TEXT,
    INDEX idx_nombre (nombre)
);

-- Tabla INMERSIONES
CREATE TABLE IF NOT EXISTS inmersiones (
    id_inmersion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    contenido TEXT,
    datos TEXT,
    dificultad ENUM('Baja', 'Media', 'Alta') NOT NULL,
    ubicacion INT NOT NULL,
    INDEX idx_ubicacion (ubicacion),
    FOREIGN KEY (ubicacion) REFERENCES ubicaciones(id_ubicacion) ON DELETE CASCADE
);

-- Tabla INSTRUCTORES
CREATE TABLE IF NOT EXISTS instructores (
    dni VARCHAR(9) PRIMARY KEY UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    disponibilidad TINYINT(1),
    titulaciones TEXT,
    email VARCHAR(100) NOT NULL,
    telefono_contacto INT,
    telefono_personal INT,
    INDEX idx_email (email),
    INDEX idx_nombre (nombre)
);

-- Tabla RESERVAS
CREATE TABLE IF NOT EXISTS reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL,
    actividad INT NOT NULL,
    ubicacion INT NOT NULL,
    precio DECIMAL(6,2),
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    FOREIGN KEY (dni) REFERENCES usuarios(dni) ON DELETE CASCADE,
    FOREIGN KEY (actividad) REFERENCES actividades(id_actividad) ON DELETE CASCADE,
    FOREIGN KEY (ubicacion) REFERENCES ubicaciones(id_ubicacion) ON DELETE CASCADE,
    INDEX idx_dni (dni),
    INDEX idx_actividad (actividad),
    INDEX idx_ubicacion (ubicacion)
);

-- Tabla USUARIOS_RESERVAS (N:M entre Usuarios y Reservas)
CREATE TABLE IF NOT EXISTS usuarios_reservas (
    usuario VARCHAR(9) NOT NULL,
    reserva INT NOT NULL,
    PRIMARY KEY (usuario, reserva),
    FOREIGN KEY (usuario) REFERENCES usuarios(dni) ON DELETE CASCADE,
    FOREIGN KEY (reserva) REFERENCES reservas(id_reserva) ON DELETE CASCADE
);

-- Tabla INSTRUCTORES_RESERVAS (N:M entre Instructores y Reservas)
CREATE TABLE IF NOT EXISTS instructores_reservas (
    id_instructor_reserva INT AUTO_INCREMENT PRIMARY KEY,
    instructor VARCHAR(9) NOT NULL,
    reserva INT NOT NULL,
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    FOREIGN KEY (instructor) REFERENCES instructores(dni) ON DELETE CASCADE,
    FOREIGN KEY (reserva) REFERENCES reservas(id_reserva) ON DELETE CASCADE,
    INDEX idx_instructor (instructor),
    INDEX idx_reserva (reserva)
);

/* ============================================
    Tablas de información para la WEB
   ============================================ */

CREATE TABLE IF NOT EXISTS webfooters (
    id_footer INT AUTO_INCREMENT PRIMARY KEY,
    tipo_info VARCHAR (255),
    contenido TEXT
);

/* ============================================
    Inserción de datos para las tablas
   ============================================ */

-- Usuarios por defecto (idempotente)
INSERT INTO usuarios (dni, nombre_completo, email, password, rol, Verificar_titulacion)
VALUES ('admin', 'Administrador del Sistema', 'admin@mourosub.com', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE
    nombre_completo = VALUES(nombre_completo),
    email = VALUES(email),
    password = VALUES(password),
    rol = VALUES(rol),
    Verificar_titulacion = VALUES(Verificar_titulacion);

INSERT INTO usuarios (dni, nombre_completo, email, password, rol, Verificar_titulacion)
VALUES ('verificador', 'Verificador de Credenciales', 'verificador@mourosub.com', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'VERIFICADOR', 1)
ON DUPLICATE KEY UPDATE
    nombre_completo = VALUES(nombre_completo),
    email = VALUES(email),
    password = VALUES(password),
    rol = VALUES(rol),
    Verificar_titulacion = VALUES(Verificar_titulacion);

