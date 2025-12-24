public class Exam {
    private final String subject;
    private final double duration;

    public Exam(String subject, double duration) {
        this.subject = subject;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Exam: " + subject + " [" + duration + " min]";
    }
}