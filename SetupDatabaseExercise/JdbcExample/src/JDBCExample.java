import java.sql.*;

public class JDBCExample {

    public static void main(String[] args) {
        System.out.println("Output");

        String url = "jdbc:mysql://localhost:3306/Studies"; // from XAMPP
        String user = "root";
        String password = ""; // XAMPP default has no password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");

            // ─────────────────────────────────────────
            // Insert Data and show
            // ─────────────────────────────────────────
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "Mario");
                stmt.setString(2, "Mario@example.com");
                stmt.executeUpdate();
                System.out.println("Data inserted successfully.");
            }

            // ─────────────────────────────────────────
            // Query all users and print them
            // ─────────────────────────────────────────

            String selectSql = "SELECT id, name, email FROM users";

            try (
                    Statement selectStmt = conn.createStatement();
                    ResultSet rs = selectStmt.executeQuery(selectSql)
            ) {
                System.out.println("\n─ Users in DB ─");

                while (rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");

                    System.out.printf("[%d] %-20s %s%n", id, name, email);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}