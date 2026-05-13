# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 443

# The keystore must be mounted at /app/keystore.p12 by the runtime (docker-compose volume or secret)
# Do NOT generate the keystore in the image for production; mount a managed keystore instead.
ENTRYPOINT ["java", "-jar", "app.jar"]
