import java.util.Objects;

public class Question implements Comparable<Question> {
    private final String text;
    private final int marks;

    public Question(String text, int marks) {
        this.text = text;
        this.marks = marks;
    }

    public String getText() { return text; }
    public int getMarks() { return marks; }

    // Сортировка от меньшего к большему
    @Override
    public int compareTo(Question other) {
        return Integer.compare(this.marks, other.marks);
    }

    @Override
    public String toString() {
        // Красивое форматирование таблицы
        return String.format("| %-30s | %3d pts |", text, marks);
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