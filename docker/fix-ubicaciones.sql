-- Script para corregir las ubicaciones y hacerlas coincidir con las imágenes

USE mourosub;

-- Primero, eliminar las ubicaciones actuales y sus referencias
DELETE FROM inmersiones;
DELETE FROM reservas;
DELETE FROM ubicaciones;

-- Insertar las ubicaciones correctas (con nombres que coincidan con las imágenes)
INSERT INTO ubicaciones (id_ubicacion, nombre, descripcion) VALUES
(1, 'La Isla de Mouro', 'Punto emblemático para inmersiones avanzadas en la bahía de Santander.'),
(2, 'Bajos y Cabezos', 'Zona ideal para inmersiones de nivel medio con formaciones rocosas.'),
(3, 'Cabo Menor y Cabo Mayor', 'Área de gran profundidad para buceadores avanzados.'),
(4, 'El Palacio', 'Ubicación principal para salidas y bautismos.'),
(5, 'La Isla de Santa Marina', 'Zona protegida con buena visibilidad y fauna marina.'),
(6, 'Pecios', 'Inmersión técnica con pecios hundidos para buceadores experimentados.');

-- Re-insertar inmersiones (actualizar referencias de ubicación)
INSERT INTO inmersiones (id_inmersion, nombre, contenido, datos, dificultad, ubicacion) VALUES
(1, 'Inmersión Costa Norte', 'Recorrido guiado por fondo rocoso y fauna local.', 'Duración aproximada: 40 minutos', 'Baja', 4),
(2, 'Inmersión Arrecife Verde', 'Exploración de un fondo con praderas marinas.', 'Requiere certificación básica', 'Media', 2),
(3, 'Inmersión Noche Azul', 'Inmersión nocturna con foco en orientación y seguridad.', 'Salida especial con instructor', 'Alta', 5),
(4, 'Inmersión Isla de Mouro', 'Ruta técnica en una de las zonas más conocidas de la bahía.', 'Solo buceadores avanzados', 'Alta', 1);

-- Re-insertar reservas (actualizar referencias de ubicación)
INSERT INTO reservas (id_reserva, id_usuario, id_actividad, id_ubicacion, fecha_inicio, fecha_fin, precio) VALUES
(1, 3, 1, 4, '2026-05-15 09:00:00', '2026-07-15 17:00:00', 299.99),
(2, 4, 2, 2, '2026-06-01 09:00:00', '2026-08-01 17:00:00', 349.99),
(3, 5, 3, 5, '2026-05-20 09:00:00', '2026-08-20 17:00:00', 399.99);

SELECT 'Ubicaciones corregidas exitosamente' AS mensaje;
SELECT * FROM ubicaciones;
