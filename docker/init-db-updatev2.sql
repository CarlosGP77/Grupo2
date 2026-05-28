/* ============================================
   CREATE DATABASE
   ============================================ */

-- CREATE DATABASE IF NOT EXISTS mourosub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

/* ============================================
   TABLA USUARIOS
   ============================================ */

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) UNIQUE,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    licencia VARCHAR(50),
    titulaciones TEXT,
    poliza_seguro VARCHAR(50),
    telefono VARCHAR(20),
    telefono_contacto VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    rol ENUM('USUARIO','ADMIN','VERIFICADOR') NOT NULL DEFAULT 'USUARIO',
    verificar_titulacion BOOLEAN NOT NULL DEFAULT FALSE,

    INDEX idx_email (email),
    INDEX idx_dni (dni)
);

/* ============================================
   TABLA ACTIVIDADES
   ============================================ */

CREATE TABLE IF NOT EXISTS actividades (
    id_actividad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    tipo ENUM('Curso','Paquete','Actividad') NOT NULL DEFAULT 'Actividad',
    precio DECIMAL(8,2) NOT NULL,

    INDEX idx_nombre (nombre)
);

/* ============================================
   TABLA UBICACIONES
   ============================================ */

CREATE TABLE IF NOT EXISTS ubicaciones (
    id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,

    INDEX idx_nombre (nombre)
);

/* ============================================
   TABLA INMERSIONES
   ============================================ */

CREATE TABLE IF NOT EXISTS inmersiones (
    id_inmersion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    datos TEXT,
    dificultad ENUM('Baja','Media','Alta') NOT NULL,
    id_ubicacion INT NOT NULL,

    FOREIGN KEY (id_ubicacion)
        REFERENCES ubicaciones(id_ubicacion)
        ON DELETE CASCADE,

    INDEX idx_ubicacion (id_ubicacion)
);

/* ============================================
   TABLA INSTRUCTORES
   ============================================ */

CREATE TABLE IF NOT EXISTS instructores (
    id_instructor INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) UNIQUE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    disponibilidad BOOLEAN DEFAULT TRUE,
    titulaciones TEXT,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono_contacto VARCHAR(20),
    telefono_personal VARCHAR(20),

    INDEX idx_nombre (nombre),
    INDEX idx_email (email)
);

/* ============================================
   TABLA RESERVAS
   ============================================ */

CREATE TABLE IF NOT EXISTS reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,

    id_usuario INT NOT NULL,
    id_actividad INT NOT NULL,
    id_ubicacion INT NOT NULL,

    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,

    precio DECIMAL(8,2),

    FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
        ON DELETE CASCADE,

    FOREIGN KEY (id_actividad)
        REFERENCES actividades(id_actividad)
        ON DELETE CASCADE,

    FOREIGN KEY (id_ubicacion)
        REFERENCES ubicaciones(id_ubicacion)
        ON DELETE CASCADE,

    INDEX idx_usuario (id_usuario),
    INDEX idx_actividad (id_actividad),
    INDEX idx_ubicacion (id_ubicacion)
);

/* ============================================
   TABLA INSTRUCTORES_RESERVAS
   ============================================ */

CREATE TABLE IF NOT EXISTS instructores_reservas (
    id_instructor_reserva INT AUTO_INCREMENT PRIMARY KEY,

    id_instructor INT NOT NULL,
    id_reserva INT NOT NULL,

    fecha_inicio DATETIME,
    fecha_fin DATETIME,

    FOREIGN KEY (id_instructor)
        REFERENCES instructores(id_instructor)
        ON DELETE CASCADE,

    FOREIGN KEY (id_reserva)
        REFERENCES reservas(id_reserva)
        ON DELETE CASCADE,

    INDEX idx_instructor (id_instructor),
    INDEX idx_reserva (id_reserva)
);

/* ============================================
   TABLA FOOTER WEB
   ============================================ */

CREATE TABLE IF NOT EXISTS webfooters (
    id_footer INT AUTO_INCREMENT PRIMARY KEY,
    tipo_info VARCHAR(255),
    contenido TEXT
);

/* ============================================
   TABLA CURSOS (LEGACY - para compatibilidad)
   ============================================ */

CREATE TABLE IF NOT EXISTS cursos (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,

    INDEX idx_nombre (nombre)
);

/* ============================================
   TABLA USUARIOS_CURSOS (LEGACY - para compatibilidad)
   ============================================ */

CREATE TABLE IF NOT EXISTS usuarios_cursos (
    usuarios_dni VARCHAR(9) NOT NULL,
    cursos_id_curso INT NOT NULL,

    PRIMARY KEY (usuarios_dni, cursos_id_curso),

    FOREIGN KEY (usuarios_dni)
        REFERENCES usuarios(dni)
        ON DELETE CASCADE,

    FOREIGN KEY (cursos_id_curso)
        REFERENCES cursos(id_curso)
        ON DELETE CASCADE
);

/* ============================================
    Inserción de datos para las tablas
   ============================================ */

-- Usuarios por defecto (idempotente)
INSERT INTO usuarios (id_usuario, dni, nombre_completo, email, password, rol, verificar_titulacion)
VALUES (1, 'admin', 'Administrador del Sistema', 'admin@mourosub.com', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'ADMIN', TRUE)
ON DUPLICATE KEY UPDATE
    nombre_completo = VALUES(nombre_completo),
    email = VALUES(email),
    password = VALUES(password),
    rol = VALUES(rol),
    verificar_titulacion = VALUES(verificar_titulacion);

INSERT INTO usuarios (id_usuario, dni, nombre_completo, email, password, rol, verificar_titulacion)
VALUES (2, 'verificador', 'Verificador de Credenciales', 'verificador@mourosub.com', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'VERIFICADOR', TRUE)
ON DUPLICATE KEY UPDATE
    nombre_completo = VALUES(nombre_completo),
    email = VALUES(email),
    password = VALUES(password),
    rol = VALUES(rol),
    verificar_titulacion = VALUES(verificar_titulacion);

