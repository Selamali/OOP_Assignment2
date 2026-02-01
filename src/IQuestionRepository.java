import java.util.List;

public interface IQuestionRepository {
    void addQuestion(String text, int marks);
    List<Question> getAllQuestions();
    Question getQuestionById(int id);
    void updateQuestion(int id, String text, int marks);
    void deleteQuestion(int id);
}