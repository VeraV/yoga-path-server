# ── Stage 1: Build ───────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml first and download dependencies separately.
# Docker caches this layer — if pom.xml hasn't changed, dependencies
# are not re-downloaded on the next build. Saves a lot of time.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy source code and build the JAR (skip tests — tests run in CI)
COPY src ./src
RUN mvn package -DskipTests -B


# ── Stage 2: Run ─────────────────────────────────────────────────────────────
# Use JRE only (not full JDK) — smaller and more secure
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy only the built JAR from the build stage — no source code, no Maven
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
