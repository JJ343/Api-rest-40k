# Primarchs API — Warhammer 40k

API REST en **Java 21 + Spring Boot 4.1 + Maven**, con **PostgreSQL + Flyway**, **JWT (Spring Security)**,
**OpenAPI/Swagger** y **Docker Compose**. Dominio: **Primarcas y sus Capítulos**.

## Estructura (buenas prácticas)

```
src/main/java/com/warhammer/primarchs/
├── PrimarchsApiApplication.java
├── config/        # SecurityConfig, OpenApiConfig
├── common/        # exception: GlobalExceptionHandler (ProblemDetail RFC 7807)
├── user/          # User, Role, UserRepository
├── security/      # JwtService, JwtAuthenticationFilter, CustomUserDetailsService
├── auth/          # AuthController, AuthService, dto/
├── primarch/      # entity, enums, repository, dto/, mapper, service, controller
└── chapter/       # idem (N capítulos → 1 primarca)
src/main/resources/
├── application.yml
└── db/migration/  # V1 esquema, V2 seed (8 primarcas + 8 capítulos)
```

Patrones aplicados: capas Controller → Service → Repository, DTOs (`record`) + validación Bean Validation,
mappers explícitos, excepciones de dominio + `@RestControllerAdvice`, paginación, Flyway como única fuente
del esquema (`ddl-auto: validate`), JWT stateless, OpenAPI documentado.

## Requisitos

- JDK 21+ (el proyecto fija `java.version=21`)
- Docker + Docker Compose (para Postgres), o un Postgres local
- Maven Wrapper incluido (`mvnw.cmd`) — no necesitas instalar Maven

## Arranque rápido

```powershell
# 1. Base de datos
docker compose up -d db

# 2. Compilar y arrancar (usa .env o variables; hay valores por defecto para dev)
.\mvnw.cmd spring-boot:run
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Health: http://localhost:8080/actuator/health

Con Docker todo junto: `docker compose up --build`

## Autenticación

- `POST /api/v1/auth/register` `{username, email, password}` → `{token, username, role}`
- `POST /api/v1/auth/login` `{username, password}` → `{token, ...}`
- Usa el token como `Authorization: Bearer <token>`.
- `GET /api/v1/**` es público. `POST/PUT/DELETE` requieren JWT.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/primarchs?allegiance=LOYALIST&page=0&size=20&sort=legionNumber` | Listar primarcas |
| GET | `/api/v1/primarchs/{id}` | Detalle primarca |
| POST | `/api/v1/primarchs` | Crear (auth) |
| PUT | `/api/v1/primarchs/{id}` | Actualizar parcial (auth) |
| DELETE | `/api/v1/primarchs/{id}` | Eliminar (auth) |
| GET | `/api/v1/chapters?primarchId={uuid}` | Listar capítulos |
| GET | `/api/v1/chapters/{id}` | Detalle capítulo |
| POST/PUT/DELETE | `/api/v1/chapters...` | Escritura (auth) |

Errores en formato `ProblemDetail` (RFC 7807): 400 validación, 401 credenciales, 404 no encontrado,
409 duplicados (nombre / nº legión), 500 genérico.

## Variables de entorno

Ver `.env.example`. Claves: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` (Base64 ≥ 256 bits),
`JWT_EXPIRATION_MILLIS`, `PORT`.

> Genera un secreto propio para producción y no uses el de desarrollo.

## Tests

`.\mvnw.cmd test` — el test de contexto necesita Postgres en marcha (o `docker compose up -d db`).
Para CI sin BD se recomienda añadir Testcontainers (pendiente).
