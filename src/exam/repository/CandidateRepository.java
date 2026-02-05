package exam.repository;

import exam.model.Candidate;
import exam.exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateRepository implements ICandidateRepository {
    private final IDBConnection db;

    public CandidateRepository(IDBConnection db) {
        this.db = db;
    }

    @Override
    public List<Candidate> getAllCandidates() {
        List<Candidate> list = new ArrayList<>();
        String sql = "SELECT * FROM candidates ORDER BY name ASC";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Candidate(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getBoolean("is_registered"),
                        rs.getInt("score")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error loading candidates", e);
        }
        return list;
    }

    @Override
    public void addCandidate(Candidate c) {
        String sql = "INSERT INTO candidates (name, age, is_registered, score) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setInt(2, c.getAge());
            pstmt.setBoolean(3, c.isRegistered());
            pstmt.setInt(4, c.getScore());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error saving candidate", e);
        }
    }

    @Override
    public Candidate getCandidateByName(String name) {
        String sql = "SELECT * FROM candidates WHERE name = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Candidate(
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getBoolean("is_registered"),
                            rs.getInt("score")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding candidate", e);
        }
        return null;
    }

    @Override
    public void updateCandidate(String name, Candidate c) {
        String sql = "UPDATE candidates SET age = ?, score = ?, is_registered = ? WHERE name = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, c.getAge());
            pstmt.setInt(2, c.getScore());
            pstmt.setBoolean(3, c.isRegistered());
            pstmt.setString(4, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating candidate " + name, e);
        }
    }

    // РЕАЛИЗАЦИЯ УДАЛЕНИЯ
    @Override
    public void deleteCandidate(String name) {
        String sql = "DELETE FROM candidates WHERE name = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting candidate " + name, e);
        }
    }
}