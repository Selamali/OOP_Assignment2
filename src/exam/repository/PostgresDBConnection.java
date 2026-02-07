package exam.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// ВАЖНО: implements IDBConnection
public class PostgresDBConnection implements IDBConnection {

    private final String url = "---";
    private final String user = "---";
    private final String password = "---";

    @Override
    public Connection getConnection() throws SQLException {
        // Обязательно загружаем драйвер, чтобы избежать ошибок "No suitable driver"
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found!");
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, user, password);
    }
}
