# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
./mvnw clean install              # Build + run all tests
./mvnw test                       # Run all tests
./mvnw test -Dtest=UserServiceTest           # Run a single test class
./mvnw test -Dtest=UserServiceTest#testName  # Run a single test method
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev  # Run locally with dev profile
```

Note: surefire is configured with `-Dnet.bytebuddy.experimental=true` for Mockito compatibility with Java 21.

## Architecture

Spring Boot 3.2 / Java 21 REST API for a personalized yoga practice planner. PostgreSQL database, JWT auth, stateless sessions.

### Domain Model

- **User** → has one **YogaProfile** → has many **YogaRecommendation**s
- **YogaProfile** stores practice preferences (dynamic/static, structured/creative, philosophy openness) via enums, weekly availability, and selected **Goal**s (ManyToMany)
- **YogaRecommendation** is a generated session plan: minute allocations for asana, pranayama, meditation, relaxation, mantra, plus matched **YogaStyle**s
- **Goal**, **Limitation**, **YogaStyle** are reference data seeded by `DataLoader` on startup (only when tables are empty)

### Key Business Logic

`YogaRecommendationService.generateRecommendation()` is the core algorithm — it allocates session minutes based on user goals and matches yoga styles by filtering against the user's structure/dynamic/philosophy preferences.

### Layers

Standard controller → service → repository (Spring Data JPA). DTOs in `dto/` package with Request/Response suffixes. No Lombok — entities use manual getters/setters.

### Auth Flow

JWT via `JwtUtil` (implements `TokenService` interface). `JwtAuthenticationFilter` extracts token from Authorization header, sets userId in `SecurityContext.credentials`. Public endpoints: `/api/auth/register`, `/api/auth/login`, GET `/api/goals`, GET `/api/yoga-styles`, GET `/api/limitations`.

### Configuration

- `application.properties` — production config using env vars (`DATABASE_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`)
- `application-dev.properties` — local dev (copy from `.example`, uses `ddl-auto=update`)
- CORS origins configurable via `allowed.origins` env var (defaults to localhost:3000,5173)

### Tests

Unit tests use Mockito. Controller tests use `@WebMvcTest` with mocked security context. No integration/database tests currently. The `TokenService` interface exists specifically to enable mocking JWT in tests.
