import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository implements IQuestionRepository {
    private final IDBConnection dbConnection;

    // Мы передаем "подключатель" в конструктор. 
    // Репозиторию все равно, Postgres там или нет.
    public QuestionRepository(IDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void addQuestion(String text, int marks) {
        String sql = "INSERT INTO questions (text, marks) VALUES (?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, text);
            pstmt.setInt(2, marks);
            pstmt.executeUpdate();
            System.out.println(">> [Repo] Question saved.");
        } catch (SQLException e) {
            System.err.println(">> [Repo Error] Save failed: " + e.getMessage());
        }
    }

    @Override
    public List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Question(rs.getInt("id"), rs.getString("text"), rs.getInt("marks")));
            }
        } catch (SQLException e) {
            System.err.println(">> [Repo Error] Fetch failed: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Question getQuestionById(int id) {
        String sql = "SELECT * FROM questions WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Question(rs.getInt("id"), rs.getString("text"), rs.getInt("marks"));
                }
            }
        } catch (SQLException e) {
            System.err.println(">> [Repo Error] Search failed: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateQuestion(int id, String text, int marks) {
        String sql = "UPDATE questions SET text = ?, marks = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, text);
            pstmt.setInt(2, marks);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println(">> [Repo] Question updated.");
        } catch (SQLException e) {
            System.err.println(">> [Repo Error] Update failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteQuestion(int id) {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println(">> [Repo] Question deleted.");
            else System.out.println(">> [Repo] Not found.");
        } catch (SQLException e) {
            System.err.println(">> [Repo Error] Delete failed: " + e.getMessage());
        }
    }
}