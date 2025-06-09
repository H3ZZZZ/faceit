# Brug en JDK base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Kopiér kun backend-koden
COPY backend /app

# Giv eksekveringstilladelse og byg app'en
RUN chmod +x ./mvnw && ./mvnw package -DskipTests

# Eksponér Spring Boot default port
EXPOSE 8080

# Start app'en
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
