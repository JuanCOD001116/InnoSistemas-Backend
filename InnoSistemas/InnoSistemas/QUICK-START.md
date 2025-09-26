# 🚀 InnoSistemas - Guía de Inicio Rápido

## ⚡ Después de recrear el DevContainer

### 1. Ejecutar configuración automática
```bash
cd InnoSistemas/InnoSistemas
./setup-dev-environment.sh
```

### 2. URLs importantes
- **🌐 API**: http://localhost:8080
- **📚 Swagger UI**: http://localhost:8080/swagger-ui.html  
- **🗄️ Adminer (DB)**: http://localhost:8081
  - Usuario: `dev_user`
  - Contraseña: `dev_password` 
  - Base de datos: `innosistemas_dev`

### 3. Usuarios de prueba
| Usuario | Contraseña | Roles |
|---------|------------|-------|
| `estudiante` | `1234` | ROLE_STUDENT |
| `profesor` | `prof123` | ROLE_TEACHER |
| `admin` | `admin123` | ROLE_STUDENT + ROLE_TEACHER |

### 4. Comandos de prueba rápidos

#### Login
```bash
# Admin (todos los permisos)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'

# Estudiante
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "estudiante", "password": "1234"}'

# Profesor  
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "profesor", "password": "prof123"}'
```

#### Health Check
```bash
curl http://localhost:8080/health
curl http://localhost:8080/api/health/db
```

#### Usuario actual (con token)
```bash
# Usar el token guardado automáticamente
curl -H "Authorization: Bearer $(cat /tmp/admin_token.txt)" \
  http://localhost:8080/auth/me
```

### 5. En caso de problemas

#### Verificar dependencias
```bash
./check-dependencies.sh
```

#### Reiniciar servicios
```bash
cd ../../.devcontainer
docker-compose -f docker-compose.dev.yml restart
```

#### Ver logs de la aplicación
```bash
tail -f app.log
```

#### Reiniciar aplicación
```bash
pkill -f "spring-boot:run"
./mvnw spring-boot:run
```

---

## 🛠️ Desarrollo

### Estructura del proyecto
```
InnoSistemas/InnoSistemas/
├── src/main/java/com/innosistemas/InnoSistemas/
│   ├── controller/     # Controladores REST
│   ├── domain/         # Entidades JPA
│   ├── repository/     # Repositorios
│   ├── security/       # JWT y configuración de seguridad
│   └── config/         # Configuraciones
├── src/main/resources/
│   ├── db/migration/   # Scripts Flyway
│   ├── application.properties
│   └── application-secrets.properties
└── target/classes/     # Clases compiladas
```

### Endpoints principales
- **POST** `/auth/login` - Autenticación
- **GET** `/auth/me` - Usuario actual
- **GET** `/health` - Health check
- **GET** `/api/health/db` - Health check de BD
- **GET** `/swagger-ui.html` - Documentación API

¡Happy coding! 🎉