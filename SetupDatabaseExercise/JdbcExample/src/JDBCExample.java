import java.sql.*;

public class JDBCExample {

    private static String url;
    private static String user;
    private static String password;

    public static void main(String[] args) {
        url = System.getenv("DB_URL");
        user = System.getenv("DB_USER");
        password = System.getenv("DB_PASSWORD");

        // DeleteUsers(15, false);
//        DeleteUsers(0, true);
//        DeleteRoles(0, true);
//        PopulateDatabase();
//        Question1();
//        Question2();

        CreateUserRole(0, 1);
    }

    private static void PopulateDatabase()
    {
        for (int i = 0; i < 20; i++)
        {
            CreateUser("user" + i, "user" + i + "@gmail.com", "user" + i + "password");
        }

        for (int i = 0; i < 5; i++)
        {
            CreateRole("role" + i);
        }
    }

    public static void CreateUser(String userName, String userEmail, String userPassword)
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");
            String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, userName);
                stmt.setString(2, userEmail);
                stmt.setString(3, userPassword);
                stmt.executeUpdate();
                System.out.println("Data inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteUsers(int id, Boolean allUsers)
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");

            // ─────────────────────────────────────────
            // Delete user with id or all users
            // ─────────────────────────────────────────
            if (!allUsers) {
                String sql = "DELETE FROM users WHERE id = " + id;
                try (PreparedStatement deleteUserStatement = conn.prepareStatement(sql)) {
                    int numberOfRowsDeleted = deleteUserStatement.executeUpdate();
                    System.out.println(numberOfRowsDeleted + " rows deleted from users");
                }
            }
            else {
                // Disable auto-commit to start a transaction
                conn.setAutoCommit(false);

                // Disable foreign key checks
                String disableForeignKeyChecksSQL = "SET FOREIGN_KEY_CHECKS = 0";
                try (Statement disableForeignKeyChecksStatement = conn.createStatement()) {
                    disableForeignKeyChecksStatement.execute(disableForeignKeyChecksSQL);
                    System.out.println("Foreign key checks disabled.");
                }

                // Truncate the table
                String truncateTableSQL = "TRUNCATE TABLE users";
                try (Statement truncateTableStatement = conn.createStatement()) {
                    truncateTableStatement.execute(truncateTableSQL);
                    System.out.println("Table 'users' truncated.");
                }

                // Enable foreign key checks
                String enableForeignKeyChecksSQL = "SET FOREIGN_KEY_CHECKS = 1";
                try (Statement enableForeignKeyChecksStatement = conn.createStatement()) {
                    enableForeignKeyChecksStatement.execute(enableForeignKeyChecksSQL);
                    System.out.println("Foreign key checks enabled.");
                }

                // Commit the transaction
                conn.commit();
                System.out.println("Transaction committed successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void ShowAllUsers()
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");

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

    public static void CreateRole(String roleName)
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");
            String sql = "INSERT INTO roles (role_name) VALUES (?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, roleName);
                stmt.executeUpdate();
                System.out.println("Data inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteRoles(int id, Boolean allRoles)
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");

            // ─────────────────────────────────────────
            // Delete user with id or all Roles
            // ─────────────────────────────────────────
            if (!allRoles) {
                String sql = "DELETE FROM roles WHERE id = " + id;
                try (PreparedStatement deleteRolestatement = conn.prepareStatement(sql)) {
                    int numberOfRowsDeleted = deleteRolestatement.executeUpdate();
                    System.out.println(numberOfRowsDeleted + " rows deleted from roles");
                }
            }
            else {
                // Disable auto-commit to start a transaction
                conn.setAutoCommit(false);

                // Disable foreign key checks
                String disableForeignKeyChecksSQL = "SET FOREIGN_KEY_CHECKS = 0";
                try (Statement disableForeignKeyChecksStatement = conn.createStatement()) {
                    disableForeignKeyChecksStatement.execute(disableForeignKeyChecksSQL);
                    System.out.println("Foreign key checks disabled.");
                }

                // Truncate the table
                String truncateTableSQL = "TRUNCATE TABLE roles";
                try (Statement truncateTableStatement = conn.createStatement()) {
                    truncateTableStatement.execute(truncateTableSQL);
                    System.out.println("Table 'Roles' truncated.");
                }

                // Enable foreign key checks
                String enableForeignKeyChecksSQL = "SET FOREIGN_KEY_CHECKS = 1";
                try (Statement enableForeignKeyChecksStatement = conn.createStatement()) {
                    enableForeignKeyChecksStatement.execute(enableForeignKeyChecksSQL);
                    System.out.println("Foreign key checks enabled.");
                }

                // Commit the transaction
                conn.commit();
                System.out.println("Transaction committed successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ShowAllRoles()
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");

            // ─────────────────────────────────────────
            // Query all roles and print them
            // ─────────────────────────────────────────

            String selectSql = "SELECT id, role_name FROM roles";

            try (
                    Statement selectStmt = conn.createStatement();
                    ResultSet rs = selectStmt.executeQuery(selectSql)
            ) {
                System.out.println("\n─ Roles in DB ─");

                while (rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("role_name");

                    System.out.printf("[%d] %-20s%n", id, name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void CreateUserRole(int userId, int roleId)
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");
            String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1 ,userId);
                statement.setInt(2, roleId);
                statement.executeUpdate();
                System.out.println("Data inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void Question1()
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");

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

    public static void Question2()
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");

            // ─────────────────────────────────────────
            // Query all users and print them
            // ─────────────────────────────────────────

            String selectSql = "SELECT id, role_name FROM roles";

            try (
                    Statement selectStmt = conn.createStatement();
                    ResultSet rs = selectStmt.executeQuery(selectSql)
            ) {
                System.out.println("\n─ Roles in DB ─");

                while (rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("role_name");

                    System.out.printf("[%d] %-20s %n", id, name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}