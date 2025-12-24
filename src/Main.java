import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Question> pool = new ArrayList<>();

        System.out.println("=========================================");
        System.out.println("       EXAM MANAGEMENT SYSTEM v2.0       ");
        System.out.println("=========================================");

        // --- 1. Ввод вопросов ---
        System.out.print(">> Enter number of questions to add: ");
        int count = scanner.nextInt();
        scanner.nextLine(); // Fix buffer

        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Adding Question " + (i + 1) + " ---");
            System.out.print("Text: ");
            String text = scanner.nextLine();
            System.out.print("Marks: ");
            int marks = scanner.nextInt();
            scanner.nextLine();

            pool.add(new Question(text, marks));
        }

        // --- 2. Сортировка ---
        System.out.println("\n-----------------------------------------");
        System.out.print(">> Sort questions by difficulty? (y/n): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {
            Collections.sort(pool);
            System.out.println(">> [SUCCESS] List sorted!");
        }

        System.out.println("\n[ CURRENT QUESTION POOL ]");
        for (Question q : pool) {
            System.out.println(q);
        }

        // --- 3. Интерактивный поиск (НОВОЕ!) ---
        System.out.println("\n-----------------------------------------");
        System.out.print(">> Enter keyword to SEARCH (e.g. Java): ");
        String keyword = scanner.nextLine();

        System.out.println("[ SEARCH RESULTS ]");
        boolean found = false;
        for (Question q : pool) {
            if (q.getText().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(q);
                found = true;
            }
        }
        if (!found) System.out.println("No questions found.");

        // --- 4. Полиморфизм ---
        System.out.println("\n-----------------------------------------");
        System.out.println(">> Generating Candidate Profile...");

        // Полиморфизм: Person -> Candidate
        Person student = new Candidate("Alex Doe", 21, true);
        System.out.println(student);
        student.displayRole(); // Override check

        System.out.println("\n>> Updating Status (Overloading Check)...");
        Candidate c = (Candidate) student;
        c.updateStatus(false);       // boolean
        c.updateStatus("ONLINE");    // String

        System.out.println("\n=========================================");
        System.out.println("           SYSTEM SHUTDOWN               ");
        scanner.close();
    }
}