package shoppingapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class UserAuthentication {
    private Connection connection;
    private int currentUserId;
    private String currentUserRole;

    public UserAuthentication() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping_system", "root", "Spring2023@depaul");
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Database connection error: " + e.getMessage(), e);
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public boolean login(String username, String password) {
        String query = "SELECT id, role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentUserId = rs.getInt("id");
                currentUserRole = rs.getString("role");
                CustomLogger.log(Level.INFO, "User logged in successfully: " + username);
                return true;
            } else {
                CustomLogger.log(Level.WARNING, "Login failed for user: " + username);
                return false;
            }
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Error during login: " + e.getMessage(), e);
            return false;
        }
    }

    public void logout() {
        currentUserId = 0;
        currentUserRole = null;
        CustomLogger.log(Level.INFO, "User logged out successfully");
    }

    public boolean register(String username, String password, String role) {
        String checkUserQuery = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkUserQuery)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                CustomLogger.log(Level.WARNING, "Registration failed: Username " + username + " already exists");
                return false; // User already exists
            }
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Error during user existence check: " + e.getMessage(), e);
            return false;
        }

        String insertUserQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertUserQuery)) {
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, role);
            insertStmt.executeUpdate();
            CustomLogger.log(Level.INFO, "User registered successfully: " + username);
            return true;
        } catch (SQLException e) {
            CustomLogger.log(Level.SEVERE, "Error during user registration: " + e.getMessage(), e);
            return false;
        }
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }
}
