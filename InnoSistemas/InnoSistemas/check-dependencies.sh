#!/bin/bash

echo "🔍 Verificando dependencias del sistema..."

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

log_info() { echo -e "${GREEN}✅${NC} $1"; }
log_warn() { echo -e "${YELLOW}⚠️${NC} $1"; }
log_error() { echo -e "${RED}❌${NC} $1"; }

ERRORS=0

# Verificar Docker
if ! command -v docker &> /dev/null; then
    log_error "Docker no está instalado"
    ERRORS=$((ERRORS + 1))
else
    log_info "Docker disponible: $(docker --version | head -n 1)"
fi

# Verificar Docker Compose
if ! command -v docker-compose &> /dev/null; then
    log_error "Docker Compose no está instalado"
    ERRORS=$((ERRORS + 1))
else
    log_info "Docker Compose disponible: $(docker-compose --version | head -n 1)"
fi

# Verificar Java
if ! command -v java &> /dev/null; then
    log_error "Java no está instalado"
    ERRORS=$((ERRORS + 1))
else
    log_info "Java disponible: $(java -version 2>&1 | head -n 1)"
fi

# Verificar Maven
if ! command -v mvn &> /dev/null; then
    log_warn "Maven no está instalado globalmente (pero se puede usar ./mvnw)"
else
    log_info "Maven disponible: $(mvn -version 2>&1 | head -n 1)"
fi

# Verificar psql
if ! command -v psql &> /dev/null; then
    log_error "PostgreSQL client no está instalado"
    ERRORS=$((ERRORS + 1))
else
    log_info "PostgreSQL client disponible: $(psql --version)"
fi

# Verificar pg_isready
if ! command -v pg_isready &> /dev/null; then
    log_error "pg_isready no está instalado"
    ERRORS=$((ERRORS + 1))
else
    log_info "pg_isready disponible"
fi

# Verificar curl
if ! command -v curl &> /dev/null; then
    log_error "curl no está instalado"
    ERRORS=$((ERRORS + 1))
else
    log_info "curl disponible: $(curl --version | head -n 1)"
fi

# Verificar jq (opcional pero recomendado)
if ! command -v jq &> /dev/null; then
    log_warn "jq no está instalado (recomendado para pruebas JSON)"
else
    log_info "jq disponible: $(jq --version)"
fi

# Verificar git
if ! command -v git &> /dev/null; then
    log_error "Git no está instalado"
    ERRORS=$((ERRORS + 1))
else
    log_info "Git disponible: $(git --version)"
fi

echo ""
if [ $ERRORS -eq 0 ]; then
    log_info "🎉 Todas las dependencias críticas están instaladas"
    exit 0
else
    log_error "❌ Se encontraron $ERRORS errores críticos"
    echo ""
    echo "💡 Soluciones sugeridas:"
    echo "  - Asegúrate de estar usando el DevContainer correcto"
    echo "  - Ejecuta: sudo apt-get update && sudo apt-get install -y postgresql-client"
    echo "  - Para jq: sudo apt-get install -y jq"
    exit 1
fi