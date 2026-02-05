import java.util.Objects;

public class Question implements Comparable<Question> {
    private final int id; // Первичный ключ
    private final String text;
    private final int marks;

    public Question(int id, String text, int marks) {
        this.id = id;
        this.text = text;
        this.marks = marks;
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public int getMarks() { return marks; }

    @Override
    public int compareTo(Question other) {
        return Integer.compare(this.marks, other.marks);
    }

    @Override
    public String toString() {
        // Вывод с индексом
        return String.format("[%d] | %-30s | %3d pts |", id, text, marks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question q = (Question) o;
        return id == q.id && marks == q.marks && Objects.equals(text, q.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, marks);
    }
}