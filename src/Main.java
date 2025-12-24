import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Question> pool = new ArrayList<>(); // Пул данных

        System.out.println("=== EXAM SYSTEM ===");

        // --- 1. Ввод данных ---
        System.out.print("Count of questions: ");
        int count = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера

        for (int i = 0; i < count; i++) {
            System.out.println("Question #" + (i + 1));

            System.out.print("Text: ");
            String text = scanner.nextLine();

            System.out.print("Marks: ");
            int marks = scanner.nextInt();
            scanner.nextLine();

            pool.add(new Question(text, marks));
        }

        // --- 2. Вывод списка ---
        System.out.println("\n--- Current Pool ---");
        for (Question q : pool) {
            System.out.println(q);
        }

        // --- 3. Сортировка ---
        System.out.print("\nSort by marks? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            Collections.sort(pool); // Сортировка через Comparable
            System.out.println(">> Sorted:");
            for (Question q : pool) System.out.println(q);
        }

        // --- 4. Полиморфизм ---
        System.out.println("\n=== CANDIDATE ===");
        System.out.print("Name: ");
        String cName = scanner.nextLine();

        // Dynamic Polymorphism (Parent Ref -> Child Obj)
        Person student = new Candidate(cName, 20, true);
        System.out.println(student);
        student.displayRole(); // Override

        // Static Polymorphism (Overloading)
        System.out.println("\n--- Overloading Check ---");
        Candidate c = (Candidate) student;

        c.updateStatus(false);       // boolean
        c.updateStatus("ONLINE");    // String

        scanner.close();
    }
}