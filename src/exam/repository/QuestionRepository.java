package exam.repository;

import exam.exception.DatabaseException;
import exam.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository implements IQuestionRepository {

    // SQL Константы (Обновлены с учетом category)
    private static final String SQL_INSERT = "INSERT INTO questions (text, marks, category) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM questions ORDER BY id DESC";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM questions WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE questions SET text = ?, marks = ?, category = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM questions WHERE id = ?";

    private final IDBConnection dbConnection;

    public QuestionRepository(IDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Обновленный метод: принимает category
    @Override
    public void addQuestion(String text, int marks, String category) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            stmt.setString(1, text);
            stmt.setInt(2, marks);
            stmt.setString(3, category != null ? category : "General"); // Защита от null
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to add question", e);
        }
    }

    @Override
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Question q = new Question.Builder()
                        .setId(rs.getInt("id"))
                        .setText(rs.getString("text"))
                        .setMarks(rs.getInt("marks"))
                        .setCategory(rs.getString("category")) // Читаем категорию
                        .build();
                questions.add(q);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get all questions", e);
        }
        return questions;
    }

    @Override
    public Question getQuestionById(int id) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Question.Builder()
                            .setId(rs.getInt("id"))
                            .setText(rs.getString("text"))
                            .setMarks(rs.getInt("marks"))
                            .setCategory(rs.getString("category")) // Читаем категорию
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get question by id " + id, e);
        }
        return null;
    }

    // Обновленный метод: принимает category
    @Override
    public void updateQuestion(int id, String text, int marks, String category) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, text);
            stmt.setInt(2, marks);
            stmt.setString(3, category);
            stmt.setInt(4, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update question " + id, e);
        }
    }

    @Override
    public List<Question> getRandomQuestions(int limit) {
        List<Question> questions = new ArrayList<>();
        // PostgreSQL использует RANDOM(), для MySQL/H2 может быть RAND()
        String sql = "SELECT * FROM questions ORDER BY RANDOM() LIMIT ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    questions.add(new Question.Builder()
                            .setId(rs.getInt("id"))
                            .setText(rs.getString("text"))
                            .setMarks(rs.getInt("marks"))
                            .setCategory(rs.getString("category"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get random questions", e);
        }
        return questions;
    }
    @Override
    public void deleteQuestion(int id) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete question " + id, e);
        }
    }
}