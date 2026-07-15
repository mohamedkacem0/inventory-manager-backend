# Inventory Manager - Backend

API REST para gestión de inventario (productos y categorías), con autenticación JWT y roles.

## Demo en vivo
- Frontend: https://inventory-manager-frontend-three.vercel.app
- API: https://inventory-manager-backend-42lq.onrender.com

## Tecnologías
- Java 21 + Spring Boot
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- Docker
- Desplegado en Render

## Funcionalidades
- Registro e inicio de sesión con contraseñas encriptadas (BCrypt)
- Autenticación stateless mediante JWT
- CRUD de categorías
- CRUD de productos, con relación a categoría
- Filtrado de productos por categoría y por stock bajo
- Rutas protegidas mediante filtro de seguridad personalizado

## Endpoints principales

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| POST | /api/auth/register | Registrar usuario | No |
| POST | /api/auth/login | Iniciar sesión | No |
| GET | /api/categorias | Listar categorías | Sí |
| POST | /api/categorias | Crear categoría | Sí |
| DELETE | /api/categorias/:id | Borrar categoría | Sí |
| GET | /api/productos | Listar productos (filtros: categoriaId, stockMenorQue) | Sí |
| POST | /api/productos | Crear producto | Sí |
| PUT | /api/productos/:id | Actualizar producto | Sí |
| DELETE | /api/productos/:id | Borrar producto | Sí |

## Instalación local

bash
git clone https://github.com/mohamedkacem0/inventory-manager-backend.git
cd inventory-manager-backend


Crea la base de datos en PostgreSQL:
sql
CREATE DATABASE inventory_manager;


Ejecuta:
bash
mvn spring-boot:run


La app arranca en `http://localhost:8080`, usando por defecto una conexión local a PostgreSQL (usuario `postgres`, ajustable en `application.properties`).

##  Docker

bash
docker build -t inventory-manager-backend .
docker run -p 8080:8080 inventory-manager-backend
