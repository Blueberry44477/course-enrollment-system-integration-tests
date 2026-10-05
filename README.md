# Course Enrollment System

This is a sample Spring Boot application explicitly designed and built to demonstrate **Integration Testing** practices.

## Purpose

The primary goal of this project is to serve as a **System Under Test (SUT)**. It showcases how to properly test a layered architecture containing:
- **Database Persistence** (MySQL, Spring Data JPA, Liquibase)
- **Complex Business Logic** (Validation, Constraints, Lifecycle Hooks)
- **External Network Dependencies** (HTTP calls to a simulated external Policy API)

## Tech Stack
- **Java:** 25
- **Framework:** Spring Boot 4.1.1
- **Testing:** 
  - JUnit 5
  - Testcontainers
  - WireMock (for HTTP stubbing)

## Testing Architecture

The integration test suite is deliberately divided into two distinct layers to respect the **Single Responsibility Principle (SRP)** and to optimize execution speed:

### 1. Repository Tests (`StudentRepositoryIT.java`)
- **Focus:** Validates custom SQL queries, JPA entity mapping, and database constraints.
- **Approach:** Uses `@DataJpaTest` and `@AutoConfigureTestDatabase`. It boots up *only* the data layer along with a MySQL Testcontainer. 
- **Benefits:** Extremely fast execution since the full Spring Context (Web, Services, External clients) is ignored.

### 2. Service & Business Flow Tests (`EnrollmentServiceIT.java`)
- **Focus:** Validates complex enrollment flows, including data validation, database state transitions, and HTTP integration with external policy servers.
- **Approach:** Uses `@SpringBootTest` to boot the entire application context. It uses a **MySQL Testcontainer** for the database and a **WireMockContainer** to simulate the external policy HTTP API.
- **Benefits:** Ensures that all system components (Services, Repositories, HTTP Clients) interact correctly in an environment that closely mirrors production.

## How to Run the Tests

Because the tests are Integration Tests (suffixed with `*IT.java`), they are executed during the Maven `verify` phase (via the Failsafe plugin), rather than the standard `test` phase.

To run the complete integration test suite, execute:
```bash
./mvnw clean verify
```

*(Note: Docker must be running on your machine for Testcontainers to spin up the MySQL and WireMock instances).*
