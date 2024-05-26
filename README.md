# Table Tennis Tournament Microservice

This is a Java Spring Boot application that manages bank users operations. It allows to register users; add, update, and delete user's contact info; transfer money.

## Features

- User Registration: Allows new users to register with their personal details including full name, birthdate, initial balance, phone number, and email.
- Authentication: Provides secure login functionality with JWT token-based authentication.
- Contact Information Management: Users can add, update, and delete their contact information (phone numbers and emails).
- Money Transfer: Authenticated users can transfer money to other users.
- API Documentation: Detailed API documentation is available via Swagger UI.

## Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL
- Spring Boot 3.0.0 or higher
- Maven dependencies for pom.xml:
    - Spring Boot Starter Data JPA
    - Spring Boot Starter Web
    - PostgreSQL JDBC Driver
    - Lombok
    - Spring Boot Starter Test
    - Jakarta Validation API
    - Springdoc OpenAPI UI
    - Spring Boot Starter Security
    - JJWT API
    - JJWT Implementation
    - JJWT Jackson
    - Mockito Core
    - Mockito JUnit Jupiter

## How to Build

1. Clone the repository:

```bash
git clone https://github.com/LuminiteTime/Bank-Test-Task.git
```

2. Navigate to the project directory:

```bash
cd Bank-Test-Task
```

3. Build the project using Maven:

```bash
mvn clean install
```

## How to Run

1. Configure your PostgreSQL database detailes according to the `src/main/resources/application.properties` file.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bank
spring.datasource.username=postgres
spring.datasource.password=postgres
```

2. Run the application:

```bash
mvn spring-boot:run
```

## API Documentation

The API documentation is available at `/swagger` endpoint when the application is running.

## License

This project is licensed under the MIT License.