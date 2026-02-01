import java.util.Scanner;
import java.util.List;
import java.util.Collections;

public class ConsoleMenu {
    private final IQuestionRepository questionRepository;
    private final Scanner scanner;

    public ConsoleMenu(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        System.out.println("=========================================");
        System.out.println("   EXAM SYSTEM (SOLID VERSION)           ");
        System.out.println("=========================================");

        while (running) {
            try {
                printMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                running = handleChoice(choice);
            } catch (NumberFormatException e) {
                System.err.println(">> [Input Error] Numeric value required.");
            } catch (Exception e) {
                System.err.println(">> [Error] " + e.getMessage());
            }
        }
        System.out.println("System closed.");
    }

    private void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add New Question");
        System.out.println("2. View & Sort Questions");
        System.out.println("3. Update Question (Smart Edit)");
        System.out.println("4. Delete Question");
        System.out.println("5. Search by Keyword");
        System.out.println("6. Show Candidate Profile (OOP Demo)");
        System.out.println("7. Exit");
        System.out.print(">> Select: ");
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case 1 -> addQuestion();
            case 2 -> viewQuestions();
            case 3 -> updateQuestion();
            case 4 -> deleteQuestion();
            case 5 -> searchQuestions();
            case 6 -> showProfile();
            case 7 -> { return false; } // Exit
            default -> System.out.println("Invalid option.");
        }
        return true;
    }

    private void addQuestion() {
        System.out.print("Enter text: ");
        String text = scanner.nextLine();
        System.out.print("Enter marks: ");
        int marks = Integer.parseInt(scanner.nextLine());
        questionRepository.addQuestion(text, marks);
    }

    private void viewQuestions() {
        List<Question> list = questionRepository.getAllQuestions();
        if (list.isEmpty()) {
            System.out.println("List is empty.");
        } else {
            System.out.print("Sort by marks? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                Collections.sort(list);
            }
            list.forEach(System.out::println);
        }
    }

    private void updateQuestion() {
        System.out.print("Enter ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Question current = questionRepository.getQuestionById(id);

        if (current == null) {
            System.out.println(">> [Error] ID not found.");
            return;
        }

        System.out.println("Current text: [" + current.getText() + "]");
        System.out.print("New text (Enter to keep): ");
        String text = scanner.nextLine();
        if (text.trim().isEmpty()) text = current.getText();

        System.out.println("Current marks: [" + current.getMarks() + "]");
        System.out.print("New marks (Enter to keep): ");
        String marksStr = scanner.nextLine();
        int marks = marksStr.trim().isEmpty() ? current.getMarks() : Integer.parseInt(marksStr);

        questionRepository.updateQuestion(id, text, marks);
    }

    private void deleteQuestion() {
        System.out.print("Enter ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        questionRepository.deleteQuestion(id);
    }

    private void searchQuestions() {
        System.out.print("Enter keyword: ");
        String key = scanner.nextLine().toLowerCase();
        System.out.println("\n--- Search Results ---");

        // Используем Stream API как в твоем старом Main
        questionRepository.getAllQuestions().stream()
                .filter(q -> q.getText().toLowerCase().contains(key))
                .forEach(System.out::println);
    }

    private void showProfile() {
        Person p = new Candidate("Ramazan", 20, true);
        System.out.println("\n--- Candidate Profile ---");
        System.out.println(p);
        p.displayRole();
    }
}