package exam.model;

import java.util.Objects;

public class Candidate extends Person {
    private boolean isRegistered;
    private int score; // НОВОЕ ПОЛЕ

    // Обновленный конструктор
    public Candidate(String name, int age, boolean isRegistered, int score) {
        super(name, age);
        this.isRegistered = isRegistered;
        this.score = score;
    }

    // Конструктор для совместимости (по умолчанию score = 0)
    public Candidate(String name, int age, boolean isRegistered) {
        this(name, age, isRegistered, 0);
    }

    public boolean isRegistered() { return isRegistered; }
    public void setRegistered(boolean registered) { isRegistered = registered; }

    public int getScore() { return score; } // НОВЫЙ ГЕТТЕР
    public void setScore(int score) { this.score = score; } // НОВЫЙ СЕТТЕР

    @Override
    public void displayRole() {
        System.out.println("Role: Candidate (Status: " + (isRegistered ? "Registered" : "Pending") + ", Score: " + score + ")");
    }

    @Override
    public String toString() {
        return super.toString() + ", Registered: " + isRegistered + ", Score: " + score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidate candidate)) return false;
        if (!super.equals(o)) return false;
        return isRegistered == candidate.isRegistered && score == candidate.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isRegistered, score);
    }
}