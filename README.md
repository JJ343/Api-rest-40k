# Primarchs API — Warhammer 40k

Sample REST API in **Java 21 + Spring Boot 4.1 + Maven**, with **PostgreSQL + Flyway**,
**JWT (Spring Security)**, **OpenAPI/Swagger** and **Docker Compose**.
Domain: **Primarchs and their Chapters**.

This is a demo project showing how I structure a Spring Boot REST API:
layered code, validation, migrations, auth, docs and Docker.

## Stack

- Java 21, Spring Boot 4.1 (WebMVC, Data JPA, Validation, Security, Actuator)
- PostgreSQL 16 + Flyway (`ddl-auto: validate`, migrations are the only schema source)
- JWT (jjwt), springdoc-openapi, Docker Compose

## Layout

```
src/main/java/com/warhammer/primarchs/
├── PrimarchsApiApplication.java
├── config/        # SecurityConfig, OpenApiConfig
├── common/        # exception: GlobalExceptionHandler (ProblemDetail RFC 7807)
├── user/          # User, Role, UserRepository
├── security/      # JwtService, JwtAuthenticationFilter, CustomUserDetailsService
├── auth/          # AuthController, AuthService, dto/
├── primarch/      # entity, enums, repository, dto/, mapper, service, controller
└── chapter/       # same (N chapters -> 1 primarch)
src/main/resources/
├── application.yml
├── db/migration/  # V1 schema, V2 seed (8 primarchs + 8 chapters)
└── db/seed/       # manual seed, NOT run by Flyway (10 remaining primarchs)
tools/seed/        # SeedPrimarchs.java: manual upload via the API
```

Patterns: Controller → Service → Repository layers, `record` DTOs + Bean Validation,
explicit mappers, domain exceptions + `@RestControllerAdvice`, pagination,
stateless JWT, documented OpenAPI.

## Requirements

- JDK 21+ (`java.version=21`)
- Docker + Docker Compose (for Postgres and full-stack run)
- Included Maven Wrapper (`mvnw.cmd`) — no Maven install needed

## Quickstart (recommended)

```powershell
# Full stack, builds the jar inside Docker (no local build needed)
docker compose up --build
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Health: http://localhost:8080/actuator/health

Local run (without Docker image):

```powershell
# 1. Database only
docker compose up -d db

# 2. Build + run (uses .env or defaults for dev)
.\mvnw.cmd spring-boot:run
```

## Auth

- `POST /api/v1/auth/register` `{"username","email","password"}` → `{"token","username","role"}`
- `POST /api/v1/auth/login` `{"username","password"}` → `{"token",...}`
- Send the token as `Authorization: Bearer <token>`.
- `GET /api/v1/**` is public. `POST/PUT/DELETE` require JWT.

```powershell
$reg = Invoke-RestMethod -Uri http://localhost:8080/api/v1/auth/register -Method Post -ContentType "application/json" -Body '{"username":"demo","email":"demo@test.com","password":"demo1234"}'
$env:JWT_TOKEN = $reg.token

Invoke-RestMethod -Uri http://localhost:8080/api/v1/primarchs -Method Post -ContentType "application/json" -Headers @{Authorization="Bearer $env:JWT_TOKEN"} -Body '{"name":"Fulgrim","legionNumber":3,"allegiance":"TRAITOR","status":"DAEMON_PRINCE","homeworld":"Chemos","description":"Demo create"}'
```

## Endpoints

| Method | Route | Description |
|---|---|---|
| GET | `/api/v1/primarchs?allegiance=LOYALIST&page=0&size=20&sort=legionNumber` | List primarchs |
| GET | `/api/v1/primarchs/{id}` | Primarch detail |
| POST | `/api/v1/primarchs` | Create (auth) |
| PUT | `/api/v1/primarchs/{id}` | Partial update (auth) |
| DELETE | `/api/v1/primarchs/{id}` | Delete (auth) |
| GET | `/api/v1/chapters?primarchId={uuid}` | List chapters |
| GET | `/api/v1/chapters/{id}` | Chapter detail |
| POST/PUT/DELETE | `/api/v1/chapters...` | Write (auth) |

Errors use `ProblemDetail` (RFC 7807): 400 validation, 401 bad credentials,
404 not found, 409 duplicates (name / legion number), 500 generic.

## Data / seeds

- `db/migration/V2__seed_primarchs_chapters.sql` runs automatically via Flyway: 8 primarchs + 8 chapters.
- `db/seed/insert_another_primarchs.sql` is **manual** (outside Flyway locations): 10 remaining primarchs + chapters.
- `tools/seed/SeedPrimarchs.java` parses that SQL file and uploads via `POST /api/v1/primarchs` to simulate a client. See `tools/seed/README.md`.

```powershell
$env:API_BASE_URL = "http://localhost:8080"
java tools/seed/SeedPrimarchs.java
```

## Env vars

See `.env.example`. Keys: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`,
`JWT_SECRET` (Base64, >= 256 bits), `JWT_EXPIRATION_MILLIS`, `PORT`.
Copy it to `.env` for local runs — never commit real secrets.

> Generate your own secret for production, do not use the dev one.

## Tests

`.\mvnw.cmd test` — the context test needs Postgres running (`docker compose up -d db`).
No CI/Workflows here on purpose: this repo is a structure demo.

## License

MIT — see `LICENSE`.
