# Retro Board Backend
Retro Backend 

## Prerequisites

- Java 21
- Gradle
- IDE (IntelliJ IDEA recommended)

### Install Java 21 on MacOS
```brew install openjdk@21```

## Running the Application

### Build and Run
```
./gradlew clean build
./gradlew bootRun
```
Or run `RetroBackendApplication.java` from your IDE.

The application will start on `http://localhost:8081`

## Database Access (H2 Console)

1. While application is running, visit: `http://localhost:8081/h2-console`
2. Connection Details:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: `password`
   - Driver Class: `org.h2.Driver`

## API Documentation

### Swagger UI
- Interactive API Documentation: `http://localhost:8081/swagger-ui.html`
- OpenAPI Specification: `http://localhost:8081/api-docs`

### Detailed Documentation
For detailed API documentation, see [Board Management API Documentation](docs/api/board-management.md)

## Development

### Running Tests
```
./gradlew test
```

## Misc

### Hexagonal Architecture Reference/Resources
- [Youtube](https://www.youtube.com/watch?v=SO9bHRL3Fic)
- [Article](https://betterprogramming.pub/hexagonal-architecture-with-spring-boot-74e93030eba3)
- [Github Sample](https://github.com/rbailen/Hexagonal-Architecture) 
