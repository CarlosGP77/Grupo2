#!/bin/bash

# ============================================
# MouroSubV2 - Gestor de Producción para Debian
# ============================================
# Uso: sudo ./deploy.sh

set -e

# ============================================
# Colores
# ============================================
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m'

# ============================================
# Configuración
# ============================================
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_NAME="mourosubv2"
COMPOSE_FILE="${SCRIPT_DIR}/docker-compose.prod.yml"
ENV_FILE="${SCRIPT_DIR}/.env"
BACKUP_DIR="${SCRIPT_DIR}/backups"
LOG_DIR="${SCRIPT_DIR}/logs"

# ============================================
# Funciones
# ============================================

header() {
    clear
    echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║${NC}                                                            ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}   ${CYAN}${APP_NAME}${NC} ${GREEN}v1.0.0${NC} - Gestor de Producción              ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}                                                            ${BLUE}║${NC}"
    echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
    echo ""
}

success() {
    echo -e "${GREEN}✓${NC} $1"
}

error() {
    echo -e "${RED}✗${NC} $1" >&2
}

info() {
    echo -e "${YELLOW}ℹ${NC} $1"
}

warn() {
    echo -e "${MAGENTA}⚠${NC} $1"
}

check_root() {
    if [[ $EUID -ne 0 ]]; then
        error "Este script debe ejecutarse con permisos de root"
        echo "Usa: sudo ./deploy.sh"
        exit 1
    fi
}

check_requirements() {
    header
    info "Verificando requisitos..."

    # Docker
    if ! command -v docker &> /dev/null; then
        error "Docker no está instalado"
        exit 1
    fi
    success "Docker: $(docker --version)"

    # Docker Compose
    if ! command -v docker-compose &> /dev/null; then
        error "Docker Compose no está instalado"
        exit 1
    fi
    success "Docker Compose: $(docker-compose --version)"

    # Docker daemon
    if ! docker ps &> /dev/null; then
        error "Docker daemon no está ejecutándose"
        exit 1
    fi
    success "Docker daemon ejecutándose"

    echo ""
}

create_directories() {
    mkdir -p "${BACKUP_DIR}"
    mkdir -p "${LOG_DIR}"
    mkdir -p "${SCRIPT_DIR}/data/mariadb"
    mkdir -p "${SCRIPT_DIR}/data/logs/mariadb"
    mkdir -p "${SCRIPT_DIR}/data/logs/app"
    mkdir -p "${SCRIPT_DIR}/config"
    success "Directorios creados"
}

create_env_file() {
    if [ ! -f "${ENV_FILE}" ]; then
        info "Creando archivo .env"
        cp "${SCRIPT_DIR}/.env.example" "${ENV_FILE}"
        success "Archivo .env creado"
        warn "EDITA ${ENV_FILE} CON TUS CREDENCIALES ANTES DE CONTINUAR"
        read -p "¿Continuar? (s/n): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Ss]$ ]]; then
            info "Abortado"
            exit 1
        fi
    fi
}

check_env_file() {
    if [ ! -f "${ENV_FILE}" ]; then
        error "Archivo .env no encontrado"
        exit 1
    fi
    success "Archivo .env encontrado"
}

build_images() {
    header
    info "Compilando imágenes Docker..."
    docker-compose -f "${COMPOSE_FILE}" build --no-cache
    success "Imágenes compiladas"
    echo ""
}

start_services() {
    header
    info "Iniciando servicios..."
    docker-compose -f "${COMPOSE_FILE}" up -d

    # Esperar a que MariaDB esté listo
    info "Esperando a que MariaDB esté listo..."
    sleep 15

    success "Servicios iniciados"
    echo ""

    # Mostrar estado
    docker-compose -f "${COMPOSE_FILE}" ps
    echo ""
}

stop_services() {
    header
    info "Deteniendo servicios..."
    docker-compose -f "${COMPOSE_FILE}" down
    success "Servicios detenidos"
    echo ""
}

restart_services() {
    header
    info "Reiniciando servicios..."
    docker-compose -f "${COMPOSE_FILE}" restart
    success "Servicios reiniciados"
    echo ""
}

backup_database() {
    header
    TIMESTAMP=$(date +%Y%m%d_%H%M%S)
    BACKUP_FILE="${BACKUP_DIR}/mourosubv2_${TIMESTAMP}.sql"

    info "Realizando backup de la base de datos..."
    docker exec ${APP_NAME}-mariadb-prod \
        mariadb-dump -u root -p$(grep DB_ROOT_PASSWORD ${ENV_FILE} | cut -d= -f2) \
        mourosubv2 > "${BACKUP_FILE}"

    success "Backup realizado: ${BACKUP_FILE}"
    ls -lh "${BACKUP_FILE}"
    echo ""
}

backup_volume() {
    header
    TIMESTAMP=$(date +%Y%m%d_%H%M%S)
    BACKUP_FILE="${BACKUP_DIR}/mariadb_volume_${TIMESTAMP}.tar.gz"

    info "Realizando backup del volumen..."
    docker run --rm \
        -v ${APP_NAME}_mariadb_data:/data \
        -v "${BACKUP_DIR}":/backup \
        alpine tar czf /backup/mariadb_volume_${TIMESTAMP}.tar.gz -C /data .

    success "Backup realizado: ${BACKUP_FILE}"
    ls -lh "${BACKUP_FILE}"
    echo ""
}

show_logs() {
    header
    echo "¿Qué logs deseas ver?"
    echo "1) Aplicación (tiempo real)"
    echo "2) MariaDB (tiempo real)"
    echo "3) Ambos (tiempo real)"
    echo "4) Últimos 100 líneas - App"
    echo "5) Últimos 100 líneas - BD"
    read -p "Selecciona [1-5]: " choice

    case $choice in
        1) docker-compose -f "${COMPOSE_FILE}" logs app -f ;;
        2) docker-compose -f "${COMPOSE_FILE}" logs mariadb -f ;;
        3) docker-compose -f "${COMPOSE_FILE}" logs -f ;;
        4) docker-compose -f "${COMPOSE_FILE}" logs app --tail=100 ;;
        5) docker-compose -f "${COMPOSE_FILE}" logs mariadb --tail=100 ;;
        *) error "Opción no válida" ;;
    esac
}

show_status() {
    header
    info "Estado de servicios:"
    docker-compose -f "${COMPOSE_FILE}" ps
    echo ""

    info "Consumo de recursos:"
    docker stats --no-stream
    echo ""
}

database_shell() {
    header
    DB_PASS=$(grep DB_ROOT_PASSWORD ${ENV_FILE} | cut -d= -f2)
    info "Conectando a MariaDB..."
    docker exec -it ${APP_NAME}-mariadb-prod mariadb -u root -p${DB_PASS}
}

menu_principal() {
    header
    echo "OPCIONES:"
    echo ""
    echo "  INSTALACIÓN Y DESPLIEGUE"
    echo "    1) Instalación inicial (build + start)"
    echo "    2) Compilar imágenes"
    echo "    3) Iniciar servicios"
    echo "    4) Detener servicios"
    echo "    5) Reiniciar servicios"
    echo ""
    echo "  MANTENIMIENTO"
    echo "    6) Backup de BD (SQL)"
    echo "    7) Backup de volumen"
    echo "    8) Ver estado"
    echo "    9) Ver logs"
    echo "    10) Conectar a MariaDB"
    echo ""
    echo "    0) Salir"
    echo ""
}

# ============================================
# MAIN
# ============================================

check_root
check_requirements
create_directories

while true; do
    menu_principal
    read -p "Selecciona opción: " option

    case $option in
        1)
            create_env_file
            build_images
            start_services
            success "¡Instalación completada!"
            info "Accede a http://localhost:8080"
            ;;
        2)
            check_env_file
            build_images
            ;;
        3)
            check_env_file
            start_services
            ;;
        4)
            stop_services
            ;;
        5)
            restart_services
            ;;
        6)
            backup_database
            ;;
        7)
            backup_volume
            ;;
        8)
            show_status
            ;;
        9)
            show_logs
            ;;
        10)
            database_shell
            ;;
        0)
            info "Saliendo..."
            exit 0
            ;;
        *)
            error "Opción no válida"
            ;;
    esac

    sleep 2
done

