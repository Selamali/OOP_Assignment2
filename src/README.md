# Online Examination System 3.0

## UPDATES (for the 3rd assignment)
* PostgreSQL Integration: Established a connection between the Java application and a PostgreSQL database using the JDBC API.
* Database Handler: Introduced a new DatabaseHandler class to encapsulate all SQL operations, including connection management and CRUD logic.
* Relational Tables: Created two database tables (questions and candidates) to provide persistent data storage.
* Exception Pan (Error Handling): Implemented a robust exception handling system using try-catch-finally and try-with-resources to manage SQLException and NumberFormatException.

---

## Description
This project is a console-based Java application developed to demonstrate Object-Oriented Programming (OOP) principles integrated with relational database management. The system allows managing exam questions and candidates, featuring data persistence, sorting, and filtering capabilities.

---

## Technical Implementation

### 1. Database & Persistence
* JDBC Connectivity: Uses the PostgreSQL JDBC driver to communicate with the database server.
* PreparedStatement: Utilized for secure SQL execution to prevent SQL injection and handle data parameters safely.
* Data Persistence: Replaced volatile memory storage with permanent storage in PostgreSQL tables.



### 2. Exception Handling (Exception Pan)
* Connectivity Errors: Catches SQLException to handle database downtime, incorrect credentials, or syntax errors.
* Input Validation: Catches NumberFormatException and InputMismatchException to prevent application crashes during user data entry.
* Resource Management: Employs try-with-resources to ensure database connections and statements are closed automatically.



### 3. OOP Principles
* Encapsulation: All class fields are private or protected and accessed via methods.
* Inheritance: The Candidate class inherits from the abstract Person class.
* Abstraction: Person is defined as an abstract class with abstract methods.
* Polymorphism:
  * Dynamic (Overriding): Implementation of displayRole() in the subclass and custom toString() methods.
  * Static (Overloading): Multiple updateStatus() methods with different parameters.

### 4. Data Management
* Data Retrieval: Fetches data sets from the database and maps them into a List<Question>.
* Sorting: Implements the Comparable interface to sort questions by marks after they are retrieved from the database.
* Input: Uses java.util.Scanner for console user interaction.

### 5. Standard Methods
* Overridden toString(), equals(), and hashCode() methods in all entity classes to ensure correct object comparison and identification.

---

## Project Structure
* Main.java: Entry point. Handles the UI, user input, sorting logic, and main exception handling blocks.
* DatabaseHandler.java: Manages the JDBC connection string and executes SQL queries.
* Person.java: Abstract base class defining common properties for all users.
* Candidate.java: Entity class representing a student; demonstrates inheritance and overriding.
* Question.java: Entity class representing an exam question (implements Comparable).
* Exam.java: Entity class representing exam metadata.

---

## How to Run
1. Database Setup: Ensure PostgreSQL is running and create the required tables (questions and candidates).
2. Add Library: Include the postgresql-42.x.x.jar driver in the project's library dependencies.
3. Configure Connection: Update the url, user, and password in the DatabaseHandler class.
4. Execute: Run the Main class in any Java IDE (e.g., IntelliJ IDEA) and follow the console prompts.

---

**Student:** Anarbekov Ramazan

**Group:** SE-2504