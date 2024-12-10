package edu.moravian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Stack;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {
    private GameSession gameSession;
    private Stack<String> testDeck;
    private DatabaseManager mockDatabaseManager;

    @BeforeEach
    void setUp() {
        mockDatabaseManager = new DatabaseManager();
        gameSession = new GameSession(mockDatabaseManager);

        // Create a test deck
        testDeck = new Stack<>();
        testDeck.push("A♠");
        testDeck.push("K♥");
        testDeck.push("Q♦");
        testDeck.push("J♣");
        testDeck.push("10♥");
    }

    @Test
    void testStartNewGame() {
        gameSession.startNewGame(testDeck, "TestPlayer");
        assertTrue(gameSession.isGameActive());
        assertEquals("TestPlayer", gameSession.getCurrentPlayerName());
    }

    @Test
    void testAddPlayer() {
        gameSession.startNewGame(testDeck, "FirstPlayer");
        assertTrue(gameSession.addPlayer("SecondPlayer", testDeck));
        assertFalse(gameSession.addPlayer("FirstPlayer", testDeck)); // Can't add same player twice
    }

    @Test
    void testIsPlayerTurn() {
        gameSession.startNewGame(testDeck, "TestPlayer");
        assertTrue(gameSession.isPlayerTurn("TestPlayer"));
    }

    @Test
    void testNextTurn() {
        gameSession.startNewGame(testDeck, "FirstPlayer");
        gameSession.addPlayer("SecondPlayer", testDeck);
        gameSession.nextTurn();
        assertEquals("SecondPlayer", gameSession.getCurrentPlayerName());
    }

    @Test
    void testPlayDealer() {
        gameSession.startNewGame(testDeck, "TestPlayer");
        gameSession.playDealer();
        assertTrue(gameSession.isGameActive());
    }

    @Test
    void testSetBetAndHasBet() {
        gameSession.startNewGame(testDeck, "TestPlayer");
        gameSession.setbet("TestPlayer", 50);
        assertTrue(gameSession.hasPlayerBet("TestPlayer"));
    }

    @Test
    void testCalculatePlayerOutcomes() {
        gameSession.startNewGame(testDeck, "TestPlayer");
        gameSession.setbet("TestPlayer", 50);
        gameSession.playDealer();

        Map<String, Integer> outcomes = gameSession.calculatePlayerOutcomes();
        assertNotNull(outcomes);
        assertTrue(outcomes.containsKey("TestPlayer"));
    }

    @Test
    void testRemovePlayer() {
        gameSession.startNewGame(testDeck, "TestPlayer");
        gameSession.addPlayer("SecondPlayer", testDeck);
        assertTrue(gameSession.removePlayer("SecondPlayer"));
        assertFalse(gameSession.removePlayer("NonExistentPlayer"));
    }

}