# Online Examination System v4.0

This project is the second milestone of the course project (Assignment 4), focusing on the development of a **REST API web-service** and the integration of **SOLID principles**. It is based on Subject Area #21: **Online Examination System**.

The system has evolved from a console-based tool into a full-stack solution with a custom Java HTTP server and persistent PostgreSQL database integration.

---

## UPDATES (for Assignment 4 and partially endterm defense)

* **SOLID Principles Implementation:** The codebase has been completely refactored into distinct layers (Repository, Handler, Connection) to ensure maintainability.
* **REST API Integration:** Developed a web-service handling JSON request/response formats using the Java built-in `com.sun.net.httpserver`.
* **JSON Data Exchange:** Integrated Google `Gson` library for serialization and deserialization of objects.
* **New Web Interface:** A responsive frontend built with Vanilla JavaScript and Bootstrap 5, focusing on a "Clean UI" without the need for heavy frameworks.

---

## Architecture & Design (SOLID)

The project strictly adheres to SOLID principles to meet assignment requirements:

* **Single Responsibility Principle (SRP):** * `QuestionRepository`: Exclusively manages SQL queries and data mapping.
    * `QuestionHandler`: Handles HTTP routing and JSON processing.
    * `PostgresDBConnection`: Manages only the database connection logic.
* **Open/Closed Principle (OCP):** Use of interfaces (`IQuestionRepository`, `IDBConnection`) allows for easy extension (e.g., adding new DB types) without modifying existing business logic.
* **Liskov Substitution Principle (LSP):** The `Candidate` class extends the abstract `Person` class, maintaining behavioral consistency.
* **Interface Segregation Principle (ISP):** Abstractions are kept minimal and specific to their required tasks.
* **Dependency Inversion Principle (DIP):** High-level modules depend on interfaces. Dependencies are manually injected in `Main.java` at runtime.

---

## Tech Stack
* **Backend:** Java 17+, `com.sun.net.httpserver`
* **Database:** PostgreSQL + JDBC
* **Libraries:** `gson-2.10.1.jar`
* **Frontend:** HTML5, CSS3, JavaScript (ES6+), Bootstrap 5.3

---

## API Documentation

The backend provides a RESTful API at `http://localhost:8080/questions`.

| Method | Endpoint | Description | Payload Example |
| --- | --- | --- | --- |
| **GET** | `/questions` | Get all questions | - |
| **POST** | `/questions` | Create a new question | `{"text": "What is SOLID?", "marks": 10}` |
| **PUT** | `/questions` | Update a question | `{"id": 1, "text": "Updated text", "marks": 20}` |
| **DELETE** | `/questions?id=1` | Delete by ID | - |

---

## Project Structure
```text
src/
├── Main.java                 # Entry point (DI & Server startup)
├── QuestionHandler.java      # REST API Controller
├── WebHandler.java           # Static File Server
├── PostgresDBConnection.java # Database Connection logic
├── QuestionRepository.java   # Data Access Layer (SQL)
├── Question.java             # Entity model
├── Person.java               # Abstract base class
├── Candidate.java            # Inherited entity class
├── index.html                # Frontend application
└── interfaces/               # IDBConnection, IQuestionRepository
```

## Installation and Setup



### 1. Database Setup

Run the following SQL script in your PostgreSQL terminal:



```sql

CREATE DATABASE exam_db;

CREATE TABLE questions (

id SERIAL PRIMARY KEY,

text TEXT NOT NULL,

marks INT NOT NULL CHECK (marks > 0 AND marks <= 100)

);

```



### 2. Configuration



You must configure your database credentials before running the application.

Open `src/PostgresDBConnection.java` and update the following lines:



```java

public class PostgresDBConnection implements IDBConnection {

@Override

public Connection getConnection() throws SQLException {

// UPDATE THESE VALUES:

String url = "jdbc:postgresql://localhost:5432/exam_db";

String user = "your_postgres_username";

String pass = "your_postgres_password";



return DriverManager.getConnection(url, user, pass);

}

}

```



### 3. Execution



1. Open the project in your IDE (e.g., IntelliJ IDEA).

2. Ensure `postgresql` and `gson` JARs are added to the project libraries.

3. Run the `src/Main.java` file.

4. Select **Option 1** in the console menu to start the Web Server.

5. You will see the startup logs:



```text

>> WEB SERVER STARTED!

>> Open Interface: http://localhost:8080/

>> API Endpoint: http://localhost:8080/questions

```



6. Open your browser and navigate to `http://localhost:8080/`.

---
Student: Anarbekov Ramazan

Group: SE-2504