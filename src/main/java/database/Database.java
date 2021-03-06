package database;

import expense.Expense;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
                + "	cost real NOT NULL,\n"
                + " payer text NOT NULL,\n"
                + " people text NOT NULL,\n"
                + " status text  NOT NULL\n"
                + ");";
        createTable(sql);
    }

    public void createExpenseCategoriesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS expense_categories (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " category text NOT NULL,\n"
                + " username text NOT NULL\n"
                + ");";
        createTable(sql);
    }

    public void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses(date, name, cost, payer, people, status) VALUES(?,?,?,?,?,?)";
        Connection conn = getConn();
        if (conn == null) {
            return;
        }
        String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, expense.getName());
            pstmt.setInt(3, expense.getCost());
            pstmt.setString(4, expense.getPayer());
            pstmt.setString(5, expense.getWhoseExpense());
            pstmt.setString(6, "open");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " username text NOT NULL\n"
                + ");";
        createTable(sql);
    }

    public void addUser(String username) {
        String sql = "INSERT INTO users(username) VALUES(?)";
        Connection conn = getConn();
        if (conn == null) {
            return;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
    }

    public void addExpensesToExcelTable(Sheet sheet, CellStyle style, int startRowIndex) {
        String sql = "SELECT date, name, cost, payer, people, status FROM expenses";
        Connection conn = getConn();
        if (conn == null) {
            return;
        }
        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                ResultSetMetaData metadata = rs.getMetaData();
                int columnCount = metadata.getColumnCount();
                Row row = sheet.createRow(startRowIndex++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i-1);
                    cell.setCellValue(rs.getString(i));
                    cell.setCellStyle(style);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
    }

    public List<Expense> readActualExpensesForPeople(String people) {
        String sql = String.format("SELECT name, cost, payer FROM expenses WHERE people = '%s' AND status = '%s' ;",
                people, "open");
        Connection conn = getConn();
        if (conn == null) {
            return Collections.emptyList();
        }
        List<Expense> expenses = new ArrayList<>();
        try (ResultSet rs    = conn.createStatement().executeQuery(sql)){
            while (rs.next()) {
                expenses.add(new Expense(rs.getString("name"), rs.getString("payer"), rs.getInt("cost")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
        return expenses;
    }

    public void closedActualExpensesForPeople(String people) {
        String sql = String.format("UPDATE expenses SET status = 'closed' WHERE people = '%s' AND status = '%s' ;",
                people, "open");
        Connection conn = getConn();
        if (conn == null) {
            return;
        }
        try {
            conn.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
    }

    public List<String> readUsers() {
        String sql = "SELECT username FROM users";
        Connection conn = getConn();
        if (conn == null) {
            return Collections.emptyList();
        }
        List<String> users = new ArrayList<>();
        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
        return users;
    }


    public void addExpenseCategory(String category, String username) {
        String sql = "INSERT INTO expense_categories(category, username) VALUES(?,?)";
        Connection conn = getConn();
        if (conn == null) {
            return;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
    }

    public Map<String, List<String>> readExpenseCategories() {
        String sql = "SELECT category, username FROM expense_categories";
        Connection conn = getConn();
        if (conn == null) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> categories = new HashMap<>();
        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                String username = rs.getString("username");
                String category = rs.getString("category");
                if (categories.containsKey(username)) {
                    categories.get(username).add(category);
                } else {
                    List<String> values = new ArrayList<>();
                    values.add(category);
                    categories.put(username, values);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
        return categories;
    }
}
