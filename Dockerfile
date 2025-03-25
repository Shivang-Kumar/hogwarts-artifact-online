# First stage: Build the JAR inside the container
FROM eclipse-temurin:17-jdk as builder
WORKDIR /app

# Copy the project source code
COPY . .

# Grant execution permissions to Maven Wrapper
RUN chmod +x mvnw

# Run Maven build
RUN ./mvnw clean package -DskipTests

# Second stage: Run the application using layers
FROM eclipse-temurin:17-jre
WORKDIR /application

# Copy the JAR from the build stage
COPY --from=builder /app/target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Copy extracted JAR layers
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

EXPOSE 8080
