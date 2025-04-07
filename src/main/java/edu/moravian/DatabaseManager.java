/**
 * DatabaseManager.java
 * This class manages the connection to the MySQL database for the Blackjack game.
 * It handles player data, including adding players, updating balances, and retrieving leaderboards.
 *
 */
package edu.moravian;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Map;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/blackjack";
    private static final String USER = "root";
    private static final String PASSWORD = "rootpass";

    private Connection connection;

    /**
     * Constructor to initialize the database connection.
     */
    public DatabaseManager() {
        try {
            // Connect to MySQL database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Creates the blackjack database and required tables if they don't already exist.
     * @return true if successful, false if an error occurred
     */
    public boolean createDatabaseIfNotExists() {
        String baseUrl = "jdbc:mysql://localhost:3306/";

        try (Connection conn = DriverManager.getConnection(baseUrl, USER, PASSWORD)) {
            // Check if database exists
            boolean databaseExists = false;
            try (ResultSet resultSet = conn.getMetaData().getCatalogs()) {
                while (resultSet.next()) {
                    String databaseName = resultSet.getString(1);
                    if ("blackjack".equalsIgnoreCase(databaseName)) {
                        databaseExists = true;
                        break;
                    }
                }
            }

            // Create database if it doesn't exist
            if (!databaseExists) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE blackjack");
                    System.out.println("Database 'blackjack' created successfully.");
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
     * Creates a database dump file for backup purposes using mysqldump utility.
     * @return true if dump was created successfully, false otherwise
     */
    public boolean createdumpfile() {
        try {
            // Create timestamp for unique filename
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String dumpFileName = "blackjack_backup_" + timestamp + ".sql";

            // Dump file path in current directory
            String dumpFilePath = System.getProperty("user.dir") + java.io.File.separator + dumpFileName;

            // Build mysqldump command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "mysqldump",
                    "--user=" + USER,
                    "--password=" + PASSWORD,
                    "--host=localhost",
                    "--port=3306",
                    "--add-drop-database",
                    "--databases", "blackjack",
                    "--result-file=" + dumpFilePath
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Wait for the process to complete
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Database dump created successfully at: " + dumpFilePath);
                return true;
            } else {
                System.err.println("Error creating database dump. Exit code: " + exitCode);
                try (java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("Exception creating database dump:");
            e.printStackTrace();
            return false;
        }
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
