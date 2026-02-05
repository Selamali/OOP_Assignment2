package exam.model;

import java.util.Objects;

public class Exam {
    private final String subject;
    private final double duration;

    public Exam(String subject, double duration) {
        this.subject = subject;
        this.duration = duration;
    }

    // Добавляем геттеры (Encapsulation)
    public String getSubject() {
        return subject;
    }

    public double getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Exam: " + subject + " [" + duration + " min]";
    }

    // ОБЯЗАТЕЛЬНО для Endterm: корректное сравнение объектов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return Double.compare(exam.duration, duration) == 0 &&
                Objects.equals(subject, exam.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, duration);
    }
}