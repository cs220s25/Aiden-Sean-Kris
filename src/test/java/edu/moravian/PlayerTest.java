package edu.moravian;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer");
    }

    @Test
    void testStand() {
        assertFalse(player.hasStood());
        player.stand();
        assertTrue(player.hasStood());
    }

    @Test
    void testResetStandStatus() {
        player.stand();
        assertTrue(player.hasStood());
        player.resetStandStatus();
        assertFalse(player.hasStood());
    }

    @Test
    void testGetName() {
        assertEquals("TestPlayer", player.getName());
    }

    @Test
    void testGetHand() {
        assertTrue(player.getHand().isEmpty());
    }
    @Test
    public void testPlayerInitialization() {
        Player player = new Player("TestPlayer");
        assertEquals("TestPlayer", player.getName());
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    public void testAddCard() {
        Player player = new Player("TestPlayer");
        player.addCard("A♠");
        assertEquals(1, player.getHand().size());
        assertTrue(player.getHand().contains("A♠"));
    }

    @Test
    public void testCalculateHandValue() {
        Player player = new Player("TestPlayer");
        player.addCard("A♠");
        player.addCard("K♥");
        assertEquals(21, player.calculateHandValue());

        player.resetHand();
        player.addCard("10♦");
        player.addCard("7♣");
        player.addCard("4♥");
        assertEquals(21, player.calculateHandValue());
    }

    @Test
    public void testSetBet() {
        Player player = new Player("TestPlayer");
        player.setBet(100);
        assertEquals(100, player.getBet());
    }


    @Test
    public void testStandStatus() {
        Player player = new Player("TestPlayer");
        assertFalse(player.hasStood());
        player.stand();
        assertTrue(player.hasStood());
        player.resetStandStatus();
        assertFalse(player.hasStood());
    }
}