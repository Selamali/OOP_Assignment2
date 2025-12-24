import java.util.Objects;

// Реализует Comparable для сортировки
public class Question implements Comparable<Question> {
    private String text;
    private int marks;

    public Question(String text, int marks) {
        this.text = text;
        this.marks = marks;
    }

    public String getText() { return text; }
    public int getMarks() { return marks; }

    // Логика сортировки (по баллам)
    @Override
    public int compareTo(Question other) {
        return Integer.compare(this.marks, other.marks);
    }

    @Override
    public String toString() {
        return String.format("Question: [%-20s] | Marks: %d", text, marks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question q = (Question) o;
        return marks == q.marks && Objects.equals(text, q.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, marks);
    }
}