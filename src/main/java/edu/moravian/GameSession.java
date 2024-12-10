package edu.moravian;

import java.util.*;

public class GameSession {
    private final List<Player> players = new ArrayList<>();
    private final Player dealer = new Player("Dealer");
    private Stack<String> deck;
    private int currentPlayerIndex = 0;
    private boolean gameActive = false;
    private DatabaseManager databaseManager; // Add a reference to the DatabaseManager

    public GameSession(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }



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

    public boolean addPlayer(String username, Stack<String> deck) {
        if (players.size() >= 8) return false; // Maximum of 8 players
        if (players.stream().anyMatch(player -> player.getName().equals(username))) return false;

        Player newPlayer = new Player(username);
        newPlayer.addCard(deck.pop());
        newPlayer.addCard(deck.pop());
        players.add(newPlayer);
        return true;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public boolean isPlayerTurn(String username) {
        return players.get(currentPlayerIndex).getName().equals(username);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public String getCurrentPlayerName() {
        return getCurrentPlayer().getName();
    }

    public void nextTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            gameActive = false;  // All players are done, dealer plays
        }
    }

    public void playDealer() {
        while (dealer.calculateHandValue() < 17) {
            // Dealer draws cards until hand value is 17 or higher
            dealer.addCard(deck.pop());
        }
    }

    public List<String> getDealerActions() {
        List<String> actions = new ArrayList<>();
        for (String card : dealer.getHand()) {
            actions.add("*Dealer draws: *" + card);
        }
        actions.add("`Dealer's final hand value: " + dealer.calculateHandValue() + "`");
        return actions;
    }

    public String getDealerFinalHand() {
        return "**__Dealer's Final Hand:" + dealer.getHand()+ "__**";
    }



    public String getAllPlayerHands() {
        StringBuilder sb = new StringBuilder();
        for (Player player : players) {
            sb.append(player.getName()).append(": ").append(player.getHand()).append("\n");
        }
        return sb.toString();
    }

    public String getDealerHand() {
        return dealer.getHand().toString();
    }
    //if you deduct all the money from the player, the player will have should have a balance of 0,and
    // then you can remove the player from the game


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

    public void setbet(String username, int betAmount) {
        players.stream()
                .filter(player -> player.getName().equals(username))
                .findFirst()
                .ifPresent(player -> player.setBet(betAmount));
    }

    public String getcalculatehandvalue() {
        Player player = getCurrentPlayer();
        return player.calculateHandValue() + "";
    }

    public String getDealerHandValue() {
        return dealer.calculateHandValue() + "";
    }

    public boolean hasPlayerBet(String username) {
        return players.stream()
                .filter(player -> player.getName().equals(username))
                .anyMatch(player -> player.getBet() > 0);
    }

    public boolean removePlayer(String username) {
        return players.removeIf(player -> player.getName().equals(username));
    }
}
