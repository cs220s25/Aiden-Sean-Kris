## ActionsExploration

### CI Status

![Testing](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/maven-build.yml/badge.svg)

## Project Description

Gojo's Ultimate Blackjack Arena 
Project Description:

Gojo's Ultimate Blackjack Arena is a multiplayer Blackjack Discord bot that has all the same functionalities of a regular blackjack game. Inspired by the charismatic Jujutsu Kaisen character Gojo Satoru, this bot offers an engaging, interactive Blackjack experience with unique features:

Multiplayer Gameplay: Up to 8 players can join a single game session

Persistent Coin Economy: Players start with 40 coins and can win or lose based on their Blackjack skills

Dynamic Betting System: Place bets, track your balance, and compete with friends

Personalized Game Interactions: Playful Gojo-themed messages add humor to the gaming experience

## System Diagram
![DiscordUML.png](src/main/java/DiscordUML.png)

The system diagram illustrates the key components of the Blackjack bot:

Discord Message Handler: Processes user commands and coordinates game flow

Game Session: Manages the state of an individual game

Database Manager: Handles player data and currency tracking

Player: Represents individual player state and actions

DiscordMessageManager: Handles card drawing and shuffling while also initializing the bot


## Usage
Usage
Getting Started

Type gojo in the designated Discord channel to see available commands
Use !play to start a new game
Invite friends to join with !join

Key Commands

!play: Start a new game
!join: Join an existing game
!bet [amount]: Place a bet (e.g., !bet 10)
!hit: Draw another card
!stand: Keep current hand
!balance: Check your coin balance

Game Mechanics

Start with 40 coins>
Bet before playing
Play against the dealer
Win or lose coins based on your hand
Blackjack pays 1.5x the bet
Bust (over 21) results in losing your bet

