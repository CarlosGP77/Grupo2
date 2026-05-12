-- Script SQL para crear la estructura de base de datos
-- Base de datos de gestión de cursos, instructores y reservas

-- Crear la base de datos (si no existe)
CREATE DATABASE IF NOT EXISTS tienda_db;
USE tienda_db;

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
    rol VARCHAR(20) DEFAULT 'USUARIO',
    Verificar_titulacion TINYINT DEFAULT 0,
    INDEX idx_email (email)
);

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

-- Datos de ejemplo
-- Insertar actividades
INSERT INTO actividades (nombre, descripcion) VALUES
('Teoría', 'Clases teóricas del curso'),
('Práctica', 'Ejercicios prácticos'),
('Laboratorio', 'Trabajo en laboratorio'),
('Evaluación', 'Pruebas y examenes');

-- Insertar cursos
INSERT INTO curso (nombre, descripcion) VALUES
('Curso de Java', 'Aprende programación en Java desde cero'),
('Curso de Python', 'Introducción a la programación con Python'),
('Curso de Spring Boot', 'Desarrollo de aplicaciones web con Spring Boot'),
('Curso de SQL', 'Gestión de bases de datos SQL');

-- Insertar ubicaciones
INSERT INTO ubicaciones (descripcion, actividad, curso) VALUES
('Aula 101 - Teoría', 1, 1),
('Laboratorio 1 - Práctica', 2, 1),
('Aula 102 - Teoría', 1, 2),
('Laboratorio 2 - Práctica', 2, 2);

-- Insertar usuarios
INSERT INTO usuarios (dni, nombre_completo, email, licencia, titulaciones, poliza_seguro, telefono, telefono_de_contacto) VALUES
('12345678A', 'Juan García López', 'juan@example.com', 'LIC-001', 'Bachillerato', 'POL-001', 600123456, 600123456),
('87654321B', 'María Rodríguez Pérez', 'maria@example.com', 'LIC-002', 'Técnico Informática', 'POL-002', 600789012, 600789012),
('11111111C', 'Carlos López Martínez', 'carlos@example.com', 'LIC-003', 'Grado Informática', 'POL-003', 600555555, 600555555);

-- Insertar usuario ADMIN
INSERT INTO usuarios (dni, nombre_completo, email, password, rol, Verificar_titulacion) VALUES
('admin', 'Administrador del Sistema', 'admin@example.com', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'ADMIN', 1);

-- Insertar usuario VERIFICADOR
INSERT INTO usuarios (dni, nombre_completo, email, password, rol, Verificar_titulacion) VALUES
('verificador', 'Verificador de Credenciales', 'verificador@example.com', '$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne', 'VERIFICADOR', 1);

-- Insertar instructores
INSERT INTO instructores (dni, nombre, disponibilidad, titulaciones, email, telefono_contacto, telefono_personal) VALUES
('99999999X', 'Dr. Antonio Experto', 1, 'Doctorado en Informática', 'antonio@example.com', 600111111, 600111111),
('88888888Y', 'Dra. Laura Especialista', 1, 'Máster en Desarrollo Web', 'laura@example.com', 600222222, 600222222);

-- Insertar usuarios-cursos
INSERT INTO usuarios_cursos (usuarios_dni, curso, precio, fecha_inicio, fecha_fin) VALUES
('12345678A', 1, 299.99, '2026-05-15 09:00:00', '2026-07-15 17:00:00'),
('87654321B', 2, 349.99, '2026-06-01 09:00:00', '2026-08-01 17:00:00'),
('11111111C', 3, 399.99, '2026-05-20 09:00:00', '2026-08-20 17:00:00');

-- Insertar reservas
INSERT INTO reservas (dni, curso, estado, fecha_hora) VALUES
('12345678A', 1, 'confirmada', '2026-05-10 10:00:00'),
('87654321B', 2, 'confirmada', '2026-05-28 14:30:00'),
('11111111C', 3, 'pendiente', '2026-05-18 11:00:00');

-- Insertar instructores-reservas
INSERT INTO instructores_reservas (instructores_dni, curso, fecha_inicio, fecha_fin) VALUES
('99999999X', 1, '2026-05-15 09:00:00', '2026-07-15 17:00:00'),
('88888888Y', 2, '2026-06-01 09:00:00', '2026-08-01 17:00:00');

-- Confirmación
SELECT 'Base de datos creada exitosamente' as mensaje;

