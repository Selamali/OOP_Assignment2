public class Main {
    public static void main(String[] args) {
        // 1. Создаем подключение (Dependency) - Принцип S и D
        IDBConnection connection = new PostgresDBConnection();

        // 2. Создаем репозиторий (Injection) - Принцип O и L
        IQuestionRepository repository = new QuestionRepository(connection);

        // 3. Создаем меню - Принцип S
        ConsoleMenu menu = new ConsoleMenu(repository);

        // 4. Запускаем
        menu.start();
    }
}