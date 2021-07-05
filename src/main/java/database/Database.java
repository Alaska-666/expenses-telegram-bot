package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private void createTable(String sql) {
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

    public void createExpensesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS expenses (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " date text NOT NULL,\n"
                + "	name text NOT NULL,\n"
                + " people text NOT NULL,\n"
                + "	cost real NOT NULL,\n"
                + " payer text NOT NULL,\n"
                + " status text  NOT NULL\n"
                + ");";
        createTable(sql);
    }

    public void createExpenseCategoriesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS expense_categories (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " category text NOT NULL\n" +
                ");";
        createTable(sql);
    }

    public void addExpenseCategory(String category) {
        String sql = "INSERT INTO expense_categories(category) VALUES(?)";
        Connection conn = getConn();
        if (conn == null) {
            return;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
    }

    public List<String> readExpenseCategories() {
        String sql = "SELECT category FROM expense_categories";
        Connection conn = getConn();
        if (conn == null) {
            return Collections.emptyList();
        }
        List<String> categories = new ArrayList<>();
        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categories;
    }
}
