/**
 * DiscordMessageHandler.java
 * this class handles the messages received from the Discord bot
 * it processes the commands and interacts with the game session
 * and the database.
 */
package edu.moravian;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Map;
import java.util.Stack;


public class DiscordMessageHandler {
    private final DatabaseManager databaseManager;
    private final Map<String, GameSession> games;
    private final Stack<String> deck;

    /**
     * Constructor for DiscordMessageHandler
     * this constructor initializes the database manager, games map and deck
     * @param databaseManager
     * @param games
     * @param deck
     */
    public DiscordMessageHandler(DatabaseManager databaseManager, Map<String, GameSession> games, Stack<String> deck) {
        this.databaseManager = databaseManager;
        this.games = games;
        this.deck = deck;
    }
    /**
     * This method handles the commands received from the Discord bot
     * it processes the commands and interacts with the game session
     * and the database.
     * @param event
     */

    public void handleCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw().toLowerCase();
        String channelId = event.getChannel().getId();
        String username = event.getAuthor().getName();

        switch (message) {
            case "!play","p" -> startGame(channelId, username, event);
            case "!join","j" -> joinGame(channelId, username, event);
            case "!hit","h" -> hit(channelId, username, event);
            case "!stand","s" -> stand(channelId, username, event);
            case "!balance", "!bal", "!coins" -> checkBalance(username, event);
            case "!double" -> event.getChannel().sendMessage("Double down is not yet implemented that `Aiden guy is working on it`.").queue();
            case "!leave" -> leaveGame(channelId, username, event);
            case "!give"-> event.getChannel().sendMessage("Give is not yet implemented that `Aiden guy is working on it`.").queue();
            case "!leaderboard", "!lb" -> showLeaderboard(event);
        }

        if (message.startsWith("!bet")) {
            handleBet(channelId, username, event, message);
        }

        if (message.equalsIgnoreCase("gojo")) {
            event.getChannel().sendMessage("https://tenor.com/view/welcome-gif-24588321").queue();
            event.getChannel().sendMessage(
                    "**♤ ♡ ♧ ♢Welcome to Gojo’s Ultimate Blackjack Arena!♤ ♡ ♧ ♢**\n" +
                            "I am Gojo Satoru, the strongest sorcerer in the world! And I’m here to challenge you to a game of Blackjack. \n" +
                            "1. `!play`: Start a new game.\n" +
                            "2. `!join`: Join the game.\n" +
                            "3. `!hit`: Draw a card.\n" +
                            "4. `!stand`: Stand.\n" +
                            "5. `!bet [amount]`: Place a bet.\n" +
                            "6. `!balance`: Check your balance.\n"+
                            "7. `!leaderboard`: View the top players.\n"
            ).queue();
        }
    }
    /**
     * This method handles the leave command
     * it removes the player from the game session
     * @param channelId
     * @param username
     * @param event
     */


    private void leaveGame(String channelId, String username, MessageReceivedEvent event) {
        GameSession game = games.get(channelId);
        if (game == null || !game.isGameActive()) {
            event.getChannel().sendMessage("No active game in this channel. Start one with `!play`.").queue();
            return;
        }

        if (game.removePlayer(username)) {
            event.getChannel().sendMessage("You have left the game.").queue();
        } else {
            event.getChannel().sendMessage("You are not in the game.").queue();
        }
    }
    /**
     * This method checks the balance of the player
     * it retrieves the balance from the database and sends it to the player
     * @param username
     * @param event
     */

    private void checkBalance(String username, MessageReceivedEvent event) {
        try {
            int balance = databaseManager.getPlayerBalance(username);
            event.getChannel().sendMessage(username + ", you’ve got ⛁**" + balance + "** coins. Enough to make it rain? Or just enough to get burned? Your call!"
            ).queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("Error retrieving your balance. Please try again later.").queue();
            e.printStackTrace();
        }
    }
    /**
     * This method starts a new game session
     * it adds the player to the database if not already added
     * and starts a new game session
     * @param channelId
     * @param username
     * @param event
     */

    private void startGame(String channelId, String username, MessageReceivedEvent event) {
        // Add player to the database if not already added
        if (!databaseManager.playerExists(username)) {
            databaseManager.addPlayer(username, 40); // Add with an initial balance of 40 coins
            event.getChannel().sendMessage(username + ", you have been added to the game with 40 coins.").queue();
        }

        // Start a new game and prompt for a bet
        GameSession game = games.computeIfAbsent(channelId, k -> new GameSession(databaseManager));
        if (game.isGameActive()) {
            event.getChannel().sendMessage("A game is already active in this channel!").queue();
        } else {
            game.startNewGame(deck, username);
            event.getChannel().sendMessage("Oh-ho, looks like someone wants to play Blackjack with the best—me, Gojo Satoru! \n"+"Proceed with the game to play solo or wait for others to `!join` to play along with others\n" +
                    username + ", you're in! Don't forget to bet (`!bet [amount]`) if you wanna test your luck against the *strongest* dealer.").queue();
        }
    }
    /**
     * This method handles the join command
     * it adds the player to the game session
     * @param channelId
     * @param username
     * @param event
     */

    private void joinGame(String channelId, String username, MessageReceivedEvent event) {
        if (!databaseManager.playerExists(username)) {
            databaseManager.addPlayer(username, 40); // Add with an initial balance of 40 coins
            event.getChannel().sendMessage(username + ", you have been added to the game with 40 coins.").queue();
        }

        GameSession game = games.get(channelId);
        if (game == null || !game.isGameActive()) {
            event.getChannel().sendMessage("No active game in this channel. Start one with `!play`.").queue();
            return;
        }

        if (game.addPlayer(username, deck)) {
            event.getChannel().sendMessage("Yo, " + username + ", welcome to the game! \n" +
                    "Let’s see if you’ve got what it takes to stand up to my unbeatable dealer skills. Place your bet with `!bet [amount]`, and let's get rolling!").queue();
            event.getChannel().sendMessageEmbeds(buildEmbed(username + " received two cards!", game)).queue();
        } else {
            event.getChannel().sendMessage("You are already in the game or the game is full.").queue();
        }
    }
    /**
     * This method handles the hit command
     * it adds a card to the player's hand
     * @param channelId
     * @param username
     * @param event
     */

    private void hit(String channelId, String username, MessageReceivedEvent event) {
        GameSession game = games.get(channelId);
        if (game == null || !game.isGameActive()) {
            event.getChannel().sendMessage("No active game in this channel. Start one with `!play`.").queue();
            return;
        }

        if (!game.hasPlayerBet(username)) {
            event.getChannel().sendMessage(username + ", you must place a bet with `!bet [amount]` before making any moves.").queue();
            return;
        }


        if (!game.isPlayerTurn(username)) {
            event.getChannel().sendMessage("It's not your turn, its currently **" + game.getCurrentPlayerName() + "'s** turn").queue();
            return;
        }

        Player player = game.getCurrentPlayer();
        player.addCard(deck.pop());

        if (player.calculateHandValue() > 21) {
            event.getChannel().sendMessageEmbeds(buildEmbed(  username + ", you busted! Tough luck, but hey, at least you tried. Next time, maybe bring some six eyes of your own?", game, 0xFF0000)).queue();
            game.nextTurn();
            if (!game.isGameActive()) {
                finishGame(channelId, event);
            }
        } else {
            event.getChannel().sendMessageEmbeds(buildEmbed(username + " drew a card.", game)).queue();
        }
    }
    /**
     * This method handles the stand command
     * it ends the player's turn and moves to the next player
     * @param channelId
     * @param username
     * @param event
     */

    private void stand(String channelId, String username, MessageReceivedEvent event) {
        GameSession game = games.get(channelId);
        if (game == null || !game.isGameActive()) {
            event.getChannel().sendMessage("Huh? No game here! Looks like you need to start one first with `!play`. Don’t keep *me* waiting!"
            ).queue();
            return;
        }

        if (!game.hasPlayerBet(username)) {
            event.getChannel().sendMessage(username + ", you must place a bet with `!bet [amount]` before making any moves.").queue();
            return;
        }

        if (!game.isPlayerTurn(username)) {
            event.getChannel().sendMessage("It's not your turn, its currently " + game.getCurrentPlayerName() + "'s turn").queue();
            return;
        }

        if (!game.isPlayerTurn(username)) {
            event.getChannel().sendMessage("It's not your turn, its currently " + game.getCurrentPlayerName() + "'s turn").queue();
            return;
        }

        event.getChannel().sendMessageEmbeds(buildEmbed("Alright, " + username + ", you’ve made your move. Bold choice to stand—let’s see if it pays off against *my* dealer hand.", game)).queue();
        game.nextTurn();
        if (!game.isGameActive()) {
            finishGame(channelId, event);
        }
    }
    /**
     * This method handles the finish game command
     * it plays the dealer's turn and calculates the outcomes
     * @param channelId
     * @param event
     */


    private void finishGame(String channelId, MessageReceivedEvent event) {
        GameSession game = games.get(channelId);
        if (game == null) return;

        game.playDealer();
        event.getChannel().sendMessage("Dealer begins to draw cards...").queue();

        for (String action : game.getDealerActions()) {
            event.getChannel().sendMessage(action).queue();
        }

        event.getChannel().sendMessage(game.getDealerFinalHand()).queue();

        Map<String, Integer> outcomes = game.calculatePlayerOutcomes();

        for (Map.Entry<String, Integer> entry : outcomes.entrySet()) {
            String playerName = entry.getKey();
            int adjustment = entry.getValue();
            int newBalance = databaseManager.updateCurrency(playerName, adjustment, "adjustment");

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Game Results for " + playerName);
            if (adjustment > 0) {
                embed.setDescription("You won! 🏆 New balance: ⛁**" + newBalance + "**").setColor(0x00FF00);
            } else if (adjustment < 0) {
                embed.setDescription("You lost. ❌ New balance: ⛁**" + newBalance + "**").setColor(0xFF0000);
            } else {
                embed.setDescription("It’s a tie. 🤝 Your balance remains: ⛁**" + newBalance + "**").setColor(0xFFFF00);
            }

            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        }

        games.remove(channelId);
    }
    /**
     * This method builds the embed message for the game session
     * @param title
     * @param game
     * @return
     */

    private MessageEmbed buildEmbed(String title, GameSession game) {
        return buildEmbed(title, game, 0x000000);  // Default to black color
    }
    /**
     * This method builds the embed message for the game session
     * @param title
     * @param game
     * @param color
     * @return
     */

    private MessageEmbed buildEmbed(String title, GameSession game, int color) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.addField("Current Player", game.getCurrentPlayerName(), false);
        embed.addField("Current Player's Hand calculation", game.getcalculatehandvalue(), false);
        embed.addField("Player Hands", game.getAllPlayerHands(), false);
        embed.addField("Dealer's Hand", game.getDealerHand(), false);
        embed.addField("Dealer's Hand calculation", game.getDealerHandValue(), false);
        embed.setColor(color);
        return embed.build();
    }
    /**
     * This method shows the leaderboard
     * it retrieves the leaderboard from the database and sends it to the player
     * @param event
     */
    private void showLeaderboard(MessageReceivedEvent event) {
        List<Map.Entry<String, Integer>> leaderboard = databaseManager.getLeaderboard();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("🌟 **GOJO'S ULTIMATE COIN RANKINGS** 🌟");
        embed.setDescription("Witness the hierarchy of power... in coins! 😎");
        embed.setColor(0x6666FF); // Gojo's blue color

        StringBuilder leaderboardText = new StringBuilder();
        for (int i = 0; i < Math.min(leaderboard.size(), 10); i++) {
            Map.Entry<String, Integer> entry = leaderboard.get(i);
            String rank = switch (i) {
                case 0 -> "👑 ABSOLUTE TOP DOG";
                case 1 -> "🥈 ALMOST THE STRONGEST";
                case 2 -> "🥉 BARELY KEEPING UP";
                default -> (i + 1) + "th PLACE";
            };

            String gojoCritic = switch (i) {
                case 0 -> "Heh, not surprised. Absolute domain control!";
                case 1 -> "Not bad... but still no match for me.";
                case 2 -> "Struggling to keep up with the big leagues, eh?";
                default -> "Just another player in my infinite domain.";
            };

            leaderboardText.append(String.format("**%s**: ⛁ %d coins\n*%s*\n*%s*\n\n",
                    entry.getKey(), entry.getValue(), rank, gojoCritic));
        }

        embed.setDescription(leaderboardText.toString());

        // Add a Gojo-style footer
        embed.setFooter("Rankings determined by the strongest sorcerer... me! 😏", null);

        event.getChannel().sendMessage("**Behold! The coin hierarchy of my domain!**").queue();
        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
    /**
     * This method handles the bet command
     * it sets the bet amount for the player
     * @param channelId
     * @param username
     * @param event
     * @param message
     */

    private void handleBet(String channelId, String username, MessageReceivedEvent event, String message) {
        // Extract bet amount from the message (example: "!bet 10")
        String[] parts = message.split(" ");
        if (parts.length != 2) {
            event.getChannel().sendMessage("Please specify a valid bet amount. Example: `!bet 10`").queue();
            return;
        }

        try {
            int betAmount = Integer.parseInt(parts[1]);
            if (betAmount <= 0) {
                event.getChannel().sendMessage("Bet amount must be greater than zero.").queue();
                return;
            }

            // Retrieve player's current balance from MySQL
            int balance = databaseManager.getPlayerBalance(username);

            if (betAmount > balance) {
                event.getChannel().sendMessage("You do not have enough coins to place this bet.").queue();
                return;
            }

            // Proceed with betting logic
            GameSession game = games.get(channelId);
            if (game != null && game.isGameActive()) {
                game.setbet(username, betAmount);  // Set bet in game session
                // Update balance in database after the bet






                // Send confirmation of the bet
                event.getChannel().sendMessage(username + ", betting **" + betAmount + "** coins? Bold move! Hope you've got more than just luck on your side. Let’s see how this plays out!"
                ).queue();

                // Display the player's cards using an embed
                Player player = game.getCurrentPlayer();
                if (player != null && player.getName().equals(username)) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle(username+"'s hand " + player.getHand());
                    embed.setDescription("Your cards: " + player.getHand());
                    embed.addField("Your Hand calculation", player.calculateHandValue() + "", false);
                    embed.setColor(900090);
                    embed.setDescription("Dealer's Hand: " + game.getDealerHand());
                    embed.addField("Dealer's Hand calculation", game.getDealerHandValue(), false);
                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                }

            } else {
                event.getChannel().sendMessage("No active game found. Please start a game with `!play`.").queue();
            }

        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Invalid bet amount. Please use a valid number.").queue();
        }
    }
}
