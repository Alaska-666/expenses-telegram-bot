import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final String databaseConnectionUrl;

    public Database(String databaseConnectionUrl) {
        this.databaseConnectionUrl = databaseConnectionUrl;
    }

    private Connection getConn() {
        try {
            return DriverManager.getConnection(databaseConnectionUrl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS expenses (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " date text NOT NULL,\n"
                + "	name text NOT NULL,\n"
                + " people text NOT NULL,\n"
                + "	cost real NOT NULL,\n"
                + " payer text NOT NULL,\n"
                + " status text  NOT NULL"
                + ");";
        Connection connection = getConn();
        if (connection == null) {
            return;
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(connection);
    }
}
