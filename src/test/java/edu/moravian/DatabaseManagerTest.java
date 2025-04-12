package edu.moravian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {
    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() {
        databaseManager = new DatabaseManager();
        databaseManager.createDatabaseIfNotExists();

        try (Connection conn = DriverManager.getConnection(
             "jdbc:mysql://localhost:3306/blackjack", "root", "rootpass");
         Statement stmt = conn.createStatement()) {
        stmt.executeUpdate("DELETE FROM players");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    @Test
    void testAddPlayer() {
        String testUsername = "TestUser" + System.currentTimeMillis();
        databaseManager.addPlayer(testUsername, 40);
        assertTrue(databaseManager.playerExists(testUsername));
    }

    @Test
    void testPlayerExists() {
        String testUsername = "TestUser" + System.currentTimeMillis();
        assertFalse(databaseManager.playerExists(testUsername));
        databaseManager.addPlayer(testUsername, 40);
        assertTrue(databaseManager.playerExists(testUsername));
    }

    @Test
    void testGetPlayerBalance() {
        String testUsername = "TestUser" + System.currentTimeMillis();
        databaseManager.addPlayer(testUsername, 40);
        int balance = databaseManager.getPlayerBalance(testUsername);
        assertEquals(40, balance);
    }

    @Test
    void testUpdateCurrency() {
        String testUsername = "TestUser" + System.currentTimeMillis();
        databaseManager.addPlayer(testUsername, 40);

        int newBalance = databaseManager.updateCurrency(testUsername, 10, "test");
        assertEquals(50, newBalance);

        newBalance = databaseManager.updateCurrency(testUsername, -20, "test");
        assertEquals(30, newBalance);
    }

    @Test
    void testGiveCurrency() {
        String testUsername = "TestUser" + System.currentTimeMillis();
        databaseManager.addPlayer(testUsername, 40);

        databaseManager.giveCurrency(testUsername, 60);
        int balance = databaseManager.getPlayerBalance(testUsername);
        assertEquals(100, balance);
    }

    @Test
    void testLeaderboard() {
        // This test assumes you have some data in the database
        // You might want to add a few test players first
        String testUsername1 = "TestUser1" + System.currentTimeMillis();
        String testUsername2 = "TestUser2" + System.currentTimeMillis();

        databaseManager.addPlayer(testUsername1, 40);
        databaseManager.addPlayer(testUsername2, 40);

        databaseManager.updateCurrency(testUsername1, 60, "test");
        databaseManager.updateCurrency(testUsername2, 20, "test");

        List<Map.Entry<String, Integer>> leaderboard = databaseManager.getLeaderboard();
        assertFalse(leaderboard.isEmpty());
        assertTrue(leaderboard.get(0).getValue() >= leaderboard.get(1).getValue());
    }
}