# Manual seed — upload via the API

Demo tool that simulates a client uploading data through the REST API
(no direct DB inserts).

Source of truth: `src/main/resources/db/seed/insert_another_primarchs.sql`
(10 remaining primarchs). That folder is outside Flyway
(`classpath:db/migration`), so Spring Boot never applies it automatically.

## What `SeedPrimarchs.java` does

1. Parses only the `INSERT INTO primarchs ... VALUES (...)` block from the SQL file.
2. `GET /api/v1/primarchs?size=100` to skip existing names (idempotent).
3. `POST /api/v1/primarchs` for each missing one with `Authorization: Bearer <JWT>`.

Single-file, no dependencies (only `java.net.http`). Requires Java 21+.

## Run (API must be up)

```powershell
docker compose up --build -d

# 1) Get a token (POST endpoints require JWT)
$reg = Invoke-RestMethod -Uri http://localhost:8080/api/v1/auth/register -Method Post -ContentType "application/json" -Body '{"username":"seeder","email":"seeder@test.com","password":"seeder1234"}'
$env:JWT_TOKEN = $reg.token
# If the user already exists:
# $login = Invoke-RestMethod -Uri http://localhost:8080/api/v1/auth/login -Method Post -ContentType "application/json" -Body '{"username":"seeder","password":"seeder1234"}'
# $env:JWT_TOKEN = $login.token

# 2) Run the seed (from the repo root)
$env:API_BASE_URL = "http://localhost:8080"
java tools/seed/SeedPrimarchs.java
```

Optional env vars: `API_BASE_URL` (default `http://localhost:8080`),
`JWT_TOKEN`, `SEED_FILE` (default `src/main/resources/db/seed/insert_another_primarchs.sql`),
`SEED_USERNAME` / `SEED_EMAIL` / `SEED_PASSWORD` for auto login/register.

Verify: `GET http://localhost:8080/api/v1/primarchs?size=100` should return 18 items
(8 from Flyway `V2` + 10 from this seed).
