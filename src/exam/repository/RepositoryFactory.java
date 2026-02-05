package exam.repository;

public class RepositoryFactory {

    // Возвращаем IQuestionRepository, а не класс
    public static IQuestionRepository createQuestionRepository(IDBConnection db) {
        return new QuestionRepository(db);
    }

    // Возвращаем ICandidateRepository, а не класс
    public static ICandidateRepository createCandidateRepository(IDBConnection db) {
        return new CandidateRepository(db);
    }
}