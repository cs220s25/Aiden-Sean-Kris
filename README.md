## ActionsExploration

### CI Status

[![Testing](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/maven-build.yml/badge.svg)](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/maven-build.yml)


## Project Description

Gojo's Ultimate Blackjack Arena 
Project Description:

Gojo's Ultimate Blackjack Arena is a multiplayer Blackjack Discord bot that has all the same functionalities of a regular blackjack game. Inspired by the charismatic Jujutsu Kaisen character Gojo Satoru, this bot offers an engaging, interactive Blackjack experience with unique features:

-Multiplayer Gameplay: Up to 8 players can join a single game session

-Persistent Coin Economy: Players start with 40 coins and can win or lose based on their Blackjack skills

-Dynamic Betting System: Place bets, track your balance, and compete with friends

-Personalized Game Interactions: Playful Gojo-themed messages add humor to the gaming experience

## Manual Setup

Create EC2 Instance and SSH into it
- ```ssh -i /path/to/labuser.pem ec2-user@your-ec2-public-ip```


Install system dependencies
- ```sudo yum install -y git```
- ```sudo yum install -y maven-amazon-corretto21```
- ```sudo dnf install -y mariadb105 mariadb105-server expect```

Start and enable MariaDB
- ```sudo systemctl start mariadb```
- ```sudo systemctl enable mariadb```
- ```sudo mysql_secure_installation```

1. When it prompts you asking for a password press enter
2. Press & enter “y” for everything else it asks you
3. When it asks you to put in a new password type in desired passsword.


Clone project in the instance
- ```git clone https://github.com/cs220s25/Aiden-Sean-Kris.git```

Change Directory 
- ```cd Aiden-Sean-Kris```

## Create the .env file with your Discord token
- ```nano .env``` 
- ```Type in “DISCORD_TOKEN=YOUR_DISCORD_TOKEN_HERE”```

Build the project
- ```enter the following command into the terminal: mvn clean package```

Install and start the systemd service
- ```sudo cp Blackjackbot.service /etc/systemd/system```
- ```sudo systemctl daemon-reload```
- ```sudo systemctl enable Blackjackbot.service```
- ```sudo systemctl start Blackjackbot.service```


## Usage
Getting Started

Type gojo in the designated Discord channel to see available commands  
Use !play to start a new game  
Invite friends to join with !join  

## Key Commands

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

## System Diagram
![DiscordUML.png](src/main/java/DiscordUML.png)

The system diagram illustrates the key components of the Blackjack bot:  

Discord Message Handler: Processes user commands and coordinates game flow  

Game Session: Manages the state of an individual game  

Database Manager: Handles player data and currency tracking  

Player: Represents individual player state and actions  

DiscordMessageManager: Handles card drawing and shuffling while also initializing the bot  

