import java.sql.*;
import java.util.Random;

public class JDBCExample {

    private static String url;
    private static String user;
    private static String password;

    public static void main(String[] args) {
        url = System.getenv("DB_URL");
        user = System.getenv("DB_USER");
        password = System.getenv("DB_PASSWORD");
        // PopulateDatabase();
        Question1();
        Question2();
    }

    private static void PopulateDatabase()
    {
        DeleteUsers(0, true);
        DeleteRoles(0, true);
        DeleteUserRoles(0, 0, true);
        for (int i = 0; i < 20; i++)
        {
            CreateUser("user" + i, "user" + i + "@gmail.com", "user" + i + "password");
        }

        for (int i = 0; i < 5; i++)
        {
            CreateRole("role" + i);
        }

        Random rnd = new Random();
        for (int i = 0; i < 50; i++)
        {
            CreateUserRole(rnd.nextInt(20) + 1, rnd.nextInt(5) + 1);
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

    public static void DeleteUserRoles(int user_id, int role_id, Boolean allUserRoles)
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");

            if (!allUserRoles) {
                String sql = "DELETE FROM user_roles WHERE id = (" + user_id + "," + role_id + ")";
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
                String truncateTableSQL = "TRUNCATE TABLE user_roles";
                try (Statement truncateTableStatement = conn.createStatement()) {
                    truncateTableStatement.execute(truncateTableSQL);
                    System.out.println("Table 'User Roles' truncated.");
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

            String sqlSelect = "SELECT * FROM user_roles WHERE user_id = ? AND role_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sqlSelect))
            {
                statement.setInt(1, userId);
                statement.setInt(2, roleId);
                try (ResultSet rs = statement.executeQuery()) {

                    if (rs.next()) {
                        // User role already exists
                        System.out.println(
                                "User role with user_id = "
                                        + userId
                                        + " and role_id = "
                                        + roleId
                                        + " already exists.");
                    } else {
                        // User role does not exist, so insert it
                        String sqlInsert = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
                        try (PreparedStatement insertStatement = conn.prepareStatement(sqlInsert)) {
                            insertStatement.setInt(1, userId);
                            insertStatement.setInt(2, roleId);
                            insertStatement.executeUpdate();
                            System.out.println("Data inserted successfully.");
                        }
                    }
                }
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

    public static void Question1()
    {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database");

            // ─────────────────────────────────────────
            // Query all users and print them
            // ─────────────────────────────────────────

            String selectSql = "SELECT id, name, email FROM users ORDER BY created_at DESC";

            try (
                    Statement selectStmt = conn.createStatement();
                    ResultSet rs = selectStmt.executeQuery(selectSql)
            ) {
                System.out.println("\n─ Question 1: Users in DB ─");

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
            // Query all distics roles and print them
            // ─────────────────────────────────────────

            String selectSql = "SELECT DISTINCT role_name FROM roles ORDER BY role_name";

            try (
                    Statement selectStmt = conn.createStatement();
                    ResultSet rs = selectStmt.executeQuery(selectSql)
            ) {
                System.out.println("\n─ Question 2: Roles in DB ─");

                while (rs.next()) {
                    String roleName = rs.getString("role_name");
                    System.out.printf("%s%n", roleName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void Question3()
    {
        
    }
}