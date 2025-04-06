/**
 * Player class representing a player in a card game.
 * It manages the player's hand, balance, and betting.
 */
package edu.moravian;

import java.util.*;

public class Player {
    private final String name;
    private final List<String> hand = new ArrayList<>();
    private int balance;
    private int bet;
    private boolean hasStood; // New field to track if the player has stood

    /**
     * Constructor to initialize the player with a name.
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.balance = 1000000;// the max a player can bet
        this.bet = 0;
        this.hasStood = false; // Initially, the player has not stood
    }

    /**
     * Method is see if the player chose to stand.
     */

    public void stand() {
        this.hasStood = true;
    }
    /**
     * Method to check if the player has chosen to stand.
     * @return true if the player has stood, false otherwise.
     */

    public boolean hasStood() {
        return hasStood;
    }

    /**
     * method to check if the player has not chose to stand.
     * @return true if the player has not stood, false otherwise.
     */

    public void resetStandStatus() {
        this.hasStood = false;
    }

    /**
     * method to get the players name.
     * @return
     */

    public String getName() {
        return name;
    }

    /**
     * method to get the players hand
     * @return
     */

    public List<String> getHand() {
        return hand;
    }

    /**
     * method to reset the players hand
     */

    public void resetHand() {
        hand.clear();
    }

    /**
     * this method adds a card to the players hand (if they chose hit for example)
     * @param card
     */

    public void addCard(String card) {
        hand.add(card);
    }
    /** this method calculates the value of the players hand including aces and face cards
     * @return the value of the players hand
     */

    public int calculateHandValue() {
        int value = 0;
        int aceCount = 0;
        for (String card : hand) {
            String cardValue = card.substring(0, card.length() - 1); // Get card rank
            if (cardValue.equals("A")) {
                aceCount++;
                value += 11;
            } else if (cardValue.equals("K") || cardValue.equals("Q") || cardValue.equals("J")) {
                value += 10;
            } else {
                value += Integer.parseInt(cardValue);
            }
        }

        // Adjust for aces
        while (value > 21 && aceCount > 0) {
            value -= 10; // Change Ace from 11 to 1
            aceCount--;
        }
        return value;
    }

    /**
     * this method allows the player to set a bet amount.
     * @param betAmount
     */

    // Setter for bet
    public void setBet(int betAmount) {
        if (betAmount <= balance) { // Ensure the player has enough balance for the bet
            this.bet = betAmount;
        } else {
            throw new IllegalArgumentException("Insufficient balance for the bet.");
        }
    }
    /**
     * this method allows the player to get their bet amount.
     */

    // Getter for bet
    public int getBet() {
        return bet;
    }
}