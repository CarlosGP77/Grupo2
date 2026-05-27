#!/bin/bash

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}╔════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║   GRUPO2 - DOCKER DEPLOYMENT SCRIPT    ║${NC}"
echo -e "${YELLOW}╚════════════════════════════════════════╝${NC}"

# Verificar que docker-compose existe
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}✗ docker-compose no está instalado${NC}"
    exit 1
fi

echo -e "${GREEN}✓ docker-compose encontrado${NC}"

# Verificar que estamos en el directorio correcto
if [ ! -f "docker-compose.yml" ]; then
    echo -e "${RED}✗ Error: No se encuentra docker-compose.yml${NC}"
    echo "Ejecuta este script desde el directorio raíz del proyecto"
    exit 1
fi

echo -e "${GREEN}✓ docker-compose.yml encontrado${NC}"

# Detener servicios antiguos
echo -e "\n${YELLOW}[1/5] Deteniendo servicios antiguos...${NC}"
docker-compose down

# Limpiar imágenes no usadas (opcional)
echo -e "\n${YELLOW}[2/5] Limpiando imágenes no usadas...${NC}"
docker image prune -f --filter "until=24h" || true

# Construir imagen
echo -e "\n${YELLOW}[3/5] Construyendo imagen Docker...${NC}"
docker-compose build --no-cache

# Iniciar servicios
echo -e "\n${YELLOW}[4/5] Iniciando servicios...${NC}"
docker-compose up -d

# Esperar a que todo esté listo
echo -e "\n${YELLOW}[5/5] Esperando a que los servicios estén listos...${NC}"
sleep 10

# Verificar que todo está corriendo
echo -e "\n${YELLOW}═══════════════════════════════════════${NC}"
echo -e "${YELLOW}ESTADO DE SERVICIOS:${NC}"
echo -e "${YELLOW}═══════════════════════════════════════${NC}\n"

docker-compose ps

# Mostrar URLs
echo -e "\n${GREEN}╔════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║   ACCESO A SERVICIOS                   ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════╝${NC}"
echo -e ""
echo -e "${GREEN}Aplicación:${NC}"
echo -e "  http://localhost:8080"
echo -e ""
echo -e "${GREEN}Verificador de Imágenes:${NC}"
echo -e "  http://localhost:8080/verificador.html"
echo -e ""
echo -e "${GREEN}API REST:${NC}"
echo -e "  http://localhost:8080/api/verificador"
echo -e ""
echo -e "${GREEN}Nextcloud (Almacenamiento):${NC}"
echo -e "  http://localhost:8888"
echo -e "  Usuario: admin"
echo -e "  Contraseña: admin_123"
echo -e ""
echo -e "${YELLOW}═══════════════════════════════════════${NC}"

# Mostrar logs iniciales
echo -e "\n${YELLOW}Mostrando logs de la aplicación (Ctrl+C para salir):${NC}\n"
docker-compose logs -f grupo2-app
