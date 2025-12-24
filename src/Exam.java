public class Exam {
    private String subject;
    private double duration;

    public Exam(String subject, double duration) {
        this.subject = subject;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Exam: " + subject + " (" + duration + " min)";
    }
}