# InnoSistemas-Backend

## Configuración de variables de entorno y manejo de secretos

Para evitar commitear credenciales en texto plano, la aplicación lee la configuración de la base de datos desde variables de entorno.

### Variables requeridas
- `DB_URL`: URL JDBC de PostgreSQL. Ejemplo Neon (AWS): `jdbc:postgresql://ep-muddy-mouse-af2imat3-pooler.c-2.us-west-2.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require`
- `DB_USERNAME`: usuario de la base de datos. Ej: `neondb_owner`
- `DB_PASSWORD`: contraseña del usuario. Debe establecerse siempre por variable de entorno.

Si no defines estas variables, por defecto se usará la URL y el usuario de Neon anteriores, pero la app no arrancará sin `DB_PASSWORD`.

### Cómo establecer variables de entorno (Neon)

#### Windows PowerShell
```powershell
$env:DB_URL = "jdbc:postgresql://ep-muddy-mouse-af2imat3-pooler.c-2.us-west-2.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require"
$env:DB_USERNAME = "neondb_owner"
$env:DB_PASSWORD = "npg_GA2k9waJYIBn"

./mvnw spring-boot:run
```

Para que persistan entre sesiones, configúralas en el perfil de Windows o usa un gestor de secretos.

#### Linux / macOS (bash/zsh)
```bash
export DB_URL="jdbc:postgresql://ep-muddy-mouse-af2imat3-pooler.c-2.us-west-2.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require"
export DB_USERNAME="neondb_owner"
export DB_PASSWORD="npg_GA2k9waJYIBn"

./mvnw spring-boot:run
```

#### Maven (variables inline)
```bash
DB_URL="jdbc:postgresql://ep-muddy-mouse-af2imat3-pooler.c-2.us-west-2.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require" \
DB_USERNAME=neondb_owner \
DB_PASSWORD="npg_GA2k9waJYIBn" \
./mvnw spring-boot:run
```

### Dónde se usan en la app
En `src/main/resources/application.properties`:
```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://ep-muddy-mouse-af2imat3-pooler.c-2.us-west-2.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require}
spring.datasource.username=${DB_USERNAME:neondb_owner}
spring.datasource.password=${DB_PASSWORD}
```

### Manejo de secretos (recomendaciones)
- No commitees contraseñas en repositorios. Usa variables de entorno o un Secret Manager.
- Para despliegues (Docker/Kubernetes/CI):
  - Docker: usa `--env-file` o secretos de Docker.
  - Kubernetes: usa `Secret` y móntalo como variables o volúmenes.
  - CI (GitHub Actions, GitLab CI): almacena en Secrets del proyecto y expórtalos en el job.
- Rota contraseñas periódicamente y limita permisos del usuario de DB.
- Habilita `sslmode=require` (ya configurado) para cifrar la conexión.