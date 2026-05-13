#!/bin/bash

# ============================================
# MouroSubV2 - Script de Ejecución para Linux
# ============================================
# Compatible: Debian, Ubuntu, CentOS, Fedora
# ============================================

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# ============================================
# Funciones
# ============================================

print_header() {
    echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║${NC}                                                            ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}             ${GREEN}MouroSubV2 - Sistema de Gestión${NC}              ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}                                                            ${BLUE}║${NC}"
    echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
    echo ""
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# ============================================
# Validaciones
# ============================================

print_header

echo "Validando requisitos..."
echo ""

# Verificar Docker
if ! command -v docker &> /dev/null; then
    print_error "Docker no está instalado"
    echo "Instala Docker desde: https://docs.docker.com/engine/install/"
    exit 1
fi
print_success "Docker encontrado: $(docker --version)"

# Verificar Docker Compose
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose no está instalado"
    echo "Instala Docker Compose desde: https://docs.docker.com/compose/install/"
    exit 1
fi
print_success "Docker Compose encontrado: $(docker-compose --version)"

# Verificar Docker daemon
if ! docker ps &> /dev/null; then
    print_error "Docker daemon no está ejecutándose"
    echo "Inicia Docker con: sudo systemctl start docker"
    exit 1
fi
print_success "Docker daemon ejecutándose"

echo ""
echo "════════════════════════════════════════════════════════════"
echo ""

# ============================================
# Crear archivo .env si no existe
# ============================================

if [ ! -f ".env" ]; then
    print_info "Creando archivo .env desde .env.example"
    cp .env.example .env
    print_success "Archivo .env creado"
fi

echo ""

# ============================================
# Opções de ejecución
# ============================================

echo "Opciones:"
echo "  1) Iniciar aplicación (docker-compose up -d)"
echo "  2) Ver logs en tiempo real"
echo "  3) Detener aplicación (docker-compose down)"
echo "  4) Reiniciar aplicación"
echo "  5) Limpiar todo (docker-compose down -v)"
echo ""

read -p "Selecciona opción [1-5]: " option

case $option in
    1)
        print_info "Iniciando aplicación..."
        docker-compose up -d
        echo ""
        print_success "Aplicación iniciada"
        echo ""
        echo "════════════════════════════════════════════════════════════"
        echo ""
        print_info "Esperando 15 segundos para que inicie..."
        sleep 15
        echo ""
        print_success "✓ Aplicación disponible en: http://localhost:8080"
        echo ""
        echo "Credenciales de prueba:"
        echo "  👑 Admin:       admin / admin123"
        echo "  ✅ Verificador: verificador / verificador123"
        echo "  👤 Usuario:     usuario1 / usuario123"
        echo ""
        print_info "Ver logs:     docker-compose logs app -f"
        print_info "Detener:      docker-compose down"
        echo ""
        ;;
    2)
        print_info "Mostrando logs en tiempo real..."
        echo "(Presiona Ctrl+C para salir)"
        sleep 2
        docker-compose logs app -f
        ;;
    3)
        print_info "Deteniendo aplicación..."
        docker-compose down
        print_success "Aplicación detenida"
        ;;
    4)
        print_info "Reiniciando aplicación..."
        docker-compose restart
        print_success "Aplicación reiniciada"
        ;;
    5)
        read -p "¿Estás seguro? Se eliminarán todos los datos (s/n): " confirm
        if [ "$confirm" = "s" ] || [ "$confirm" = "S" ]; then
            print_info "Limpiando todo..."
            docker-compose down -v
            print_success "Limpieza completada"
        else
            print_info "Cancelado"
        fi
        ;;
    *)
        print_error "Opción no válida"
        exit 1
        ;;
esac

echo ""
echo "════════════════════════════════════════════════════════════"
echo ""



