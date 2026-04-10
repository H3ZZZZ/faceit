# Stage 1: Resolve dependencies in a cache-friendly layer
FROM eclipse-temurin:17-jdk-jammy AS deps

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw && ./mvnw dependency:go-offline -DskipTests

# Stage 2: Build
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /app /app
COPY src src

RUN ./mvnw package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
