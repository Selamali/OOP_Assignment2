import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDBConnection implements IDBConnection {
    // Данные для входа переехали сюда
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "ramazananarbekov";
    private final String password = "12345";

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}