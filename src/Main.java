import java.util.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DatabaseHandler db = new DatabaseHandler();

        System.out.println("=========================================");
        System.out.println("  Online Examination System 3.0  ");
        System.out.println("=========================================");

        try {
            // 1. Проверка соединения
            db.getConnection().close();
            System.out.println(">> [System] Database connected successfully.");

            // 2. Ввод новых вопросов в базу
            System.out.print("\nEnter number of questions to add: ");
            int count = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < count; i++) {
                System.out.println("\n--- Entry #" + (i + 1) + " ---");
                System.out.print("Enter question text: ");
                String text = scanner.nextLine();
                System.out.print("Enter marks: ");
                int marks = Integer.parseInt(scanner.nextLine());

                db.addQuestion(new Question(text, marks));
            }

            // 3. Получение данных из БД
            System.out.println("\n--- Synchronizing with PostgreSQL ---");
            List<Question> questionsFromDB = db.getQuestions();

            if (questionsFromDB.isEmpty()) {
                System.out.println(">> [System] The database is currently empty.");
            } else {
                // --- ВОТ ЭТОТ БЛОК МЫ ВЕРНУЛИ ---
                System.out.print("\n>> Would you like to sort questions by marks? (y/n): ");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("y")) {
                    Collections.sort(questionsFromDB); // Использует твой Comparable в Question
                    System.out.println(">> [Success] List sorted by difficulty.");
                }

                System.out.println("\n[ QUESTION POOL ]");
                for (Question q : questionsFromDB) {
                    System.out.println(q);
                }
                // --------------------------------
            }

            // 4. Демонстрация Полиморфизма (обязательное требование)
            System.out.println("\n-----------------------------------------");
            System.out.println(">> Checking Candidate Profile (Polymorphism)...");
            Person student = new Candidate("Ramazan", 18, true);
            System.out.println(student);
            student.displayRole(); // Override

        } catch (NumberFormatException e) {
            // Если ввели текст вместо числа
            System.err.println(">> [Input Error] Invalid number format. Please try again.");
        } catch (SQLException e) {
            // Если упала база или не подключен Driver
            System.err.println(">> [Database Error] Connection failed. Check your JAR driver and URL.");
            e.printStackTrace();
        } catch (Exception e) {
            // Все остальные ошибки
            System.err.println(">> [Critical Error] Something went wrong: " + e.getMessage());
        } finally {
            System.out.println("\n=========================================");
            System.out.println("        APPLICATION TERMINATED           ");
            System.out.println("=========================================");
            scanner.close();
        }
    }
}