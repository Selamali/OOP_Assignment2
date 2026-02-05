package exam.exception;

// Это "Checked" исключение для бизнес-логики
public class QuestionNotFoundException extends Exception {
    public QuestionNotFoundException(String message) {
        super(message);
    }
}