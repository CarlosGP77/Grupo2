-- Insertar certificados de prueba para los usuarios por defecto
-- Estos certificados son de prueba para demostración

-- Para el usuario admin@mourosub.com (DNI: 1111111)
INSERT INTO verified_images (id_usuario, filename, nextcloud_path, upload_date, status, file_size, mime_type)
SELECT u.id_usuario, 'Certificado_Admin_Buzo.pdf', 'Certificados/1111111/Certificado_Admin_Buzo.pdf', NOW(), 'APPROVED', 245600, 'application/pdf'
FROM usuarios u WHERE u.email = 'admin@mourosub.com'
ON DUPLICATE KEY UPDATE filename = filename;

INSERT INTO verified_images (id_usuario, filename, nextcloud_path, upload_date, status, file_size, mime_type)
SELECT u.id_usuario, 'Certificado_Admin_Advanced.pdf', 'Certificados/1111111/Certificado_Admin_Advanced.pdf', NOW(), 'APPROVED', 312400, 'application/pdf'
FROM usuarios u WHERE u.email = 'admin@mourosub.com'
ON DUPLICATE KEY UPDATE filename = filename;

-- Para el usuario prueba@prueba.com (DNI: prueba)
INSERT INTO verified_images (id_usuario, filename, nextcloud_path, upload_date, status, file_size, mime_type)
SELECT u.id_usuario, 'Certificado_Prueba_Basico.pdf', 'Certificados/prueba/Certificado_Prueba_Basico.pdf', NOW(), 'PENDING', 198500, 'application/pdf'
FROM usuarios u WHERE u.email = 'prueba@prueba.com'
ON DUPLICATE KEY UPDATE filename = filename;

INSERT INTO verified_images (id_usuario, filename, nextcloud_path, upload_date, status, file_size, mime_type)
SELECT u.id_usuario, 'Certificado_Prueba_Intermedio.pdf', 'Certificados/prueba/Certificado_Prueba_Intermedio.pdf', NOW(), 'APPROVED', 267300, 'application/pdf'
FROM usuarios u WHERE u.email = 'prueba@prueba.com'
ON DUPLICATE KEY UPDATE filename = filename;

INSERT INTO verified_images (id_usuario, filename, nextcloud_path, upload_date, status, file_size, mime_type)
SELECT u.id_usuario, 'Certificado_Prueba_Avanzado.jpg', 'Certificados/prueba/Certificado_Prueba_Avanzado.jpg', NOW(), 'APPROVED', 445200, 'image/jpeg'
FROM usuarios u WHERE u.email = 'prueba@prueba.com'
ON DUPLICATE KEY UPDATE filename = filename;

