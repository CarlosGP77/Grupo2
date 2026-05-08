# Guía: conectar Spring Boot con una base de datos en otro contenedor Docker

Esta guía explica cómo ejecutar la aplicación **Spring Boot** en un contenedor Docker y conectar la base de datos en **otro contenedor distinto**. También toma como referencia el **modelo de la imagen adjunta** para proponer un esquema relacional y explicar cómo llevarlo a JPA/Hibernate.

---

## 1. Objetivo

La idea es separar la aplicación y la base de datos:

- **Contenedor 1:** la web Spring Boot.
- **Contenedor 2:** la base de datos MariaDB/MySQL.
- **Opcional:** un proxy como nginx o Apache si se publica la app en internet.

Esto permite:

- desplegar más fácil en Docker;
- cambiar credenciales por variables de entorno;
- mantener la BD persistente con volúmenes;
- usar la misma app en desarrollo, pruebas y producción.

---

## 2. Cómo funciona la conexión

Spring Boot no debe usar `localhost` para conectarse a la base de datos cuando ambos servicios están en Docker.

Dentro de Docker, cada contenedor ve al otro por el **nombre del servicio** definido en `docker-compose.yml`.

Por ejemplo:

- `db` → contenedor de MariaDB/MySQL
- `app` → contenedor de Spring Boot

Entonces la URL JDBC debe apuntar a `db` y no a `localhost`:

```properties
spring.datasource.url=jdbc:mariadb://db:3306/tu_base_de_datos
```

---

## 3. Modelo de base de datos según la imagen

La imagen muestra varias entidades y relaciones. Una forma razonable de traducirla a tablas sería esta:

### Tablas principales

#### `usuarios`
Representa a los usuarios del sistema.

Campos sugeridos:
- `dni` (PK)
- `username`
- `userlastname`
- `email`
- `licencia`

#### `instructores`
Representa a los instructores.

Campos sugeridos:
- `dni` (PK)
- `nombre`
- `tipo_licencia`
- `disponibilidad`

#### `reservas`
Representa las reservas hechas por usuarios.

Campos sugeridos:
- `id_reserva` (PK)
- `estado`
- `fecha_hora`
- `id_tipo` (FK)
- `id_ubicacion` (FK, si aplica)

#### `tipo_reserva`
Catálogo de tipos de reserva.

Campos sugeridos:
- `id_tipo` (PK)
- `nombre`
- `descripcion`

#### `inmersiones`
Catálogo de ubicaciones o inmersiones.

Campos sugeridos:
- `id_ubicacion` (PK)
- `nombre`
- `descripcion`

### Tablas intermedias para relaciones N:M

La imagen indica relaciones muchos-a-muchos:

- un usuario puede pedir varias reservas;
- una reserva puede estar asociada a varios usuarios;
- un instructor puede impartir varias reservas;
- una reserva puede tener varios instructores.

Para eso se usan tablas puente:

#### `usuario_reserva`
- `dni_usuario` (FK)
- `id_reserva` (FK)

#### `instructor_reserva`
- `dni_instructor` (FK)
- `id_reserva` (FK)

---

## 4. Ejemplo de esquema relacional SQL

```sql
CREATE TABLE tipo_reserva (
    id_tipo BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE inmersiones (
    id_ubicacion BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE usuarios (
    dni VARCHAR(20) PRIMARY KEY,
    username VARCHAR(80) NOT NULL,
    userlastname VARCHAR(120),
    email VARCHAR(120),
    licencia VARCHAR(50)
);

CREATE TABLE instructores (
    dni VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    tipo_licencia VARCHAR(50),
    disponibilidad VARCHAR(100)
);

CREATE TABLE reservas (
    id_reserva BIGINT AUTO_INCREMENT PRIMARY KEY,
    estado VARCHAR(30) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    id_tipo BIGINT NOT NULL,
    id_ubicacion BIGINT NULL,
    CONSTRAINT fk_reserva_tipo FOREIGN KEY (id_tipo) REFERENCES tipo_reserva(id_tipo),
    CONSTRAINT fk_reserva_ubicacion FOREIGN KEY (id_ubicacion) REFERENCES inmersiones(id_ubicacion)
);

CREATE TABLE usuario_reserva (
    dni_usuario VARCHAR(20) NOT NULL,
    id_reserva BIGINT NOT NULL,
    PRIMARY KEY (dni_usuario, id_reserva),
    CONSTRAINT fk_ur_usuario FOREIGN KEY (dni_usuario) REFERENCES usuarios(dni),
    CONSTRAINT fk_ur_reserva FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva)
);

CREATE TABLE instructor_reserva (
    dni_instructor VARCHAR(20) NOT NULL,
    id_reserva BIGINT NOT NULL,
    PRIMARY KEY (dni_instructor, id_reserva),
    CONSTRAINT fk_ir_instructor FOREIGN KEY (dni_instructor) REFERENCES instructores(dni),
    CONSTRAINT fk_ir_reserva FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva)
);
```

> Nota: el esquema exacto puede cambiar según cómo quieras interpretar la imagen. Si la lógica de negocio exige otra relación, puedes ajustar las FK o mover algunos campos a otras tablas.

---

## 5. Configuración de Spring Boot

En este proyecto, la conexión debe ir en `src/main/resources/application.properties` o, mejor aún, usando variables de entorno.

### Opción recomendada: variables de entorno

```properties
spring.application.name=tienda
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
```

### Ejemplo de `.env`

```env
DB_URL=jdbc:mariadb://db:3306/reservas_db
DB_USER=root
DB_PASSWORD=root123
MARIADB_DATABASE=reservas_db
MARIADB_ROOT_PASSWORD=root123
```

---

## 6. Ejemplo de `docker-compose.yml`

Este ejemplo levanta la aplicación y la base de datos por separado.

```yaml
services:
  db:
    image: mariadb:11.4
    container_name: bd_reservas
    restart: unless-stopped
    environment:
      MARIADB_DATABASE: reservas_db
      MARIADB_ROOT_PASSWORD: root123
    ports:
      - "3306:3306"
    volumes:
      - mariadb_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "healthcheck.sh", "--connect", "--innodb_initialized"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build: .
    container_name: web_springboot
    restart: unless-stopped
    ports:
      - "8080:8080"
    depends_on:
      db:
        condition: service_healthy
    environment:
      DB_URL: jdbc:mariadb://db:3306/reservas_db
      DB_USER: root
      DB_PASSWORD: root123

volumes:
  mariadb_data:
```

---

## 7. Ejemplo de `Dockerfile` para Spring Boot

```dockerfile
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

> Antes de usar este `Dockerfile`, ejecuta `mvn clean package` para generar el `.jar`.

---

## 8. Pasos para desplegar

### 8.1. Compilar la aplicación

```bash
mvn clean package
```

### 8.2. Levantar los contenedores

```bash
docker compose up -d --build
```

### 8.3. Verificar logs

```bash
docker compose logs -f app
```

### 8.4. Abrir la aplicación

- Web: `http://localhost:8080/`
- Base de datos: `localhost:3306`

---

## 9. Si la app va en un alojamiento web

Si más adelante publicas la web en un hosting o servidor externo:

- deja Spring Boot como backend principal;
- pon nginx o Apache delante solo si necesitas HTTPS, dominio o proxy inverso;
- usa el nombre del servicio Docker para conectar a la BD interna;
- no pongas credenciales reales dentro del repositorio.

---

## 10. Buenas prácticas

- No usar `localhost` dentro de Docker para la BD.
- Separar la app y la base de datos en contenedores distintos.
- Guardar credenciales en variables de entorno o `.env`.
- Usar volúmenes para que la BD no pierda datos al reiniciar.
- Activar `restart: unless-stopped` para mayor estabilidad.
- Revisar la interpretación del modelo de la imagen antes de crear las entidades JPA finales.

---

## 11. Relación con el proyecto actual

Este repositorio ya usa Spring Boot + JPA. Actualmente tiene las entidades:

- `Pedido`
- `Producto`

Si quieres llevarlo al modelo de la imagen, tendrás que cambiar o añadir entidades nuevas como:

- `Usuario`
- `Instructor`
- `Reserva`
- `TipoReserva`
- `Inmersion`

Y después crear sus repositorios, servicios y controladores.

---

## 12. Resumen

La forma correcta de conectar este proyecto es:

1. correr Spring Boot en un contenedor Docker;
2. correr la base de datos en otro contenedor;
3. conectar Spring Boot a la BD usando el nombre del servicio Docker;
4. usar variables de entorno para credenciales;
5. adaptar el modelo relacional de la imagen a entidades JPA.

Con esto tienes una arquitectura limpia, escalable y fácil de desplegar.

