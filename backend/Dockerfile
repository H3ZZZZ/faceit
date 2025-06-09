# Use an official JDK runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the Maven project files into the container
COPY . .

# Build the project and skip tests to speed things up
RUN ./mvnw package -DskipTests

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
