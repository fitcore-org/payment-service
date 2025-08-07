# Etapa de build
FROM gradle:8.7.0-jdk17 AS builder
WORKDIR /app
COPY . .
WORKDIR /app/app
RUN gradle build -x test

# Etapa final
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
