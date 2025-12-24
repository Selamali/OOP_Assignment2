import java.util.Objects;

// Класс-наследник (Inheritance)
public class Candidate extends Person {
    private boolean isOnline;

    public Candidate(String name, int age, boolean isOnline) {
        super(name, age); // Вызов конструктора родителя
        this.isOnline = isOnline;
    }

    // --- Static Polymorphism (Overloading) ---

    // Перегрузка: аргумент boolean
    public void updateStatus(boolean status) {
        this.isOnline = status;
        System.out.println(">> Status updated (bool): " + isOnline);
    }

    // Перегрузка: аргумент String
    public void updateStatus(String statusText) {
        this.isOnline = statusText.equalsIgnoreCase("ONLINE");
        System.out.println(">> Status updated (String): " + isOnline);
    }

    // --- Dynamic Polymorphism (Overriding) ---

    // Переопределение метода родителя
    @Override
    public void displayRole() {
        System.out.println(">> Role: Candidate (Student)");
    }

    @Override
    public String toString() {
        return "Candidate: " + name + " (" + age + ") | Online: " + isOnline;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Candidate c = (Candidate) o;
        return isOnline == c.isOnline;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isOnline);
    }
}