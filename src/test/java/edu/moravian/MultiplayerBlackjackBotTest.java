package edu.moravian;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Stack;

public class MultiplayerBlackjackBotTest {
    private MultiplayerBlackjackBot bot;

    @BeforeEach
    void setUp() {
        bot = new MultiplayerBlackjackBot();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testShuffleDeck() throws Exception {
        // Use reflection to access the private shuffleDeck method
        Method shuffleDeckMethod = MultiplayerBlackjackBot.class.getDeclaredMethod("shuffleDeck");
        shuffleDeckMethod.trySetAccessible();

        // Access the private deck field
        Field deckField = MultiplayerBlackjackBot.class.getDeclaredField("deck");
        deckField.trySetAccessible();

        // Get the original deck
        Stack<String> originalDeck = (Stack<String>) deckField.get(bot);
        Stack<String> originalDeckCopy = (Stack<String>) originalDeck.clone();

        // Shuffle the deck
        shuffleDeckMethod.invoke(bot);

        // Verify the deck order has changed (probabilistic check)
        boolean isDifferent = false;
        for (int i = 0; i < originalDeck.size(); i++) {
            if (!originalDeck.get(i).equals(originalDeckCopy.get(i))) {
                isDifferent = true;
                break;
            }
        }
        assertTrue(isDifferent, "The deck order should be different after shuffling.");
    }
}
