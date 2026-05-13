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
COPY --chown=root:root ./keystore-ignoreme /dev/null || true

# Generate a self-signed PKCS12 keystore for HTTPS (store in /app/keystore.p12)
# Use keytool from the JDK. Passwords are development defaults - replace in production.
RUN keytool -genkeypair \
    -alias springboot \
    -storetype PKCS12 \
    -keystore /app/keystore.p12 \
    -storepass Admin_123 \
    -keypass Admin_123 \
    -dname "CN=localhost, OU=Dev, O=Grupo2, L=City, S=State, C=ES" || true

EXPOSE 443

# No app healthcheck here to avoid restart loops if networking/tools aren't present in image
ENTRYPOINT ["java", "-jar", "app.jar"]
