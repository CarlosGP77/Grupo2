FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y mariadb-client && rm -rf /var/lib/apt/lists/*
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "until mariadb-admin ping -h mariadb --silent; do echo 'Waiting for MariaDB...'; sleep 2; done; exec java -jar app.jar"]
