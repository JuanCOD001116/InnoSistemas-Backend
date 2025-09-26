# 🚀 InnoSistemas - Entorno de Desarrollo

## ⚡ Configuración Rápida (Después de recrear DevContainer)

### Opción 1: Script Automático (Recomendado)
```bash
cd /workspaces/InnoSistemas-Backend/InnoSistemas/InnoSistemas
./setup-dev-environment.sh
```

### Opción 2: Configuración Manual

#### 1. Verificar servicios Docker
```bash
cd .devcontainer
docker-compose -f docker-compose.dev.yml up -d
```

#### 2. Esperar PostgreSQL
```bash
# Esperar hasta que esté listo
until pg_isready -h localhost -p 5433 -U dev_user; do sleep 2; done
```

#### 3. ~~Crear usuarios de prueba~~ (Ya no necesario)
```bash
# Los usuarios se crean automáticamente con Flyway V2 migration
# estudiante / 1234, profesor / prof123, admin / admin123
# Este paso ya no es necesario - se ejecuta automáticamente
```

#### 4. Iniciar aplicación
```bash
nohup mvn spring-boot:run > app.log 2>&1 &
```

#### 5. Verificar estado
```bash
# Esperar unos segundos y probar
sleep 10
curl http://localhost:8080/api/health/db | jq .
```

## 🧪 Testing de la API

### Obtener Token JWT
```bash
# Login admin (con todos los roles)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}' | jq .

# Login estudiante
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "estudiante", "password": "1234"}' | jq .

# Login profesor  
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "profesor", "password": "prof123"}' | jq .

# Guardar token de admin
curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}' | jq -r '.token' > /tmp/token.txt
```

### Endpoints Principales
```bash
# Usuario actual
curl -H "Authorization: Bearer $(cat /tmp/token.txt)" \
  http://localhost:8080/auth/me | jq .

# Lista usuarios
curl -H "Authorization: Bearer $(cat /tmp/token.txt)" \
  http://localhost:8080/api/users | jq .

# Usuarios con roles
curl -H "Authorization: Bearer $(cat /tmp/token.txt)" \
  http://localhost:8080/api/users/with-roles | jq .

# Lista roles
curl -H "Authorization: Bearer $(cat /tmp/token.txt)" \
  http://localhost:8080/api/roles | jq .
```

## 📋 URLs Importantes

- **API Base:** http://localhost:8080
- **Health Check:** http://localhost:8080/api/health/db
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI Docs:** http://localhost:8080/v3/api-docs
- **Adminer (DB GUI):** http://localhost:8081

## 👤 Usuarios por Defecto

Los usuarios se crean automáticamente con las migraciones de Flyway:

| Username   | Password | Email                      | Roles                           |
|------------|----------|----------------------------|---------------------------------|
| estudiante | 1234     | estudiante@innosistemas.com| ROLE_STUDENT                    |
| profesor   | prof123  | profesor@innosistemas.com  | ROLE_TEACHER                    |
| admin      | admin123 | admin@innosistemas.com     | ROLE_STUDENT + ROLE_TEACHER     |

## 🗄️ Base de Datos

- **Host:** localhost:5433
- **Database:** innosistemas_dev  
- **Username:** dev_user
- **Password:** dev_password

### Acceso directo a PostgreSQL
```bash
psql postgresql://dev_user:dev_password@localhost:5433/innosistemas_dev
```

## 🔧 Comandos Útiles

### Verificar aplicación corriendo
```bash
pgrep -f "spring-boot:run"  # Ver PID
tail -f app.log             # Ver logs
```

### Reiniciar aplicación
```bash
pkill -f "spring-boot:run"                    # Detener
nohup mvn spring-boot:run > app.log 2>&1 &   # Iniciar
```

### Verificar servicios Docker
```bash
cd .devcontainer
docker-compose -f docker-compose.dev.yml ps
```

## 🚨 Troubleshooting

### Si PostgreSQL no responde:
```bash
cd .devcontainer
docker-compose -f docker-compose.dev.yml restart postgres
```

### Si la aplicación no inicia:
```bash
# Ver logs
cat app.log

# Verificar Java
java -version

# Limpiar y compilar
mvn clean compile
```

### Si faltan usuarios de prueba:
```bash
# Verificar
psql postgresql://dev_user:dev_password@localhost:5433/innosistemas_dev -c "SELECT * FROM users;"

# Recrear con el script
./setup-dev-environment.sh
```

## 📝 Notas

- El DevContainer preserva todo el código y configuración
- Los datos de la BD se pierden al recrear el container
- El script `setup-dev-environment.sh` automatiza toda la configuración
- La aplicación se ejecuta en background para persistir entre sesiones del terminal