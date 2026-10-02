# Health API

A REST API built with Java and Spring Boot for managing patients and appointments.

## Tech
Java 25
Spring Boot
Spring Security + JWT
Spring Data JPA / Hibernate
PostgreSQL
Docker
JUnit 5, Mockito & MockMvc
Maven
## Features
Patient management
Appointment management
User registration and login
JWT authentication
Role-based authorization (USER / ADMIN)
BCrypt password hashing
Request validation
Exception handling
Automated tests
## Getting Started
1. Start PostgreSQL
docker compose up -d
2. Configure the application

Copy:

src/main/resources/application-example.properties

to:

src/main/resources/application.properties

Then add your local database password and JWT secret.

application.properties is ignored by Git and should not be committed.

3. Run the application

Windows:

.\mvnw.cmd spring-boot:run

macOS/Linux:

./mvnw spring-boot:run

The API runs on:

http://localhost:8080
## Authentication

Register:

POST /auth/register

Login:

POST /auth/login

Login returns a JWT token. Use it for protected endpoints:

Authorization: Bearer <token>

USER accounts can access authenticated endpoints, while ADMIN accounts can also delete patients and appointments.

## Testing

Run all tests with:

./mvnw clean test

On Windows:

.\mvnw.cmd clean test

The project includes unit, controller, integration and security tests.

## Project Structure
```text
src/main/java/health_api/
├── config/
├── controller/
├── dto/
├── exception/
├── model/
├── repository/
└── service/
```
## Future Improvements
Swagger / OpenAPI
Flyway migrations
Pagination
Refresh tokens
GitHub Actions
