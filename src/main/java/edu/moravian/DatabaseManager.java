/**
 * DatabaseManager.java
 * This class manages the connection to the MySQL database for the Blackjack game.
 * It handles player data, including adding players, updating balances, and retrieving leaderboards.
 *
 */
package edu.moravian;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Map;

public class DatabaseManager {

    private Connection connection;
    private String URL;
    private String USER;
    private String PASSWORD;

    /**
     * Constructor to initialize the database connection.
     */
    public DatabaseManager() {
        // Read from environment variables with defaults
        String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
        String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
        String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "blackjack";
        USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
        PASSWORD = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "rootpass";
        
        URL = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        
        try {
            // Connect to MySQL database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL database at " + URL);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database at " + URL);
            e.printStackTrace();
        }
    }
    
    /**
 * Creates the blackjack database and required tables if they don't already exist.
 * @return true if successful, false if an error occurred
 */
public boolean createDatabaseIfNotExists() {
    String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
    String baseUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/";
    String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "blackjack";

    try (Connection conn = DriverManager.getConnection(baseUrl, USER, PASSWORD)) {
        // Check if database exists
        boolean databaseExists = false;
        try (ResultSet resultSet = conn.getMetaData().getCatalogs()) {
            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (dbName.equalsIgnoreCase(databaseName)) {
                    databaseExists = true;
                    break;
                }
            }
        }

        // Create database if it doesn't exist
        if (!databaseExists) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE " + dbName);
                System.out.println("Database '" + dbName + "' created successfully.");
            }
        }

        // Connect to the blackjack database to create tables if needed
        try (Connection dbConn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = dbConn.createStatement()) {

            // Create players table if it doesn't exist
            String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL UNIQUE," +
                    "balance INT NOT NULL DEFAULT 40)";
            stmt.executeUpdate(createPlayersTable);
            System.out.println("Table 'players' checked/created successfully.");

            return true;
        }
    } catch (SQLException e) {
        System.err.println("Error creating database or tables:");
        e.printStackTrace();
        return false;
    }
}
    
    /**
     * Creates a database dump file using JDBC instead of mysqldump
     * @return true if dump was created successfully, false otherwise
     */
    public boolean createdumpfile() {
        String dumpFileName = "blackjackdump.sql";
        String dumpFilePath = System.getProperty("user.dir") + File.separator + dumpFileName;

        try (FileWriter writer = new FileWriter(dumpFilePath)) {
            // Write header
            writer.write("-- Blackjack Database Dump\n");
            writer.write("-- Generated: " + new java.util.Date() + "\n\n");
            writer.write("USE blackjack;\n\n");

            // Export players table structure
            writer.write("DROP TABLE IF EXISTS players;\n");
            writer.write("CREATE TABLE players (\n");
            writer.write("  id INT AUTO_INCREMENT PRIMARY KEY,\n");
            writer.write("  name VARCHAR(255) NOT NULL UNIQUE,\n");
            writer.write("  balance INT NOT NULL DEFAULT 40\n");
            writer.write(");\n\n");

            // Export player data
            String query = "SELECT * FROM players";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name").replace("'", "''"); // Escape quotes
                    int balance = rs.getInt("balance");

                    writer.write(String.format("INSERT INTO players (id, name, balance) VALUES (%d, '%s', %d);\n",
                            id, name, balance));
                }
            }

            System.out.println("Database dump created successfully at: " + dumpFilePath);
            return true;
        } catch (Exception e) {
            System.err.println("Exception creating database dump:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Imports the database from the dump file if it exists
     * @return true if import was successful, false otherwise
     */
    public boolean importDumpFile() {
        String dumpFileName = "blackjackdump.sql";
        String dumpFilePath = System.getProperty("user.dir") + File.separator + dumpFileName;

        // Check if dump file exists
        File dumpFile = new File(dumpFilePath);
        if (!dumpFile.exists()) {
            System.out.println("No dump file found at: " + dumpFilePath);
            return false;
        }

        try {
            // Read SQL statements from file
            List<String> sqlStatements = new ArrayList<>();
            StringBuilder statement = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(dumpFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip comments and empty lines
                    if (line.startsWith("--") || line.trim().isEmpty()) {
                        continue;
                    }

                    statement.append(line);

                    // If statement complete
                    if (line.trim().endsWith(";")) {
                        sqlStatements.add(statement.toString());
                        statement = new StringBuilder();
                    }
                }
            }

            // Execute SQL statements
            try (Statement stmt = connection.createStatement()) {
                for (String sql : sqlStatements) {
                    // Skip USE statements as we're already connected
                    if (!sql.trim().toUpperCase().startsWith("USE")) {
                        stmt.execute(sql);
                    }
                }
            }

            System.out.println("Database imported successfully from: " + dumpFilePath);
            return true;
        } catch (Exception e) {
            System.err.println("Exception importing database dump:");
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Updates the dump file with the current database state.
     * This method can be called explicitly or automatically on program shutdown.
     * @return true if dump was updated successfully, false otherwise
     */
    public boolean updatedumpfile() {
        return createdumpfile(); // Reuse the existing dump functionality
    }
    /**
     * Method to create the players table if it doesn't exist.
     */
    public List<Map.Entry<String, Integer>> getLeaderboard() {
        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>();
        String query = "SELECT name, balance FROM players ORDER BY balance DESC";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String playerName = resultSet.getString("name");
                int balance = resultSet.getInt("balance");
                leaderboard.add(new AbstractMap.SimpleEntry<>(playerName, balance));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }


    /**
     * Method that adds a new player to the database if they don't already exist.
     */
    // Add a new player to the database (if not already existing)
    public void addPlayer(String name, int defaultBalance) {
        String checkQuery = "SELECT COUNT(*) FROM players WHERE name = ?";
        String insertQuery = "INSERT INTO players (name, balance) VALUES (?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, name);
                    insertStmt.setInt(2, defaultBalance); // Default balance
                    insertStmt.executeUpdate();
                    System.out.println("Player " + name + " has been added to the database with " + defaultBalance + " coins.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to retrieve the balance of a player from the database.
     * @param name
     * @return
     */

    // Retrieve the balance of a player from the database if they are at 0 coins, delete them
    public int getPlayerBalance(String name) {
        String query = "SELECT balance FROM players WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Indicate error
    }

    // Update the player's balance in the database


    // Close the database connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Method to check if a player exists in the database.
     * @param username
     * @return
     */

    public boolean playerExists(String username) {
        String query = "SELECT COUNT(*) FROM players WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Method to update the player's balance in the database.
     * @param username
     * @param adjustment
     * @param reason
     * @return
     */

    public int updateCurrency(String username, int adjustment, String reason) {
        try {
            String updateQuery = "UPDATE players SET balance = balance + ? WHERE name = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, adjustment);
                updateStmt.setString(2, username);
                updateStmt.executeUpdate();
            }

            int newBalance = getPlayerBalance(username);

            if (newBalance <= 0) {
                deletePlayer(username);
                System.out.println("Player " + username + " has lost all balance and has been reset. Balance will be set to 40 when they play again.");
                return 0; // Indicate reset
            }

            return newBalance;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Indicate error
    }

    /**
     * Method to delete a player from the database if their balance is 0.
     * @param username
     */
private void deletePlayer(String username) {
    String query = "DELETE FROM players WHERE name = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.executeUpdate();
        System.out.println("Player " + username + " has been deleted from the database.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    /**
     * Method to give currency to a player.(Admin command)
     * @param username
     * @param i
     */
    public void giveCurrency(String username, int i) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "UPDATE players SET balance = balance + ? WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, i);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
