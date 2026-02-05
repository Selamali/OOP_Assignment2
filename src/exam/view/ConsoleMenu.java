package exam.view;

import exam.model.Question;
import exam.model.Person;
import exam.model.Candidate;
import exam.repository.IQuestionRepository;
import exam.repository.ICandidateRepository;
import exam.exception.DatabaseException;
import exam.exception.QuestionNotFoundException;

import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleMenu {
    private final IQuestionRepository questionRepository;
    private final ICandidateRepository candidateRepository;
    private final Scanner scanner;

    public ConsoleMenu(IQuestionRepository questionRepository, ICandidateRepository candidateRepository) {
        this.questionRepository = questionRepository;
        this.candidateRepository = candidateRepository;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        System.out.println("=========================================");
        System.out.println("   EXAM SYSTEM (V5.0 PRO)                ");
        System.out.println("=========================================");

        while (running) {
            try {
                printMenu();
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) continue;

                int choice = Integer.parseInt(input);
                running = handleChoice(choice);
            } catch (NumberFormatException e) {
                System.err.println(">> [Input Error] Please enter a valid number.");
            } catch (DatabaseException e) {
                System.err.println(">> [Critical DB Error] " + e.getMessage());
            } catch (QuestionNotFoundException e) {
                System.err.println(">> [Logic Error] " + e.getMessage());
            } catch (Exception e) {
                System.err.println(">> [Unexpected Error] " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("System closed.");
    }

    private void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add New Question");
        System.out.println("2. View & Sort Questions");
        System.out.println("3. Update Question");
        System.out.println("4. Delete Question");
        System.out.println("5. Search Questions");
        System.out.println("6. Show All Candidates");
        System.out.println("7. Find Candidate Profile");
        System.out.println("8. Exit");
        System.out.print(">> Select: ");
    }

    private boolean handleChoice(int choice) throws QuestionNotFoundException {
        switch (choice) {
            case 1 -> addQuestion();
            case 2 -> viewQuestions();
            case 3 -> updateQuestion();
            case 4 -> deleteQuestion();
            case 5 -> searchQuestions();
            case 6 -> viewCandidates();
            case 7 -> showProfile();
            case 8 -> { return false; }
            default -> System.out.println("Invalid option.");
        }
        return true;
    }

    // --- ТАБЛИЦЫ ---

    private void printQuestionTable(List<Question> list) {
        System.out.println("+------+-----------------+----------------------------------+-------+");
        System.out.printf("| %-4s | %-15s | %-32s | %-5s |%n", "ID", "Category", "Question Text", "Marks");
        System.out.println("+------+-----------------+----------------------------------+-------+");

        if (list.isEmpty()) {
            System.out.printf("| %-68s |%n", " No questions found.");
        } else {
            for (Question q : list) {
                String text = q.getText();
                if (text.length() > 30) text = text.substring(0, 27) + "...";

                // Используем категорию (с защитой от null)
                String cat = q.getCategory();
                if (cat == null) cat = "General";
                if (cat.length() > 15) cat = cat.substring(0, 12) + "...";

                System.out.printf("| %-4d | %-15s | %-32s | %-5d |%n", q.getId(), cat, text, q.getMarks());
            }
        }
        System.out.println("+------+-----------------+----------------------------------+-------+");
    }

    private void printCandidateTable(List<Candidate> list) {
        System.out.println("+----------------------+-------+-------+------------+");
        System.out.printf("| %-20s | %-5s | %-5s | %-10s |%n", "Name", "Age", "Score", "Status");
        System.out.println("+----------------------+-------+-------+------------+");

        if (list.isEmpty()) {
            System.out.printf("| %-49s |%n", " No candidates found.");
        } else {
            for (Candidate c : list) {
                String status = c.isRegistered() ? "Registered" : "Pending";
                System.out.printf("| %-20s | %-5d | %-5d | %-10s |%n",
                        c.getName(), c.getAge(), c.getScore(), status);
            }
        }
        System.out.println("+----------------------+-------+-------+------------+");
    }

    // --- ЛОГИКА ---

    private void addQuestion() {
        System.out.print("Enter category (Java, SQL, etc.): ");
        String category = scanner.nextLine();

        System.out.print("Enter text: ");
        String text = scanner.nextLine();

        System.out.print("Enter marks: ");
        int marks = Integer.parseInt(scanner.nextLine());

        // ИСПРАВЛЕНО: передаем 3 аргумента
        questionRepository.addQuestion(text, marks, category);
        System.out.println(">> Success! Question added.");
    }

    private void viewQuestions() {
        List<Question> list = questionRepository.getAllQuestions();
        questionRepository.printStats(list);
        if (!list.isEmpty()) {
            System.out.print("Sort by marks descending? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                list = list.stream()
                        .sorted((q1, q2) -> Integer.compare(q2.getMarks(), q1.getMarks()))
                        .collect(Collectors.toList());
            }
        }
        printQuestionTable(list);
    }

    private void updateQuestion() throws QuestionNotFoundException {
        System.out.print("Enter ID to edit: ");
        int id = Integer.parseInt(scanner.nextLine());
        Question current = questionRepository.getQuestionById(id);

        if (current == null) throw new QuestionNotFoundException("ID " + id + " not found.");

        System.out.println(">> Current: [" + current.getCategory() + " | " + current.getText() + " | " + current.getMarks() + " pts]");

        System.out.print("New category (Enter to keep): ");
        String category = scanner.nextLine();
        if (category.trim().isEmpty()) category = current.getCategory();

        System.out.print("New text (Enter to keep): ");
        String text = scanner.nextLine();
        if (text.trim().isEmpty()) text = current.getText();

        System.out.print("New marks (Enter to keep): ");
        String marksStr = scanner.nextLine();
        int marks = marksStr.trim().isEmpty() ? current.getMarks() : Integer.parseInt(marksStr);

        // ИСПРАВЛЕНО: передаем 4 аргумента
        questionRepository.updateQuestion(id, text, marks, category);
        System.out.println(">> Success! Updated.");
    }

    private void deleteQuestion() {
        System.out.print("Enter ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        questionRepository.deleteQuestion(id);
        System.out.println(">> Success! Deleted.");
    }

    private void searchQuestions() {
        System.out.print("Enter keyword: ");
        String key = scanner.nextLine().toLowerCase();
        List<Question> results = questionRepository.getAllQuestions().stream()
                .filter(q -> q.getText().toLowerCase().contains(key) ||
                        (q.getCategory() != null && q.getCategory().toLowerCase().contains(key)))
                .collect(Collectors.toList());
        printQuestionTable(results);
    }

    private void viewCandidates() {
        System.out.println("\nLoading candidates...");
        printCandidateTable(candidateRepository.getAllCandidates());
    }

    private void showProfile() {
        System.out.print("Enter candidate name: ");
        String name = scanner.nextLine();
        System.out.println("\n--- Profile ---");
        // Заглушка, можно заменить на реальный поиск если нужно
        Person p = new Candidate(name, 20, true, 85);
        System.out.println(p.toString());
        p.displayRole();
    }
}