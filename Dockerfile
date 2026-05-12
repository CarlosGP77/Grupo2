FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
COPY ssl/keystore.p12 keystore.p12
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

