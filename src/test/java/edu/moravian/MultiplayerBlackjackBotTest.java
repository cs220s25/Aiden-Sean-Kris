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

    // @Test
    // void testShuffleDeck() throws Exception {
    //     // Use reflection to access the private shuffleDeck method
    //     Method shuffleDeckMethod = MultiplayerBlackjackBot.class.getDeclaredMethod("shuffleDeck");
    //     shuffleDeckMethod.setAccessible(true);

    //     // Get the deck before shuffling
    //     Field getDeckMethod = MultiplayerBlackjackBot.class.getDeclaredField("deck");
    //     getDeckMethod.setAccessible(true);

    //     Stack<String> originalDeck = (Stack<String>) getDeckMethod.get(bot);
    //     Stack<String> originalDeckShuffled = (Stack<String>) originalDeck.clone();
    //     int originalSize = originalDeck.size();

    //     // Shuffle the deck
    //     shuffleDeckMethod.invoke(bot);

    //     // Check if the deck size remains the same
    //     assertEquals(originalSize, originalDeckShuffled.size());

    //     // Check that the deck is not in the original order (probability-based)
    //     boolean isDifferent = false;
    //     for (int i = 0; i < 5; i++) {
    //         if (!originalDeck.get(i).equals(originalDeckShuffled.get(i))) {
    //             isDifferent = true;
    //             break;
    //         }
    //     }
    //     assertTrue(isDifferent);
    // }
}
