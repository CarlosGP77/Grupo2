# Springboot X CRUD

Aplicacion web de ejemplo con Spring Boot + Thymeleaf + JPA para gestionar usuarios (alta, listado, edicion y borrado).

- **Controlador:** `UserController`
- **Servicio:** `UserService`
- **Repositorio:** `UserRepository`
- **Modelo:** `Usuario`
- **Vistas Thymeleaf:** `index.html`, `listatodos.html`, `form.html`

Rutas principales:

- `GET /` -> pagina de inicio
- `GET /listausuarios` -> lista de usuarios
- `GET /nuevousuario` -> formulario de alta
- `GET /editarusuario/{id}` -> formulario de edicion
- `GET /borrarusuario/{id}` -> elimina un usuario
- `POST /guardarusuario` -> guarda (crear/actualizar)

## Requisitos

- Java instalado
- Maven instalado
- MariaDB (o MySQL compatible)

## Configuracion de base de datos

Revisa `src/main/resources/application.properties` y ajusta estos valores segun tu entorno:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

Por defecto, el proyecto usa:

- Base de datos: `demo_db`
- Usuario: `root`
- Password: `root123`

> Importante: cambia esas credenciales para tu entorno local o de produccion.

## Como desplegarlo (local)

1. Clona el repositorio.
2. Crea la base de datos `demo_db` en tu motor MariaDB/MySQL.
3. Configura credenciales en `application.properties`.
4. Ejecuta la app:

```bash
mvn spring-boot:run
```

5. Abre en el navegador:

- `http://localhost:8080/`

Con `spring.jpa.hibernate.ddl-auto=update`, Hibernate crea/actualiza la tabla automaticamente en desarrollo.

## Uso como template

Este repositorio tambien funciona como **template base** para proyectos web con Spring Boot.

Puedes reutilizarlo para acelerar nuevos desarrollos:

1. Copia la estructura MVC (`controller`, `service`, `repository`, `model`).
2. Sustituye `Usuario` por tu nueva entidad de negocio.
3. Crea tus vistas Thymeleaf siguiendo el mismo patron de formulario/listado.
4. Ajusta rutas y logica del controlador segun tu caso.
5. Cambia configuracion de base de datos y nombre del proyecto en `pom.xml`.

## Estructura rapida

```text
src/main/java/com/example/demo/
  controller/
  service/
  repository/
  model/
src/main/resources/templates/
  index.html
  listatodos.html
  form.html
```

