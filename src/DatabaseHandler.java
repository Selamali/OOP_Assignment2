import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "ramazananarbekov";
    private final String password = "1234";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void addQuestion(Question q) {
        String sql = "INSERT INTO questions (text, marks) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, q.getText());
            pstmt.setInt(2, q.getMarks());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(">> [DB Error] Failed to save question: " + e.getMessage());
        }
    }

    public List<Question> getQuestions() {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Question(rs.getString("text"), rs.getInt("marks")));
            }
        } catch (SQLException e) {
            System.err.println(">> [DB Error] Failed to fetch data: " + e.getMessage());
        }
        return list;
    }
}