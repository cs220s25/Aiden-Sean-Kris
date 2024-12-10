package edu.moravian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Stack;

public class DiscordMessageHandlerTest {
    private DatabaseManager databaseManager;
    private HashMap<String, GameSession> games;
    private Stack<String> deck;
    private DiscordMessageHandler messageHandler;

    @BeforeEach
    void setUp() {
        // Use an in-memory database or mock for testing
        databaseManager = new DatabaseManager();

        // Explicitly type the HashMap and initialize it
        games = new HashMap<>();

        // Create a deck with known cards
        deck = new Stack<>();
        deck.push("A♠");
        deck.push("K♥");
        deck.push("Q♦");
        deck.push("10♣");
        deck.push("9♥");
        deck.push("8♦");

        // Initialize messageHandler with the test setup
        messageHandler = new DiscordMessageHandler(databaseManager, games, deck);
    }

    @Test
    void testGameStartAndJoin() {
        // Generate a unique username for this test
        String username = "TestUser" + System.currentTimeMillis();

        // Ensure the player is added to the database
        databaseManager.addPlayer(username, 40);

        // Create a new GameSession directly
        GameSession game = new GameSession(databaseManager);
        game.startNewGame(deck, username);

        // Verify game state
        assertTrue(game.isGameActive(), "Game should be active after starting");
        assertEquals(username, game.getCurrentPlayerName(), "Current player should be the game starter");

        // Verify initial hand
        assertEquals(2, game.getCurrentPlayer().getHand().size(),
                "Player should start with 2 cards");
    }

    @Test
    void testGameAddPlayer() {
        // Generate unique usernames for this test
        String firstPlayer = "FirstPlayer" + System.currentTimeMillis();
        String secondPlayer = "SecondPlayer" + System.currentTimeMillis();

        // Add players to the database
        databaseManager.addPlayer(firstPlayer, 40);
        databaseManager.addPlayer(secondPlayer, 40);

        // Create and start a game with the first player
        GameSession game = new GameSession(databaseManager);
        game.startNewGame(deck, firstPlayer);

        // Try to add a second player
        boolean playerAdded = game.addPlayer(secondPlayer, deck);

        // Verify player addition
        assertTrue(playerAdded, "Second player should be able to join the game");

        // Additional checks
        assertTrue(game.isGameActive(), "Game should remain active after adding a player");
        assertEquals(2, game.getCurrentPlayer().getHand().size(),
                "Players should each have 2 cards after joining");
    }

    @Test
    void testGamePlayerLimit() {
        GameSession game = new GameSession(databaseManager);
        String firstPlayer = "Player1" + System.currentTimeMillis();
        databaseManager.addPlayer(firstPlayer, 40);
        game.startNewGame(deck, firstPlayer);

        // Add 7 more players to test the limit
        for (int i = 2; i <= 8; i++) {
            String playerName = "Player" + i + System.currentTimeMillis();
            databaseManager.addPlayer(playerName, 40);
            assertTrue(game.addPlayer(playerName, deck),
                    "Player " + i + " should be able to join");
        }

        // Try to add a 9th player
        String ninthPlayer = "Player9" + System.currentTimeMillis();
        databaseManager.addPlayer(ninthPlayer, 40);
        assertFalse(game.addPlayer(ninthPlayer, deck),
                "9th player should not be able to join");
    }
}