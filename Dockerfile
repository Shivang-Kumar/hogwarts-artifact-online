# First stage: Build the JAR inside the container
FROM eclipse-temurin:17-jdk as builder
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the project source code
COPY . .

# Build with Maven
RUN mvn clean package -DskipTests

# Ensure target folder and JAR exist
RUN ls -l target/

# Second stage: Run the application using layers
FROM eclipse-temurin:17-jre
WORKDIR /application

# Copy the JAR from the build stage
COPY --from=builder /app/target/*.jar application.jar

# Extract JAR layers
RUN java -Djarmode=layertools -jar application.jar extract

# Debugging: Check extracted files
RUN ls -l

# Copy extracted JAR layers (Make sure the paths match)
COPY --from=builder /application/dependencies/ ./dependencies/
COPY --from=builder /application/spring-boot-loader/ ./spring-boot-loader/
COPY --from=builder /application/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=builder /application/application/ ./application/

# Set entrypoint
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

EXPOSE 8080
