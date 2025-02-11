    # Use the official Gradle image to build the application
    FROM gradle:8.12.1-jdk21 AS build

    # Set the working directory inside the container
    WORKDIR /app

    # Copy the Gradle wrapper and build files into the container
    COPY gradlew /app/
    COPY gradlew.bat /app/
    COPY gradle /app/gradle
    COPY build.gradle /app/
    COPY settings.gradle /app/

    # Copy the source code into the container
    COPY src /app/src

    # Build the application
    RUN ./gradlew build

    # Use the official OpenJDK image as the base image
    FROM openjdk:21-jdk-slim

    # Set the working directory inside the container
    WORKDIR /app

    # Copy the Spring Boot application JAR file from the build stage
    COPY --from=build /app/build/libs/retro-backend-0.0.1-SNAPSHOT.jar /app/retro-backend.jar

    # Expose port 8081 to the outside world
    EXPOSE 8081

    # Command to run the Spring Boot application
    ENTRYPOINT ["java", "-jar", "/app/retro-backend.jar"]