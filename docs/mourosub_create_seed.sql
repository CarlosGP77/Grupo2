-- =========================================================
-- Base de datos: mourosub
-- Script completo de CREATE + INSERTS
-- Compatible con las entidades actuales del proyecto
-- =========================================================

CREATE DATABASE IF NOT EXISTS mourosub
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE mourosub;

-- =========================================================
-- TABLA USUARIOS
-- =========================================================
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

-- =========================================================
-- TABLA ACTIVIDADES
-- =========================================================
CREATE TABLE IF NOT EXISTS actividades (
    id_actividad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    tipo ENUM('Curso','Paquete','Actividad') NOT NULL DEFAULT 'Actividad',
    precio DECIMAL(8,2) NOT NULL,
    INDEX idx_nombre (nombre)
);

-- =========================================================
-- TABLA CURSO
-- =========================================================
CREATE TABLE IF NOT EXISTS curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    INDEX idx_nombre (nombre)
);

-- =========================================================
-- TABLA UBICACIONES
-- =========================================================
CREATE TABLE IF NOT EXISTS ubicaciones (
    id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    INDEX idx_nombre (nombre)
);

-- =========================================================
-- TABLA INMERSIONES
-- =========================================================
CREATE TABLE IF NOT EXISTS inmersiones (
    id_inmersion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    contenido TEXT,
    datos TEXT,
    dificultad ENUM('Baja','Media','Alta') NOT NULL,
    ubicacion INT NOT NULL,
    FOREIGN KEY (ubicacion) REFERENCES ubicaciones(id_ubicacion) ON DELETE CASCADE,
    INDEX idx_ubicacion (ubicacion)
);

-- =========================================================
-- TABLA INSTRUCTORES
-- =========================================================
CREATE TABLE IF NOT EXISTS instructores (
    id_instructor INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) UNIQUE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    disponibilidad BOOLEAN NOT NULL DEFAULT TRUE,
    titulaciones TEXT,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono_contacto VARCHAR(20),
    telefono_personal VARCHAR(20),
    INDEX idx_nombre (nombre),
    INDEX idx_email (email)
);

-- =========================================================
-- TABLA RESERVAS
-- =========================================================
CREATE TABLE IF NOT EXISTS reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_actividad INT NOT NULL,
    id_ubicacion INT NOT NULL,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    precio DECIMAL(8,2),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_actividad) REFERENCES actividades(id_actividad) ON DELETE CASCADE,
    FOREIGN KEY (id_ubicacion) REFERENCES ubicaciones(id_ubicacion) ON DELETE CASCADE,
    INDEX idx_usuario (id_usuario),
    INDEX idx_actividad (id_actividad),
    INDEX idx_ubicacion (id_ubicacion)
);

-- =========================================================
-- TABLA INSTRUCTORES_RESERVAS
-- =========================================================
CREATE TABLE IF NOT EXISTS instructores_reservas (
    id_instructor_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_instructor INT NOT NULL,
    id_reserva INT NOT NULL,
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    FOREIGN KEY (id_instructor) REFERENCES instructores(id_instructor) ON DELETE CASCADE,
    FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva) ON DELETE CASCADE,
    INDEX idx_instructor (id_instructor),
    INDEX idx_reserva (id_reserva)
);

-- =========================================================
-- TABLA USUARIOS_CURSOS
-- =========================================================
CREATE TABLE IF NOT EXISTS usuarios_cursos (
    usuarios_dni VARCHAR(9) NOT NULL,
    cursos_id_curso INT NOT NULL,
    precio DECIMAL(8,2),
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    PRIMARY KEY (usuarios_dni, cursos_id_curso),
    FOREIGN KEY (usuarios_dni) REFERENCES usuarios(dni) ON DELETE CASCADE,
    FOREIGN KEY (cursos_id_curso) REFERENCES curso(id_curso) ON DELETE CASCADE
);

-- =========================================================
-- TABLA WEBFOOTERS
-- =========================================================
CREATE TABLE IF NOT EXISTS webfooters (
    id_footer INT AUTO_INCREMENT PRIMARY KEY,
    tipo_info VARCHAR(255),
    contenido TEXT
);

-- =========================================================
-- INSERTS
-- =========================================================

-- Usuarios
INSERT INTO usuarios (id_usuario, dni, nombre_completo, email, licencia, titulaciones, poliza_seguro, telefono, telefono_contacto, password, rol, verificar_titulacion) VALUES
(1, 'admin', 'Administrador del Sistema', 'admin@example.com', NULL, NULL, NULL, NULL, NULL, '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'ADMIN', TRUE),
(2, 'verificador', 'Verificador de Credenciales', 'verificador@example.com', NULL, NULL, NULL, NULL, NULL, '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'VERIFICADOR', TRUE),
(3, '12345678A', 'Juan García López', 'juan@example.com', 'LIC-001', 'Bachillerato', 'POL-001', '600123456', '600123456', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'USUARIO', TRUE),
(4, '87654321B', 'María Rodríguez Pérez', 'maria@example.com', 'LIC-002', 'Técnico Informática', 'POL-002', '600789012', '600789012', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'USUARIO', FALSE),
(5, '11111111C', 'Carlos López Martínez', 'carlos@example.com', 'LIC-003', 'Grado Informática', 'POL-003', '600555555', '600555555', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'USUARIO', TRUE);

-- Actividades
INSERT INTO actividades (id_actividad, nombre, descripcion, tipo, precio) VALUES
(1, 'Open Water Diver', 'Tu primera certificación para explorar el mundo submarino con seguridad.', 'Curso', 350.00),
(2, 'Advanced Open Water', 'Perfecciona tus habilidades y desciende con más confianza.', 'Curso', 450.00),
(3, 'Rescue Diver', 'Aprende a prevenir y gestionar problemas en el agua.', 'Curso', 520.00),
(4, 'Bautismo de Buceo', 'Descubre la magia del mundo submarino en tu primera experiencia.', 'Actividad', 90.00);

-- Cursos
INSERT INTO curso (id_curso, nombre, descripcion) VALUES
(1, 'Curso de Java', 'Aprende programación en Java desde cero'),
(2, 'Curso de Python', 'Introducción a la programación con Python'),
(3, 'Curso de Spring Boot', 'Desarrollo de aplicaciones web con Spring Boot'),
(4, 'Curso de SQL', 'Gestión de bases de datos SQL');

-- Ubicaciones
INSERT INTO ubicaciones (id_ubicacion, nombre, descripcion) VALUES
(1, 'La Isla de Mouro', 'Punto emblemático para inmersiones avanzadas en la bahía de Santander.'),
(2, 'Bajos y Cabezos', 'Zona ideal para inmersiones de nivel medio con formaciones rocosas.'),
(3, 'Cabo Menor y Cabo Mayor', 'Área de gran profundidad para buceadores avanzados.'),
(4, 'El Palacio', 'Ubicación principal para salidas y bautismos.'),
(5, 'La Isla de Santa Marina', 'Zona protegida con buena visibilidad y fauna marina.'),
(6, 'Pecios', 'Inmersión técnica con pecios hundidos para buceadores experimentados.');

-- Inmersiones
INSERT INTO inmersiones (id_inmersion, nombre, contenido, datos, dificultad, ubicacion) VALUES
(1, 'Inmersión Costa Norte', 'Recorrido guiado por fondo rocoso y fauna local.', 'Duración aproximada: 40 minutos', 'Baja', 4),
(2, 'Inmersión Arrecife Verde', 'Exploración de un fondo con praderas marinas.', 'Requiere certificación básica', 'Media', 2),
(3, 'Inmersión Noche Azul', 'Inmersión nocturna con foco en orientación y seguridad.', 'Salida especial con instructor', 'Alta', 5),
(4, 'Inmersión Isla de Mouro', 'Ruta técnica en una de las zonas más conocidas de la bahía.', 'Solo buceadores avanzados', 'Alta', 1);

-- Instructores
INSERT INTO instructores (id_instructor, dni, nombre, disponibilidad, titulaciones, email, telefono_contacto, telefono_personal) VALUES
(1, '99999999X', 'Antonio Experto', TRUE, 'Instructor de buceo y rescate', 'antonio@example.com', '600111111', '600111111'),
(2, '88888888Y', 'Laura Especialista', TRUE, 'Formación avanzada y mantenimiento', 'laura@example.com', '600222222', '600222222');

-- Reservas
INSERT INTO reservas (id_reserva, id_usuario, id_actividad, id_ubicacion, fecha_inicio, fecha_fin, precio) VALUES
(1, 3, 1, 4, '2026-05-15 09:00:00', '2026-07-15 17:00:00', 299.99),
(2, 4, 2, 2, '2026-06-01 09:00:00', '2026-08-01 17:00:00', 349.99),
(3, 5, 3, 5, '2026-05-20 09:00:00', '2026-08-20 17:00:00', 399.99);

-- Instructores_Reservas
INSERT INTO instructores_reservas (id_instructor_reserva, id_instructor, id_reserva, fecha_inicio, fecha_fin) VALUES
(1, 1, 1, '2026-05-15 09:00:00', '2026-07-15 17:00:00'),
(2, 2, 2, '2026-06-01 09:00:00', '2026-08-01 17:00:00');

-- Usuarios_Cursos
INSERT INTO usuarios_cursos (usuarios_dni, cursos_id_curso, precio, fecha_inicio, fecha_fin) VALUES
('12345678A', 1, 299.99, '2026-05-15 09:00:00', '2026-07-15 17:00:00'),
('87654321B', 2, 349.99, '2026-06-01 09:00:00', '2026-08-01 17:00:00'),
('11111111C', 3, 399.99, '2026-05-20 09:00:00', '2026-08-20 17:00:00');

-- Web footer
INSERT INTO webfooters (tipo_info, contenido) VALUES
('empresa', 'Mourosub'),
('descripcion', 'Abyssal Elegance in Diving.'),
('direccion', 'Puerto Deportivo Marina del Cantábrico'),
('copyright', '©2024 Mourosub.'),
('enlace_legal', 'Privacy Policy'),
('enlace_legal', 'Terms of Service'),
('empresa_info', 'About Us'),
('empresa_info', 'Safety Protocols'),
('red_social', 'https://www.facebook.com/mourosub'),
('red_social', 'https://www.instagram.com/mourosub'),
('red_social', 'https://x.com/mourosub');

SELECT 'Base de datos mourosub creada y cargada correctamente' AS mensaje;

