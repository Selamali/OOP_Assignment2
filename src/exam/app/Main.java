package exam.app;

import exam.repository.*;
import exam.controller.*;
import exam.view.ConsoleMenu;
import exam.model.Question;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // 1. Инициализация базы и репозиториев через Фабрику
        IDBConnection connection = new PostgresDBConnection();

        IQuestionRepository qRepo = RepositoryFactory.createQuestionRepository(connection);
        ICandidateRepository cRepo = RepositoryFactory.createCandidateRepository(connection);

        // Проверка системы (опционально)
        try {
            // Быстрый тест соединения
            System.out.println("Checking DB connection...");
            connection.getConnection().close();
            System.out.println("DB Connected successfully.");
        } catch (Exception e) {
            System.err.println("Error connecting to DB: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("=================================");
        System.out.println("   ONLINE EXAM SYSTEM vENDTERM       ");
        System.out.println("=================================");
        System.out.println("Select mode:");
        System.out.println("1. Web Server (Port 8080)");
        System.out.println("2. Console Menu");
        System.out.print(">> Select: ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            // --- ЗАПУСК ВЕБ-СЕРВЕРА ---
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // 1. API для вопросов
            server.createContext("/questions", new QuestionHandler(qRepo));

            // 2. API для кандидатов
            server.createContext("/candidates", new CandidateHandler(cRepo));

            // 3. НОВОЕ: API для экзамена (Student Mode)
            // Убедитесь, что вы создали файл exam/controller/ExamHandler.java
            server.createContext("/exam", new ExamHandler(qRepo, cRepo));

            // 4. Фронтенд (Static Files)
            // Важно: "/" должен быть последним или не дублироваться
            server.createContext("/", new WebHandler());

            server.setExecutor(null);
            System.out.println(">> Server started on port 8080");
            System.out.println(">> Open Browser: http://localhost:8080");
            server.start();

        } else if (choice.equals("2")) {
            // --- ЗАПУСК КОНСОЛЬНОГО МЕНЮ ---
            ConsoleMenu menu = new ConsoleMenu(qRepo, cRepo);
            menu.start();
        } else {
            System.out.println("Invalid choice.");
        }
    }
}