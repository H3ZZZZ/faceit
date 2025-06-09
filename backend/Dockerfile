# Use slimmer JDK base
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Du ER allerede i ./backend som build context, så bare kopiér alt herfra
COPY . .

# Build uden tests
RUN ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
