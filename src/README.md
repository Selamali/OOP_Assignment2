# Online Examination System (OOP Assignment 2)

## Description
This project is a console-based Java application developed to demonstrate Object-Oriented Programming (OOP) principles. The system allows managing exam questions and candidates, featuring data sorting, searching, and filtering capabilities.

## Technical Implementation
The project fulfills the following assignment requirements:

### 1. OOP Principles
* **Encapsulation:** All class fields are `private` or `protected` and accessed via methods.
* **Inheritance:** The `Candidate` class inherits from the abstract `Person` class.
* **Abstraction:** `Person` is defined as an abstract class with abstract methods.
* **Polymorphism:**
    * **Dynamic (Overriding):** Implementation of `displayRole()` in the subclass.
    * **Static (Overloading):** Multiple `updateStatus()` methods with different parameters.

### 2. Data Management
* **Data Pool:** Uses `ArrayList<Question>` for dynamic storage.
* **Sorting:** Implements the `Comparable` interface to sort questions by marks.
* **Searching & Filtering:** Includes logic to search by keywords and filter data.
* **Input:** Uses `java.util.Scanner` for console user input.

### 3. Standard Methods
* Overridden `toString()`, `equals()`, and `hashCode()` methods in all entity classes.

## Project Structure
* `Main.java`: Entry point. Handles user input, sorting, and output.
* `Person.java`: Abstract base class.
* `Candidate.java`: Entity class representing a student.
* `Question.java`: Entity class representing an exam question (sortable).
* `Exam.java`: Entity class representing exam details.

## How to Run
1.  Open the project in any Java IDE (e.g., IntelliJ IDEA).
2.  Run the `Main` class.
3.  Follow the instructions in the console to input data.

---
**Student:** Anarbekov Ramazan
**Group:** SE-2504
