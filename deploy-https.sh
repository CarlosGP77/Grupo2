#!/usr/bin/env bash
set -euo pipefail

# deploy-https.sh
# Genera (opcional) un keystore PKCS12 y despliega la app con Docker Compose usando HTTPS.
# Usage:
#   ./deploy-https.sh            # genera keystore si no existe y despliega
#   ./deploy-https.sh --force    # fuerza regeneración del keystore
#   ./deploy-https.sh --domain example.com --keystore ./certs/keystore.p12

REPO_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$REPO_DIR"

# Defaults
KEYSTORE_DIR="${KEYSTORE_DIR:-$REPO_DIR/certs}"
KEYSTORE_PATH="${KEYSTORE_PATH:-$KEYSTORE_DIR/keystore.p12}"
STOREPASS="${STOREPASS:-Admin_123}"
KEYALG="${KEYALG:-RSA}"
KEYSIZE="${KEYSIZE:-2048}"
STORETYPE="${STORETYPE:-PKCS12}"
ALIAS="${ALIAS:-springboot}"
DNAME="${DNAME:-CN=localhost, OU=Dev, O=Grupo2, L=City, S=State, C=ES}"
FORCE="false"
DOMAIN="localhost"

print_usage() {
  cat <<EOF
Usage: $0 [options]

Options:
  --force            Regenera el keystore aunque ya exista
  --domain <domain>  CN / CommonName para el certificado (default: localhost)
  --keystore <path>  Ruta del keystore a crear/montar (default: ./certs/keystore.p12)
  --help             Mostrar esta ayuda

Ejemplo:
  $0 --domain ejemplo.com
EOF
}

# Parse args
while [[ $# -gt 0 ]]; do
  case "$1" in
    --force) FORCE="true"; shift ;;
    --domain) DOMAIN="$2"; shift 2 ;;
    --keystore) KEYSTORE_PATH="$2"; shift 2 ;;
    --help) print_usage; exit 0 ;;
    *) echo "Unknown arg: $1"; print_usage; exit 1 ;;
  esac
done

KEYSTORE_DIR="$(dirname "$KEYSTORE_PATH")"

echo "Repository: $REPO_DIR"
echo "Keystore: $KEYSTORE_PATH"
echo "Domain (CN): $DOMAIN"

# Ensure certs directory exists
mkdir -p "$KEYSTORE_DIR"

if [[ -f "$KEYSTORE_PATH" && "$FORCE" != "true" ]]; then
  echo "Keystore already exists at $KEYSTORE_PATH (use --force to regenerate)."
else
  echo "Generating PKCS12 keystore at $KEYSTORE_PATH..."
  # Build distinguished name using domain
  DNAME="CN=${DOMAIN}, OU=Dev, O=Grupo2, L=City, S=State, C=ES"

  # Generate keystore (non-interactive)
  keytool -genkeypair \
    -alias "$ALIAS" \
    -keyalg "$KEYALG" \
    -keysize "$KEYSIZE" \
    -storetype "$STORETYPE" \
    -keystore "$KEYSTORE_PATH" \
    -storepass "$STOREPASS" \
    -keypass "$STOREPASS" \
    -dname "$DNAME"

  echo "Keystore created."
fi

# Ensure keystore permissions are safe
chmod 600 "$KEYSTORE_PATH" || true

# Pull latest, build and deploy
echo "Updating repository and deploying with Docker Compose..."

# If there are local changes we avoid overwriting them – but this script will pull latest
git pull --rebase || true

# Stop running compose but keep volumes (DB data kept) to avoid data loss unless user removed -v
docker compose down || true

# Build and start
docker compose build --no-cache
docker compose up -d

# Follow logs
echo "Tailing logs for grupo2-app (Ctrl+C to stop):"
docker logs -f grupo2-app

