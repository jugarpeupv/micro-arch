# Stage 1: Build the application
FROM gradle:8.11-jdk17 AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and settings files
COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle settings.gradle
COPY build.gradle build.gradle

# Copy the source code
COPY src src

# Ensure the Gradle wrapper script is executable
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build --no-daemon

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jdk AS runtime

# Set the working directory
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/build/libs/*.jar /app/mp3-svc.jar

# Expose the port the application runs on
EXPOSE 8000

# Run the application
ENTRYPOINT ["java", "-jar", "/app/mp3-svc.jar"]
