-- V2__insert_default_users.sql
-- Inserción de usuarios por defecto con sus roles

-- Insertar usuarios con contraseñas hasheadas usando BCrypt
-- estudiante / 1234 -> hash generado
-- profesor / prof123 -> hash generado  
-- admin / admin123 -> hash generado

INSERT INTO users (username, email, password_hash, created_at) VALUES
('estudiante', 'estudiante@innosistemas.com', '$2b$10$v0j/3hyoW/m.d8mMr7RSHu/7q2ndL/c1iCENhbk.qkzyRVXdynO1e', NOW()),
('profesor', 'profesor@innosistemas.com', '$2b$10$defpjbvR6WqZ/9JX9xoQn.j1qAqUtN1OKiSXfBH14Ct6bnSMT9w1e', NOW()),
('admin', 'admin@innosistemas.com', '$2b$10$Omw1JiZYmewIFeR0u1O8xOTfRA3RUZc2xs1av9FTYStRr7IyTQIrq', NOW());

-- Asignar roles a los usuarios
-- estudiante -> ROLE_STUDENT (id: 1)
-- profesor -> ROLE_TEACHER (id: 2) 
-- admin -> ambos roles para administración completa

INSERT INTO user_roles (user_id, role_id) VALUES
-- estudiante con ROLE_STUDENT
((SELECT id FROM users WHERE username = 'estudiante'), (SELECT id FROM roles WHERE name = 'ROLE_STUDENT')),
-- profesor con ROLE_TEACHER
((SELECT id FROM users WHERE username = 'profesor'), (SELECT id FROM roles WHERE name = 'ROLE_TEACHER')),
-- admin con ROLE_STUDENT (para poder ver perspectiva de estudiante)
((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ROLE_STUDENT')),
-- admin con ROLE_TEACHER (para poder ver perspectiva de profesor)
((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ROLE_TEACHER'));