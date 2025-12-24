import java.util.Objects;

public class Candidate extends Person {
    private boolean isOnline;

    public Candidate(String name, int age, boolean isOnline) {
        super(name, age);
        this.isOnline = isOnline;
    }

    // --- Static Polymorphism (Overloading) ---
    public void updateStatus(boolean status) {
        this.isOnline = status;
        System.out.println(">> [Log] Status set to boolean: " + isOnline);
    }

    public void updateStatus(String statusText) {
        this.isOnline = statusText.equalsIgnoreCase("ONLINE");
        System.out.println(">> [Log] Status set from String: " + isOnline);
    }

    // --- Dynamic Polymorphism (Overriding) ---
    @Override
    public void displayRole() {
        System.out.println(">> Role: Student Candidate");
    }

    @Override
    public String toString() {
        return String.format("Candidate: %s | Age: %d | Online: %b", name, age, isOnline);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Candidate that = (Candidate) o;
        return isOnline == that.isOnline;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isOnline);
    }
}