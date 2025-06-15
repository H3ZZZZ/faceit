# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app
COPY . .

RUN ./mvnw package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

