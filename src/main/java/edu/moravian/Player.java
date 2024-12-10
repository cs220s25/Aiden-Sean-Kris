package edu.moravian;

import java.util.*;

public class Player {
    private final String name;
    private final List<String> hand = new ArrayList<>();
    private int balance;
    private int bet;
    private boolean hasStood; // New field to track if the player has stood

    public Player(String name) {
        this.name = name;
        this.balance = 1000000;// the max a player can bet
        this.bet = 0;
        this.hasStood = false; // Initially, the player has not stood
    }

    public void stand() {
        this.hasStood = true;
    }

    public boolean hasStood() {
        return hasStood;
    }

    public void resetStandStatus() {
        this.hasStood = false;
    }


    public String getName() {
        return name;
    }

    public List<String> getHand() {
        return hand;
    }

    public void resetHand() {
        hand.clear();
    }

    public void addCard(String card) {
        hand.add(card);
    }

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

    // Setter for bet
    public void setBet(int betAmount) {
        if (betAmount <= balance) { // Ensure the player has enough balance for the bet
            this.bet = betAmount;
        } else {
            throw new IllegalArgumentException("Insufficient balance for the bet.");
        }
    }

    // Getter for bet
    public int getBet() {
        return bet;
    }
}