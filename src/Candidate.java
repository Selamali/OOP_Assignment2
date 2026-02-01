import java.util.Objects;

public class Candidate extends Person {
    private boolean isOnline;

    public Candidate(String name, int age, boolean isOnline) {
        super(name, age);
        this.isOnline = isOnline;
    }

    // Перегрузка
    public void updateStatus(boolean status) {
        this.isOnline = status;
        System.out.println(">> [Log] Status updated to: " + isOnline);
    }

    public void updateStatus(String statusText) {
        this.isOnline = statusText.equalsIgnoreCase("ONLINE");
        System.out.println(">> [Log] Status updated from text: " + isOnline);
    }

    // Переопределение
    @Override
    public void displayRole() {
        System.out.println(">> Role: Student Candidate");
    }

    @Override
    public String toString() {
        return String.format("Candidate: %s | Age: %d | Online: %b", name, age, isOnline);
    }
}