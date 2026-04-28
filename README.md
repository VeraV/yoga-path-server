# Yoga Path — Server

REST API for the Yoga Path practice planner. Authenticates users (JWT), stores profiles and practice logs, generates personalized session recommendations, and serves reference data (yoga styles, limitations).

## Tech stack

- **Java 21** + **Spring Boot 3.2.2**
- **Spring Web** — REST controllers
- **Spring Data JPA** + **Hibernate** — ORM
- **PostgreSQL 18** — primary datastore
- **Spring Security** — stateless filter chain (CSRF off, sessionless)
- **JJWT 0.12.3** — JWT signing and verification (HS256)
- **Spring Validation** — Jakarta Bean Validation on DTOs
- **Spring Actuator** — `/actuator/health` and `/actuator/info`
- **HikariCP** — connection pool
- **JUnit 5** + **Mockito** (via `spring-boot-starter-test`) — unit tests
- **Maven Wrapper** (`mvnw`) — no need for a global Maven install
- **Docker** + **Docker Compose** — local DB and app container

## Project layout

```
src/main/java/com/yogapath/
├── controller/    REST endpoints (Auth, Goal, Limitation, PracticeLog, YogaProfile, YogaRecommendation, YogaStyle)
├── service/       Business logic
├── repository/    Spring Data JPA repositories
├── model/         JPA entities (+ enums/)
├── dto/           Request/response objects with validation
├── security/      JwtUtil, JwtAuthenticationFilter, TokenService
├── config/        SecurityConfig, DataLoader (seeds yoga styles, limitations, goals)
└── exception/     GlobalExceptionHandler

src/main/resources/
├── application.properties              Production (env-var driven)
├── application-dev.properties          Local dev (gitignored)
└── application-dev.properties.example  Template for the above
```

## Getting started

### Prerequisites

- **Java 21** (JDK)
- One of:
  - **PostgreSQL 18** running locally (for the dev profile), or
  - **Docker** + **Docker Compose** (the easiest path — boots Postgres and the app together)

### Option A — run with Docker Compose (recommended)

Create a `.env` file at the repo root:

```sh
DB_NAME=yoga_path
DB_USER=yoga
DB_PASSWORD=change_me
JWT_SECRET=replace_with_a_random_string_at_least_32_chars_long
JWT_EXPIRATION=86400000
```

Then:

```sh
docker compose up --build
```

API at <http://localhost:8080>; Postgres at `localhost:5432`. Hibernate's `ddl-auto` is set to `update` in this mode, so the schema is created on first run.

### Option B — run locally with the dev profile

1. Start a local PostgreSQL and create a database (e.g., `yoga_path`).
2. Copy the dev properties template:

   ```sh
   cp src/main/resources/application-dev.properties.example src/main/resources/application-dev.properties
   ```

3. Fill in your DB credentials and a JWT secret (≥32 chars).
4. Run:

   ```sh
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

API at <http://localhost:8080>.

## Useful commands

| Command | What it does |
|---|---|
| `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` | Start the app with the dev profile |
| `./mvnw test` | Run all JUnit tests |
| `./mvnw test -Dtest=AuthServiceTest` | Run a single test class |
| `./mvnw test -Dtest=AuthServiceTest,JwtUtilTest` | Run multiple test classes |
| `./mvnw package` | Build a JAR into `target/` (runs tests; add `-DskipTests` to skip) |
| `docker compose up --build` | Build the app image and run it alongside Postgres |
| `docker compose down -v` | Stop containers and wipe the Postgres volume |

## API at a glance

All endpoints are under `/api`. Authenticated routes require an `Authorization: Bearer <token>` header.

| Area | Endpoints |
|---|---|
| Auth | `POST /auth/register`, `POST /auth/login`, `GET /auth/verify` |
| Profile | `POST /profiles`, `GET /profiles/{id}`, `GET /profiles/user/{userId}`, `PUT /profiles/{id}` |
| Practice Log | `POST /practice-logs`, `GET /practice-logs/{id}`, `GET /practice-logs/user/{userId}`, `GET /practice-logs/user/{userId}/range`, `PUT /practice-logs/{id}`, `DELETE /practice-logs/{id}` |
| Recommendations | `POST /recommendations/generate/{profileId}`, `GET /recommendations/profile/{profileId}/latest`, `GET /recommendations/profile/{profileId}` |
| Reference data (public) | `GET /goals`, `GET /yoga-styles`, `GET /limitations` |
| Health | `GET /actuator/health` |

Detailed contracts (request/response shapes, validation rules, status codes) live in [yoga-path-docs](https://github.com/VeraV/yoga-path-docs).

## Authentication

Stateless JWT flow, signed with HS256:

1. `POST /auth/register` or `POST /auth/login` — server returns `{ token, userId, email, name }`.
2. The client sends the token on every subsequent request.
3. `JwtAuthenticationFilter` validates the token on each request and populates the `SecurityContext`.
4. Token lifetime is configurable via `jwt.expiration` (default 24h). After expiration, the user must log in again — there's no refresh token.

> Email is used only as the unique identifier for accounts. No email is sent (no verification, no password reset, no notifications).

## Seeded reference data

`DataLoader` runs on startup and inserts:

- **Goals (6):** Physical Fitness, Stress Relief, Better Sleep, Mental Focus, Flexibility, Interested in Philosophy
- **Yoga Styles (7):** Ashtanga, Sivananda, Kundalini, Iyengar, Vinyasa, Yin, Any Hatha — each tagged with boolean flags used by the recommendation matcher
- **Limitations (16):** common health conditions with practice notes

Seeding only runs when the corresponding tables are empty.

## Configuration reference

| Property / env var | Default | Used in |
|---|---|---|
| `DATABASE_URL` | — (required) | prod profile (e.g., `jdbc:postgresql://db:5432/yoga_path`) |
| `DB_USER` / `DB_PASSWORD` | — (required) | prod profile |
| `JWT_SECRET` | — (required, ≥32 chars) | both profiles |
| `JWT_EXPIRATION` | `86400000` (24h) | both profiles |
| `PORT` | `8080` | server port |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `validate` (prod), `update` (dev/docker) | schema management |

## Related repos

- **Client:** [yoga-path-client](https://github.com/VeraV/yoga-path-client) — React + TypeScript SPA
- **Specs:** [yoga-path-docs](https://github.com/VeraV/yoga-path-docs) — feature specifications

## Deployment

The provided `Dockerfile` produces a slim JRE-only image. Any container platform (Render, Fly, Railway, ECS, GKE, etc.) that accepts a Docker image and provides a PostgreSQL connection string + the JWT env vars can host it.
