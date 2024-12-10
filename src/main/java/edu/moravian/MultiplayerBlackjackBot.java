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

    public MultiplayerBlackjackBot() {
        DatabaseManager databaseManager = new DatabaseManager();
        messageHandler = new DiscordMessageHandler(databaseManager, games, deck);

        JDA api = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        api.addEventListener(this);

        shuffleDeck();
    }

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

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        messageHandler.handleCommand(event);
    }

    public static void main(String[] args) {
        new MultiplayerBlackjackBot();
    }
}