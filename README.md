# Grupo2

## Ejecución con Docker

El proyecto levanta:

- `mariadb` para la base de datos
- `app` con Spring Boot escuchando en `80` internamente
- `proxy` con Nginx y certificado autofirmado en `443`

### Arrancar

```powershell
docker compose up -d --build
```

### Acceso

Abre:

```text
https://localhost
```

El navegador mostrará una advertencia porque el certificado es autofirmado.

### Nota

Si quieres generar el certificado con otro nombre DNS o IP, define `SSL_CN` antes de construir:

```powershell
$env:SSL_CN="tu-servidor"
docker compose up -d --build
```
