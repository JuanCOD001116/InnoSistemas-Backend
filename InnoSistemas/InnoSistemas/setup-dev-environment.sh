#!/bin/bash

echo "🚀 Configurando entorno de desarrollo InnoSistemas..."

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# Verificar servicios de Docker
log_info "Verificando servicios de Docker..."
if ! docker ps | grep -q "innosistemas"; then
    log_warn "Iniciando servicios de Docker..."
    cd ../../.devcontainer
    docker-compose -f docker-compose.dev.yml up -d
    cd ../InnoSistemas/InnoSistemas
    sleep 10
fi

# Crear archivo de secretos si no existe
SECRETS_FILE="src/main/resources/application-secrets.properties"
if [ ! -f "$SECRETS_FILE" ]; then
    log_info "Creando archivo de configuración..."
    mkdir -p src/main/resources
    cat > "$SECRETS_FILE" << 'EOF'
DB_PASSWORD=dev_password
DB_USERNAME=dev_user
DB_URL=jdbc:postgresql://localhost:5433/innosistemas_dev
JWT_SECRET=dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi1hbmQtdmFsaWRhdGlvbi0xMjM0NTY3ODkw
JWT_EXP_MINUTES=60
EOF
    log_info "✅ Archivo de configuración creado"
fi

# Verificar PostgreSQL
log_info "Verificando PostgreSQL..."
until pg_isready -h localhost -p 5433 -U dev_user; do
    log_warn "PostgreSQL no está listo, esperando..."
    sleep 2
done
log_info "PostgreSQL disponible!"

# Verificar aplicación
if pgrep -f "spring-boot:run" > /dev/null; then
    log_info "✅ Aplicación Spring Boot ya está ejecutándose"
else
    log_info "Iniciando aplicación Spring Boot..."
    chmod +x mvnw
    nohup ./mvnw spring-boot:run > app.log 2>&1 &
    sleep 15
    log_info "✅ Aplicación iniciada"
fi

# Verificar endpoints
log_info "Verificando endpoints..."
if curl -s http://localhost:8080/health > /dev/null; then
    log_info "✅ Health check: OK"
fi

if curl -s -X POST http://localhost:8080/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username": "admin", "password": "admin123"}' | grep -q "token"; then
    log_info "✅ Login: OK"
fi

echo ""
log_info "🎉 ¡Entorno completamente configurado!"
echo ""
log_info "📋 URLs importantes:"
log_info "  🌐 API: http://localhost:8080"
log_info "  📚 Swagger: http://localhost:8080/swagger-ui.html"
log_info "  🗄️ Adminer: http://localhost:8081"
echo ""
log_info "👤 Usuarios de prueba:"
log_info "  estudiante / 1234"
log_info "  profesor / prof123"
log_info "  admin / admin123"
echo ""
