package exam.model;

import java.util.Objects;

public class Question {
    private int id;
    private String text;
    private int marks;
    private String category; // НОВОЕ ПОЛЕ

    // Приватный конструктор для работы через Builder
    private Question(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.marks = builder.marks;
        this.category = builder.category;
    }

    // Геттеры
    public int getId() { return id; }
    public String getText() { return text; }
    public int getMarks() { return marks; }
    public String getCategory() { return category; } // НОВЫЙ ГЕТТЕР

    @Override
    public String toString() {
        return "Question{id=" + id + ", text='" + text + "', marks=" + marks + ", category='" + category + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id &&
                marks == question.marks &&
                Objects.equals(text, question.text) &&
                Objects.equals(category, question.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, marks, category);
    }

    // ПАТТЕРН: BUILDER
    public static class Builder {
        private int id;
        private String text;
        private int marks;
        private String category = "General"; // Значение по умолчанию

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setMarks(int marks) {
            this.marks = marks;
            return this;
        }

        public Builder setCategory(String category) { // НОВЫЙ СЕТТЕР
            this.category = category;
            return this;
        }

        public Question build() {
            return new Question(this);
        }
    }
}