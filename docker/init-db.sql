-- Inicialización idempotente de la base de datos
CREATE DATABASE IF NOT EXISTS mourosub;
USE mourosub;

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
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS rol VARCHAR(20) NOT NULL DEFAULT 'USUARIO';
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS Verificar_titulacion TINYINT(1) NOT NULL DEFAULT 0;

-- Tabla ACTIVIDADES
CREATE TABLE IF NOT EXISTS actividades (
    id_actividades INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL,
    descripcion TEXT
);

-- Tabla CURSO
CREATE TABLE IF NOT EXISTS curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    INDEX idx_nombre (nombre)
);

-- Tabla UBICACIONES
CREATE TABLE IF NOT EXISTS ubicaciones (
    id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT,
    actividad INT,
    curso INT,
    FOREIGN KEY (actividad) REFERENCES actividades(id_actividades) ON DELETE SET NULL,
    FOREIGN KEY (curso) REFERENCES curso(id_curso) ON DELETE CASCADE
);

-- Tabla INSTRUCTORES
CREATE TABLE IF NOT EXISTS instructores (
    dni VARCHAR(9) PRIMARY KEY UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    disponibilidad TINYINT(1),
    titulaciones TEXT,
    email VARCHAR(100),
    telefono_contacto INT,
    telefono_personal INT,
    INDEX idx_email (email),
    INDEX idx_nombre (nombre)
);

-- Tabla RESERVAS
CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL,
    curso INT NOT NULL,
    estado VARCHAR(50),
    fecha_hora DATETIME,
    FOREIGN KEY (dni) REFERENCES usuarios(dni) ON DELETE CASCADE,
    FOREIGN KEY (curso) REFERENCES curso(id_curso) ON DELETE CASCADE,
    INDEX idx_usuario (dni),
    INDEX idx_curso (curso),
    INDEX idx_estado (estado)
);

-- Tabla USUARIOS_CURSOS (N:M entre Usuarios y Cursos)
CREATE TABLE IF NOT EXISTS usuarios_cursos (
    usuarios_dni VARCHAR(9) NOT NULL,
    curso INT NOT NULL,
    precio DECIMAL(6,2),
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    PRIMARY KEY (usuarios_dni, curso),
    FOREIGN KEY (usuarios_dni) REFERENCES usuarios(dni) ON DELETE CASCADE,
    FOREIGN KEY (curso) REFERENCES curso(id_curso) ON DELETE CASCADE
);

-- Tabla INSTRUCTORES_RESERVAS
CREATE TABLE IF NOT EXISTS instructores_reservas (
    id_instructores_curso INT AUTO_INCREMENT PRIMARY KEY,
    instructores_dni VARCHAR(9) NOT NULL,
    curso INT NOT NULL,
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    FOREIGN KEY (instructores_dni) REFERENCES instructores(dni) ON DELETE CASCADE,
    FOREIGN KEY (curso) REFERENCES curso(id_curso) ON DELETE CASCADE,
    INDEX idx_instructor (instructores_dni),
    INDEX idx_curso (curso)
);

-- Usuarios por defecto (idempotente)
INSERT INTO usuarios (dni, nombre_completo, email, password, rol, Verificar_titulacion)
VALUES ('admin', 'Administrador del Sistema', 'admin@example.com', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE
    nombre_completo = VALUES(nombre_completo),
    email = VALUES(email),
    password = VALUES(password),
    rol = VALUES(rol),
    Verificar_titulacion = VALUES(Verificar_titulacion);

INSERT INTO usuarios (dni, nombre_completo, email, password, rol, Verificar_titulacion)
VALUES ('verificador', 'Verificador de Credenciales', 'verificador@example.com', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'VERIFICADOR', 1)
ON DUPLICATE KEY UPDATE
    nombre_completo = VALUES(nombre_completo),
    email = VALUES(email),
    password = VALUES(password),
    rol = VALUES(rol),
    Verificar_titulacion = VALUES(Verificar_titulacion);

