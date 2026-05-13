# Multi-stage Dockerfile para Grupo2
# Etapa 1: Maven Build
# Etapa 2: JDK Runtime (optimizado)

# ============================================
# ETAPA 1: COMPILACIÓN CON MAVEN
# ============================================
FROM maven:3.9.6-eclipse-temurin-17-jammy AS builder

LABEL stage=maven-builder

WORKDIR /build

# Copiar archivos de configuración
COPY pom.xml .
COPY .mvn ./.mvn

# Descargar dependencias (caché layer)
RUN mvn dependency:resolve -B 2>/dev/null

# Copiar código fuente
COPY src ./src

# Compilar sin tests
RUN mvn clean package -DskipTests

# ============================================
# ETAPA 2: RUNTIME CON JDK MÍNIMO
# ============================================
FROM eclipse-temurin:17-jdk-jammy

LABEL maintainer="Grupo2"
LABEL description="Grupo2 - Sistema de Gestión de Cursos con Spring Boot"

# Usuario no-root para seguridad
RUN useradd -m -u 1000 appuser

WORKDIR /app

# Copiar JAR compilado
COPY --from=builder --chown=appuser:appuser /build/target/*.jar app.jar

# Cambiar usuario
USER appuser

# Puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget -q -O- http://localhost:8080/ || exit 1

# Variables de entorno
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0"

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]

