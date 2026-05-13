@echo off
REM Script para ejecutar MouroSubV2 con Docker en Windows

echo ===============================================
echo MouroSubV2 - Sistema de Gestion de Cursos
echo ===============================================
echo.

REM Verificar si Docker está instalado
docker --version >nul 2>&1
if errorlevel 1 (
    echo X Docker no esta instalado.
    echo Instala Docker desde: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

echo X Docker encontrado
echo.

REM Crear la aplicacion
echo Construyendo la aplicacion...
docker-compose up -d

echo.
echo ===============================================
echo X Aplicacion iniciada
echo ===============================================
echo.
echo Accede a: http://localhost:8080
echo.
echo Credenciales de prueba:
echo   Admin: admin / admin123
echo   Verificador: verificador / verificador123
echo   Usuario: usuario1 / usuario123
echo.
echo Para detener: docker-compose down
echo Para ver logs: docker-compose logs -f
echo.
pause

