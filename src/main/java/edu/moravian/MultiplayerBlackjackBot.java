/**
 * MultiplayerBlackjackBot.java
 * this is the main class for the Multiplayer Blackjack Discord bot.
 * It initializes the bot, sets up the game sessions, and handles incoming messages.
 */
package edu.moravian;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MultiplayerBlackjackBot extends ListenerAdapter {

    private final Dotenv dotenv = Dotenv.load();
    private final String token = dotenv.get("DISCORD_TOKEN");
    private final Map<String, GameSession> games = new HashMap<>();
    private final Stack<String> deck = new Stack<>();
    private final DiscordMessageHandler messageHandler;

    /**
     * Constructor to initialize the bot and set up the game sessions.
     */
    public MultiplayerBlackjackBot() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.createDatabaseIfNotExists();
        databaseManager.createdumpfile();
        databaseManager.importDumpFile();
        databaseManager.createdumpfile();
        messageHandler = new DiscordMessageHandler(databaseManager, games, deck);

        JDA api = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        api.addEventListener(this);

        shuffleDeck();

        // Add this to your main class constructor or initialization method
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Application shutting down, updating database dump...");
            databaseManager.updatedumpfile();
            databaseManager.close();
        }));
    }
    /**
     * Shuffles the deck of cards for the game.
     */

    private void shuffleDeck() {
        String[] suits = {"♠", "♦", "♣", "♥"};
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (String suit : suits) {
            for (String value : values) {
                deck.add(value + suit);
            }
        }
        Collections.shuffle(deck);
    }
    /**
     * Handles incoming messages and delegates them to the message handler.
     * @param event The message received event.
     */

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        messageHandler.handleCommand(event);
    }
    /**
     * Main method to start the bot.
     * @param args Command line arguments.
     */

    public static void main(String[] args) {
        new MultiplayerBlackjackBot();
    }
}