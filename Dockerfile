# ======== Stage 1: Build Application ========
FROM maven:3.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# ======== Stage 2: Run Application ========
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
