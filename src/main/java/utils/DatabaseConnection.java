package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Private database credentials
    private static String url = "jdbc:mysql://localhost:3306/Mydatabase";
    private static String username = "root";
    private static String password = "root";

    // Method to establish connection and test it
    public static void DBconnect() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Attempt to establish a connection
            Connection connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("Connection established successfully!");
            }

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    // Method to establish connection for CRUD operations
    public static Connection DBconnect_crud() {
        Connection connection = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Attempt to establish a connection
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("Connection established successfully!");
            }

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
        return connection;
    }
}
