package exam.repository;

import exam.model.Question;
import java.util.List;

public interface IQuestionRepository {
    void addQuestion(String text, int marks, String category);
    List<Question> getAllQuestions();
    Question getQuestionById(int id);
    void updateQuestion(int id, String text, int marks, String category);
    void deleteQuestion(int id);

    // НОВЫЙ МЕТОД: Получить N случайных вопросов
    List<Question> getRandomQuestions(int limit);

    // ТРЕБОВАНИЕ: Clean Java Feature (Default method)
    default void printStats(List<Question> questions) {
        System.out.println("-----------------------------------------");
        System.out.println(">> GENERAL STATISTICS: Questions in the database: " + questions.size());
        System.out.println("-----------------------------------------");
    }
}