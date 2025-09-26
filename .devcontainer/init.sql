-- Script de inicialización para la base de datos de desarrollo
-- Este script se ejecuta automáticamente cuando se crea el contenedor de PostgreSQL

-- Crear esquemas adicionales si son necesarios
CREATE SCHEMA IF NOT EXISTS public;

-- Grant permisos al usuario de desarrollo
GRANT ALL PRIVILEGES ON DATABASE innosistemas_dev TO dev_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO dev_user;

-- Crear algunos datos de prueba básicos (opcional)
-- Se puede personalizar según las necesidades del proyecto