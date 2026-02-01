import java.util.Objects;

public class Question implements Comparable<Question> {
    // УБРАЛИ final, чтобы Gson мог записывать сюда данные
    private int id;
    private String text;
    private int marks;

    // ОБЯЗАТЕЛЬНО: Пустой конструктор для Gson
    public Question() {
    }

    // Обычный конструктор
    public Question(int id, String text, int marks) {
        this.id = id;
        this.text = text;
        this.marks = marks;
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public int getMarks() { return marks; }

    // Сеттеры (нужны для Gson, если он не использует рефлексию напрямую)
    public void setId(int id) { this.id = id; }
    public void setText(String text) { this.text = text; }
    public void setMarks(int marks) { this.marks = marks; }

    @Override
    public int compareTo(Question other) {
        return Integer.compare(this.marks, other.marks);
    }

    @Override
    public String toString() {
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