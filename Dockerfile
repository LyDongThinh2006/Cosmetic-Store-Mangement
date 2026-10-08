# =========================
# Build stage
# =========================
FROM eclipse-temurin:25 AS build

WORKDIR /app

# Copy Maven wrapper trước để tận dụng Docker cache
COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build Spring Boot application
RUN ./mvnw clean package -DskipTests


# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:25

WORKDIR /app

# Copy generated JAR
COPY --from=build /app/target/*.jar app.jar

# Railway cung cấp PORT khi chạy container
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]