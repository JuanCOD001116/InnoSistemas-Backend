# 🚀 InnoSistemas - Guía de Desarrollo

## Configuración del DevContainer

Este DevContainer está configurado para desarrollo con:
- ☕ **Java 21** con Maven
- 🐘 **PostgreSQL 15** 
- 🔐 **JWT Authentication**
- 🛠️ **Spring Boot DevTools**
- 📊 **Adminer** para administración de BD

## 🏃‍♂️ Cómo ejecutar el proyecto

### 1. Configurar credenciales de base de datos

Edita el archivo `/src/main/resources/application-secrets.properties`:

```properties
# Para desarrollo local con Docker
DB_PASSWORD=dev_password

# O para producción (reemplazar con credenciales reales)
DB_PASSWORD=tu_password_real

JWT_SECRET=dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi1hbmQtdmFsaWRhdGlvbi0xMjM0NTY3ODkw
JWT_EXP_MINUTES=60
```

### 2. Usando PostgreSQL local (con Docker)

```bash
# Iniciar la base de datos local
cd .devcontainer
docker-compose -f docker-compose.dev.yml up -d

# Configurar application-secrets.properties para desarrollo local
DB_PASSWORD=dev_password
DB_USERNAME=dev_user
DB_URL=jdbc:postgresql://localhost:5433/innosistemas_dev
```

### 3. Ejecutar la aplicación

```bash
# Navegar al directorio del proyecto
cd InnoSistemas/InnoSistemas

# Ejecutar con Maven (alias configurado)
mvn-spring

# O comando completo
mvn spring-boot:run -Dmaven.test.skip=true
```

## 🔧 Comandos útiles configurados

- `mvn-spring` - Ejecutar la aplicación
- `mvn-clean` - Limpiar y compilar
- `ll` - Listar archivos detallados

## 📱 Puertos disponibles

- **8080** - Spring Boot Application
- **8081** - Adminer (administrador de BD)
- **5433** - PostgreSQL local

## 🌐 URLs importantes

- **Aplicación**: http://localhost:8080
- **Swagger/OpenAPI**: http://localhost:8080/swagger-ui.html
- **Adminer (BD)**: http://localhost:8081
- **Health Check**: http://localhost:8080/actuator/health

## 📁 Estructura del proyecto

```
InnoSistemas/
├── src/main/java/com/innosistemas/InnoSistemas/
│   ├── controller/          # Controladores REST
│   ├── domain/             # Entidades JPA
│   ├── repository/         # Repositorios Spring Data
│   ├── security/           # Configuración JWT y Security
│   └── config/            # Configuraciones Spring
└── src/main/resources/
    ├── application.properties
    ├── application-secrets.properties
    └── db/migration/      # Scripts Flyway
```

## 🔐 Autenticación JWT

El proyecto incluye autenticación JWT configurada. Endpoints disponibles:
- `POST /api/auth/login` - Iniciar sesión
- `GET /api/auth/me` - Información del usuario actual

## 🗄️ Base de datos

Flyway está configurado para migraciones automáticas. Los scripts están en:
`src/main/resources/db/migration/`