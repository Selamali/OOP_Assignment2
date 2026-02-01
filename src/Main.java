import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // 1. Настройка подключения к базе (SOLID)
        IDBConnection connection = new PostgresDBConnection();
        IQuestionRepository repository = new QuestionRepository(connection);

        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("      Online Examination System 4.0      ");
        System.out.println("=========================================");
        System.out.println("1. Web Server (Interface + REST API)");
        System.out.println("2. Console Menu (Classic)");
        System.out.print(">> Select mode: ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            // --- РЕЖИМ ВЕБ-СЕРВЕРА ---
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // А. Подключаем API (для данных JSON)
            server.createContext("/questions", new QuestionHandler(repository));

            // Б. Подключаем Интерфейс (для файла index.html)
            server.createContext("/", new WebHandler());

            server.setExecutor(null);
            System.out.println("\n>> WEB SERVER STARTED!");
            System.out.println(">> Open Interface: http://localhost:8080/");
            System.out.println(">> API Endpoint:   http://localhost:8080/questions");
            System.out.println(">> (Press Ctrl+C to stop)");

            server.start();

        } else if (choice.equals("2")) {
            // --- РЕЖИМ КОНСОЛИ ---
            ConsoleMenu menu = new ConsoleMenu(repository);
            menu.start();

        } else {
            System.out.println("Invalid choice. Exiting.");
        }
    }
}