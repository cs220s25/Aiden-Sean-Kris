/**
 * GameSession.java
 * this class is responsible for managing the game session, including player actions, dealer actions, and game state.
 * It handles the game logic, such as dealing cards, calculating hand values, and determining the outcome of the game.
 */
package edu.moravian;

import java.util.*;

public class GameSession {
    private final List<Player> players = new ArrayList<>();
    private final Player dealer = new Player("Dealer");
    private Stack<String> deck;
    private int currentPlayerIndex = 0;
    private boolean gameActive = false;
    private DatabaseManager databaseManager; // Add a reference to the DatabaseManager

    /**
     * Constructor to initialize the GameSession with a DatabaseManager.
     * @param databaseManager The DatabaseManager instance to manage player data.
     */
    public GameSession(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

/**
     * Starts a new game session with the given deck and starter player.
     * @param deck The stack of cards to be used in the game.
     * @param starterName The name of the player who starts the game.
     */

    public void startNewGame(Stack<String> deck, String starterName) {
        this.deck = deck;
        players.clear();
        players.add(new Player(starterName));
        dealer.resetHand();

        // Deal initial cards
        for (Player player : players) {
            player.addCard(deck.pop());
            player.addCard(deck.pop());
        }
        dealer.addCard(deck.pop());

        currentPlayerIndex = 0;
        gameActive = true;
    }
    /**
     * Adds a new player to the game session.
     * @param username The name of the player.
     * @param deck The stack of cards to be used in the game.
     * @return true if the player was added successfully, false otherwise.
     */

    public boolean addPlayer(String username, Stack<String> deck) {
        if (players.size() >= 8) return false; // Maximum of 8 players
        if (players.stream().anyMatch(player -> player.getName().equals(username))) return false;

        Player newPlayer = new Player(username);
        newPlayer.addCard(deck.pop());
        newPlayer.addCard(deck.pop());
        players.add(newPlayer);
        return true;
    }

    /**
     * Checks if the game is active.
     * @return true if the game is active, false otherwise.
     */

    public boolean isGameActive() {
        return gameActive;
    }
    /**
     * Checks if the player is the current player.
     * @param username The name of the player.
     * @return true if it's the player's turn, false otherwise.
     */


    public boolean isPlayerTurn(String username) {
        return players.get(currentPlayerIndex).getName().equals(username);
    }
    /**
     * gets the current player
     */

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    /**
     * Gets the current players name.
     * @return The current player.
     */

    public String getCurrentPlayerName() {
        return getCurrentPlayer().getName();
    }
    /**
     * Checks whose turn it is, if everyone is done, the dealer plays.
     */

    public void nextTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            gameActive = false;  // All players are done, dealer plays
        }
    }
    /**
     * The dealer plays their turn.
     */

    public void playDealer() {
        while (dealer.calculateHandValue() < 17) {
            // Dealer draws cards until hand value is 17 or higher
            dealer.addCard(deck.pop());
        }
    }
    /**
     * Gets the dealer's actions.
     * @return A list of strings representing the dealer's actions.
     */

    public List<String> getDealerActions() {
        List<String> actions = new ArrayList<>();
        for (String card : dealer.getHand()) {
            actions.add("*Dealer draws: *" + card);
        }
        actions.add("`Dealer's final hand value: " + dealer.calculateHandValue() + "`");
        return actions;
    }
    /**
     * Gets the dealer's final hand.
     * @return A string representing the dealer's final hand.
     */

    public String getDealerFinalHand() {
        return "**__Dealer's Final Hand:" + dealer.getHand()+ "__**";
    }
    /**
     * Gets the players' hands.
     * @return A string representing all players' hands.
     */

    public String getAllPlayerHands() {
        StringBuilder sb = new StringBuilder();
        for (Player player : players) {
            sb.append(player.getName()).append(": ").append(player.getHand()).append("\n");
        }
        return sb.toString();
    }
    /**
     * Gets the dealer's hand.
     * @return A string representing the dealer's hand.
     */

    public String getDealerHand() {
        return dealer.getHand().toString();
    }
    //if you deduct all the money from the player, the player will have should have a balance of 0,and
    // then you can remove the player from the game

/**
     * Calculates the outcomes for all players based on their hands and the dealer's hand.
     * @return A map of player names to their balance adjustments.
     */
    public Map<String, Integer> calculatePlayerOutcomes() {
        Map<String, Integer> balanceAdjustments = new HashMap<>();
        int dealerValue = dealer.calculateHandValue();

        for (Player player : players) {
            int playerValue = player.calculateHandValue();
            int bet = player.getBet();
            String playerName = player.getName();
            if (playerValue > 21 || (dealerValue <= 21 && dealerValue > playerValue)) {
                // Player loses
                balanceAdjustments.put(playerName, -bet); // Deduct bet
            }
            else if (playerValue == 21 && player.getHand().size() == 2) {
                    // Player has blackjack
                balanceAdjustments.put(playerName, (int) (bet * 1.5)); // 1.5x the bet
            } else if (playerValue == dealerValue) {
                // Tie, refund bet
                balanceAdjustments.put(playerName, 0);
            } else {
                // Player wins
                balanceAdjustments.put(playerName, bet * 1); // Double the bet
            }
        }

        return balanceAdjustments;
    }

/**
     * Sets the bet amount for a player.
     * @param username The name of the player.
     *  @param betAmount The amount to bet.
     */
    public void setbet(String username, int betAmount) {
        players.stream()
                .filter(player -> player.getName().equals(username))
                .findFirst()
                .ifPresent(player -> player.setBet(betAmount));
    }

    /**
     * Calculates the hand value for the current player.
     * @return The hand value of the current player.
     */

    public String getcalculatehandvalue() {
        Player player = getCurrentPlayer();
        return player.calculateHandValue() + "";
    }
    /**
     * Gets the dealer's hand value.
     * @return The dealer's hand value.
     */

    public String getDealerHandValue() {
        return dealer.calculateHandValue() + "";
    }

    /**
     * checks if the player has a bet
     * @param username
     * if not return false
     * if yes return true
     */

    public boolean hasPlayerBet(String username) {
        return players.stream()
                .filter(player -> player.getName().equals(username))
                .anyMatch(player -> player.getBet() > 0);
    }

    /**
     * Removes a player from the game session.
     * @param username
     * @return
     */

    public boolean removePlayer(String username) {
        return players.removeIf(player -> player.getName().equals(username));
    }
}
