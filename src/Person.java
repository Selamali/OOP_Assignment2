import java.util.Objects;

public abstract class Person {
    protected final String name;
    protected final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public abstract void displayRole();

    @Override
    public String toString() {
        return "Person: " + name + " (" + age + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}