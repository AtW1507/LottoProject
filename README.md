# 🎱 Lotto - Lottery Web Application

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL-47A248.svg)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

## 📖 About the Project
**Lotto** is a RESTful web application built with Spring Boot that simulates a lottery system. The application allows users to submit their numbers, generates winning numbers, and checks the results to see if the submitted tickets have won.

This project was developed with a strong emphasis on clean code principles, software design patterns, and high test coverage. It serves as a showcase of advanced backend architectural concepts, specifically focusing on domain-driven design principles.

## 🏗️ Architecture
This project stands out by implementing advanced architectural patterns:

*   **Modular Monolith:** The application is built as a single deployable unit but is logically divided into strictly separated, independent modules (e.g., number generator, ticket receiver, result checker). Each module encapsulates its own logic and communicates with others only through well-defined public interfaces (Facades).
*   **Hexagonal Architecture (Ports and Adapters):** The core business logic (Domain) is completely isolated from external frameworks and technologies.
    *   **Inside the Hexagon:** Pure Java code representing business rules, completely unaware of databases or web frameworks.
    *   **Outside the Hexagon:** Infrastructure (MongoDB, REST Controllers, external API clients) communicates with the domain through input/output Ports (interfaces) and Adapters.

## 🚀 Core Features
*   **Ticket Submission:** REST API endpoints allowing users to submit their chosen lottery numbers.
*   **Winning Numbers Generation:** Automated mechanism to draw the winning numbers for a specific date/draw.
*   **Result Verification:** System that cross-references user tickets with drawn numbers to calculate hits and determine winners.
*   **Data Persistence:** Storing user tickets and draw results securely in a NoSQL database (MongoDB).

## 🛠️ Technology Stack

**Backend & Frameworks:**
*   Java 17
*   Spring Boot (Web, Data MongoDB)
*   REST API

**Database & DevOps:**
*   MongoDB
*   Docker & Docker Compose
*   Maven

**Testing:**
*   JUnit 5 & AssertJ
*   Mockito
*   MockMvc & SpringBootTest (Integration Testing)
*   **Testcontainers:** Providing throwaway, lightweight instances of MongoDB inside Docker for reliable integration tests.
*   **WireMock:** Mocking external HTTP services to ensure tests run fast and deterministically.

**Version Control & Tools:**
*   Git & GitHub
*   IntelliJ IDEA

## ⚙️ Running the Project Locally

### Prerequisites
Make sure you have the following installed on your machine:
*   [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
*   [Maven](https://maven.apache.org/download.cgi)
*   [Docker & Docker Desktop](https://www.docker.com/) (Required for MongoDB and Testcontainers)

### Step-by-Step Guide

1. **Clone the repository:**
   ```bash
   git clone https://github.com/AtW1507/LottoProject.git
   cd LottoProject
   ```

2. **Start the Database:**
   Use Docker Compose to spin up the MongoDB instance in the background:
   ```bash
   docker-compose up -d
   ```

3. **Build the application and run tests:**
   This will execute all unit and integration tests (spinning up Testcontainers as needed).
   ```bash
   mvn clean install
   ```

4. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```
   The application will start on the default port `8080`.

## 🧪 Testing Strategy
The project is thoroughly tested to ensure high reliability and to validate the architectural boundaries:
*   **Domain Tests (Unit):** The core logic inside the hexagon is tested using standard JUnit 5 and pure Java memory repositories, completely independent of Spring Context, making them extremely fast.
*   **Integration Tests:** Spring Boot integration tests utilize **MockMvc** to simulate HTTP requests to the REST Controllers.
*   **Infrastructure Tests:** **Testcontainers** are used to spin up real MongoDB instances to test database repositories, while **WireMock** handles any external API stubs, ensuring the application behaves correctly in a production-like environment.

Run the test suite using:
```bash
mvn test
```