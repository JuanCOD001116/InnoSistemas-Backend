# InnoSistemas-Backend

## 🚀 Configuración Rápida (Nueva máquina/DevContainer)

### 1. **Clonar y abrir en DevContainer**
```bash
# Clonar el repositorio
git clone https://github.com/JuanCOD001116/InnoSistemas-Backend.git
cd InnoSistemas-Backend

# Abrir en VS Code con DevContainer
code .
# Seleccionar "Reopen in Container" cuando aparezca el popup
```

### 2. **Configuración automática**
```bash
# Una vez dentro del DevContainer, navegar al proyecto principal
cd InnoSistemas/InnoSistemas

# Ejecutar script de configuración automática
./setup-dev-environment.sh
```

### 3. **Verificar instalación**
```bash
# Verificar que todo esté funcionando
curl http://localhost:8080/health

# Probar login con usuarios por defecto
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

## 👥 **Usuarios por defecto**

El sistema incluye tres usuarios preconfigurados:

1. **👤 Estudiante**: `estudiante` / `1234` (ROLE_STUDENT)
2. **👨‍🏫 Profesor**: `profesor` / `prof123` (ROLE_TEACHER)  
3. **👑 Admin**: `admin` / `admin123` (ROLE_STUDENT + ROLE_TEACHER)

## ⚡ **Si algo falla...**

```bash
# Verificar dependencias del sistema
./check-dependencies.sh

# Reiniciar servicios Docker
cd ../../.devcontainer
docker-compose -f docker-compose.dev.yml down
docker-compose -f docker-compose.dev.yml up -d

# Volver a ejecutar configuración
cd ../InnoSistemas/InnoSistemas
./setup-dev-environment.sh
```

## 🔧 **Configuración manual (sin DevContainer)**

### Variables de entorno requeridas

Para desarrollo local, el sistema usa PostgreSQL en Docker. Si no usas DevContainer:

```bash
# Variables para desarrollo local
export DB_URL="jdbc:postgresql://localhost:5433/innosistemas_dev"
export DB_USERNAME="dev_user"
export DB_PASSWORD="dev_password"
export JWT_SECRET="dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi1hbmQtdmFsaWRhdGlvbi0xMjM0NTY3ODkw"

# Ejecutar aplicación
./mvnw spring-boot:run
```

### Configuración manual de base de datos

```bash
# Iniciar PostgreSQL con Docker
docker run -d \
  --name innosistemas-db \
  -e POSTGRES_DB=innosistemas_dev \
  -e POSTGRES_USER=dev_user \
  -e POSTGRES_PASSWORD=dev_password \
  -p 5433:5432 \
  postgres:15-alpine
```

## 📁 **Estructura del proyecto**

```
InnoSistemas-Backend/
├── .devcontainer/          # Configuración DevContainer
│   ├── Dockerfile
│   ├── devcontainer.json
│   ├── docker-compose.dev.yml
│   └── post-create-setup.sh
├── InnoSistemas/
│   └── InnoSistemas/       # Proyecto Spring Boot principal
│       ├── src/main/java/
│       ├── src/main/resources/
│       ├── setup-dev-environment.sh  # Script de configuración
│       ├── check-dependencies.sh     # Verificación de dependencias
│       └── pom.xml
└── README.md
```

## 🛠️ **Servicios incluidos**

- **Spring Boot 3.5.5** - Framework principal
- **PostgreSQL 15** - Base de datos (puerto 5433)
- **Adminer** - Administrador web DB (puerto 8081)
- **JWT Authentication** - Autenticación basada en tokens
- **Flyway** - Migraciones automáticas de BD
- **Swagger/OpenAPI** - Documentación de API

## 🔐 **Seguridad y secretos**

El archivo `src/main/resources/application-secrets.properties` se crea automáticamente con valores seguros para desarrollo local. Para producción:

- **DB_PASSWORD**: contraseña real de la base de datos
- **DB_URL**: URL de la base de datos de producción  
- **JWT_SECRET**: clave secreta más fuerte (mínimo 256 bits)

### Manejo de secretos (recomendaciones)
- ❌ No commitees contraseñas en repositorios
- ✅ Usa variables de entorno o Secret Manager
- ✅ Para Docker: usa `--env-file` o Docker Secrets
- ✅ Para Kubernetes: usa `Secret` resources
- ✅ Para CI/CD: almacena en secrets del proveedor
- ✅ Rota contraseñas periódicamente
- ✅ Usa `sslmode=require` en producción