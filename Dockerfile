# Multi-stage build for Finance Tracker Backend
FROM maven:3.9.12-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy pom.xml first for dependency caching
COPY backend/pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -DdownloadSources -DdownloadJavadoc

# Copy source code
COPY backend/src src/

# Build application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy JAR from builder
COPY --from=builder /build/target/finance-tracker-api-*.jar app.jar

# Create non-root user for security
RUN addgroup -S financetracker && adduser -S financetracker -G financetracker
USER financetracker

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/api/auth/validate || exit 1

# Run application
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
