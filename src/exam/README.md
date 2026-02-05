# Online Examination System vENDTERM 

This project is the final milestone of the course project, developed as a **Full-Stack Web Application** for conducting online examinations. It is based on Subject Area #21: **Online Examination System**.

The system has evolved from a console-based tool into a robust solution with a custom Java HTTP server, persistent PostgreSQL database, and a reactive frontend.

---

## UPDATES (Endterm Features)

* **Student Mode (New):** Added a dedicated "Take Exam" portal where users can take a real test.
* **Randomized Testing:** Implemented an algorithm to generate unique exams by selecting 5 random questions from the database using SQL `ORDER BY RANDOM()` strategy.
* **Auto-Grading System:** The system automatically processes submissions, calculates scores based on answer validation logic, and updates the candidate's record in real-time.
* **Candidate Management:** Full CRUD capabilities for managing student profiles (`/candidates` API).
* **Reactive UI & Animations:** A polished frontend with smooth CSS transitions, staggered list animations, and interactive feedback.
* **SOLID Architecture:** The codebase is strictly refactored into `Controller`, `Service`, `Repository`, and `Model` layers to ensure maintainability.

---

## Architecture & Design (SOLID)

The project strictly adheres to SOLID principles:

* **Single Responsibility Principle (SRP):**
  * `ExamHandler`: Manages only the exam flow (generation and submission).
  * `QuestionHandler`: Handles HTTP routing for questions.
  * `PostgresDBConnection`: Manages strictly the database connection logic.
* **Open/Closed Principle (OCP):** Use of interfaces (`IQuestionRepository`, `ICandidateRepository`) allows extending storage mechanisms without modifying business logic.
* **Liskov Substitution Principle (LSP):** The `Candidate` class extends the abstract `Person` class, maintaining behavioral consistency.
* **Interface Segregation Principle (ISP):** Repositories are split into specific interfaces (`IQuestionRepository`, `ICandidateRepository`) so handlers only access what they need.
* **Dependency Inversion Principle (DIP):** High-level modules depend on abstractions. Dependencies are injected manually in `Main.java`.

---

## Tech Stack
* **Backend:** Java 17+, `com.sun.net.httpserver` (No Spring/Jakarta)
* **Database:** PostgreSQL + JDBC
* **Serialization:** Google `Gson` library
* **Frontend:** HTML5, CSS3 (Custom Animations), JavaScript (Fetch API), Bootstrap 5.3

---

## API Documentation

The backend provides a RESTful API accessible at `http://localhost:####`.

### 1. Questions API
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/questions` | Get all questions |
| **POST** | `/questions` | Create a new question |
| **PUT** | `/questions` | Update a question |
| **DELETE** | `/questions?id=1` | Delete by ID |

### 2. Candidates API
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/candidates` | Get all candidates |
| **POST** | `/candidates` | Register a new candidate |
| **PUT** | `/candidates` | Update profile / score |
| **DELETE** | `/candidates?name=John` | Delete candidate |

### 3. Exam Logic API (Student Mode)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/exam/generate` | Returns 5 random questions for a test session |
| **POST** | `/exam/submit` | Accepts answers, auto-grades, and updates DB |

---

## Project Structure
```text
src/
├── exam/
│   ├── app/
│   │   ├── Main.java                 # Entry point (Server & DI)
│   ├── controller/
│   │   ├── ExamHandler.java          # Logic for taking exams
│   │   ├── QuestionHandler.java      # CRUD for questions
│   │   ├── CandidateHandler.java     # CRUD for candidates
│   │   └── WebHandler.java           # Static file server
│   ├── model/
│   │   ├── Question.java
│   │   ├── Candidate.java
│   │   └── Person.java
│   ├── repository/
│   │   ├── QuestionRepository.java   # SQL logic
│   │   ├── CandidateRepository.java  # SQL logic
│   │   └── PostgresDBConnection.java # DB Connection
│   └── view/
│       └── ConsoleMenu.java          # Legacy Console UI
├── index.html                        # Main Frontend Application
├── script.js                         # Logic (SPA-like behavior)
└── style.css                         # Custom Styles
```

## Installation and Setup



### 1. Database Setup

Run the following SQL script in your PostgreSQL terminal:



```sql

CREATE DATABASE exam_db;CREATE DATABASE exam_db;

-- Questions Table
CREATE TABLE questions (
                         id SERIAL PRIMARY KEY,
                         text TEXT NOT NULL,
                         marks INT NOT NULL CHECK (marks > 0 AND marks <= 100),
                         category VARCHAR(50) DEFAULT 'General'
);

-- Candidates Table
CREATE TABLE candidates (
                          name VARCHAR(100) PRIMARY KEY,
                          age INT,
                          is_registered BOOLEAN DEFAULT FALSE,
                          score INT DEFAULT 0
);

```



### 2. Configuration

Open src/exam/repository/PostgresDBConnection.java and update your credentials:


```java
public class PostgresDBConnection implements IDBConnection {
  @Override
  public Connection getConnection() throws SQLException {
    String url = "jdbc:postgresql://localhost:5432/exam_db";
    String user = "your_postgres_username";
    String pass = "your_postgres_password";
    return DriverManager.getConnection(url, user, pass);
  }
}
```



### 3. Execution
1.Open the project in IntelliJ IDEA.

2.Add postgresql-42.x.jar and gson-2.10.1.jar to Project Libraries.

3.Run src/exam/app/Main.java.

4.Select Option 1 to start the Web Server.

5.Open http://localhost:8080/ in your browser.

6.Use the Take Exam tab to simulate the student experience.


---
Student: Anarbekov Ramazan

Group: SE-2504