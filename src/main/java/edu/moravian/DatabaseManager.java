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

    public DatabaseManager() {
        try {
            // Connect to MySQL database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
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
// if the player has a balance of 0, delete them from the database
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
