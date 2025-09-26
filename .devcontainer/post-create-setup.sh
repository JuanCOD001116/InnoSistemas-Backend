#!/bin/bash

echo "🔧 Configuración post-creación del DevContainer..."

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }

# Navegar al directorio correcto del proyecto
WORKSPACE_ROOT="/workspaces/InnoSistemas-Backend"
PROJECT_DIR="$WORKSPACE_ROOT/InnoSistemas/InnoSistemas"

if [ ! -d "$PROJECT_DIR" ]; then
    log_warn "No se encontró el directorio del proyecto: $PROJECT_DIR"
    exit 1
fi

cd "$PROJECT_DIR"

# Hacer executable el script principal
log_info "Configurando permisos de scripts..."
if [ -f "setup-dev-environment.sh" ]; then
    chmod +x setup-dev-environment.sh
    log_info "✅ Script setup-dev-environment.sh configurado"
else
    log_warn "⚠️ No se encontró setup-dev-environment.sh"
fi

# Hacer executable el script de verificación de dependencias
if [ -f "check-dependencies.sh" ]; then
    chmod +x check-dependencies.sh
    log_info "✅ Script check-dependencies.sh configurado"
fi

# Hacer executable el wrapper de Maven
if [ -f "mvnw" ]; then
    chmod +x mvnw
    log_info "✅ Maven wrapper configurado"
else
    log_warn "⚠️ No se encontró mvnw"
fi

# Crear directorio .m2 si no existe
log_info "Configurando directorios de Maven..."
mkdir -p ~/.m2
log_info "✅ Directorio .m2 configurado"

# Crear directorio target si no existe
mkdir -p target
log_info "✅ Directorio target configurado"

echo ""
echo "✅ Configuración post-creación completada"
echo "📝 Para configurar el entorno completo, ejecuta:"
echo "   cd InnoSistemas/InnoSistemas"
echo "   ./setup-dev-environment.sh"
echo ""