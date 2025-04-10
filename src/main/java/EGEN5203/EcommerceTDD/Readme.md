# EcommerceTDD

This is an E-Commerce backend project developed using Spring Boot as part of the EGEN 5203 Test-Driven Development (TDD) course. The application is structured to demonstrate clean architecture, testing principles, and best practices using modern Java and Spring technologies.

---

Project Overview

Project Name: EcommerceTDD  
Group ID: EGEN5203  
Artifact ID: EcommerceTDD  
Version: 0.0.1-SNAPSHOT  
Java Version: 21  
Spring Boot Version: 3.4.3

---

Technologies Used

Main Frameworks & Tools

Spring Boot - Backend framework
Spring Data JPA - ORM for database operations
Spring Validation - For input validation
Maven - Build and dependency management
Lombok - To reduce boilerplate code

Testing Tools

JUnit 5 - For unit testing
Mockito - Mocking framework for unit tests
Cucumber - For BDD-style integration tests
Spring Boot Test Starter - For Spring testing support

---

Dependencies Breakdown

Spring Boot Starters

- `spring-boot-starter-web`: Enables building RESTful web applications.
- `spring-boot-starter-data-jpa`: Simplifies JPA-based database access.
- `spring-boot-starter-validation`: Adds Java Bean Validation support.

Database

- `mysql-connector-j`: MySQL JDBC driver used to connect to MySQL databases.

Utility

- `lombok` & `lombok-mapstruct-binding`: Reduces boilerplate (getters, setters, constructors, etc.)

Testing

- `spring-boot-starter-test`: Aggregates various testing libraries (JUnit, Mockito, Hamcrest, etc.)
- `mockito-core`, `mockito-inline`, `mockito-junit-jupiter`: Enables mocking in tests.
- `junit-jupiter`, `junit-jupiter-engine`: JUnit 5 testing platform.
- `cucumber-java`, `cucumber-junit`, `cucumber-junit-platform-engine`: Cucumber for BDD testing.

---

Build Configuration

Compiler Plugin

Configured to use Java 21 for both source and target compatibility:

```xml
<source>21</source>
<target>21</target>

Configure Database
Update your application.properties or application.yml to include:

spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

####################FRONTEND############################
1. Go to project File
cd frontend
cd ecommerce

2. Install Depedencies
npm install

3. Build the project 
npm run build

4. Deploy the server
npm run dev

Make sure to check port
If port is busy try changing port in config.json file



