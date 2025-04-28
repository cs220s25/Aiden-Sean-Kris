# 🎲 Gojo's Ultimate Blackjack Arena 🎲
[![License: AFL-3.0](https://img.shields.io/badge/License-AFL%203.0-purple.svg)](https://opensource.org/licenses/AFL-3.0)
## 📋 Table of Contents
- [CI/CD Status](#-cicd-status)
- [Project Overview](#-project-overview)
- [Setup Instructions](#️-setup-instructions)
- [How to Play](#-how-to-play)
- [Game Mechanics](#-game-mechanics)
- [System Architecture](#-system-architecture)

## 📈 CI/CD Status

| Workflow | Status |
|:---|:---|
| Maven Build | [![Testing](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/maven-build.yml/badge.svg)](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/maven-build.yml) |
| Deploy | [![Deploy](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/deploy.yml/badge.svg)](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/deploy.yml) |
| Style Checker | [![Style Checker](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/style-checker.yml/badge.svg)](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/style-checker.yml) |
| Docker Deployment (AWS) | [![Deploy Docker on AWS](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/dockerEC2Deploy.yml/badge.svg)](https://github.com/cs220s25/Aiden-Sean-Kris/actions/workflows/dockerEC2Deploy.yml) |

---

## 🎮 Project Overview

**Gojo's Ultimate Blackjack Arena** brings the excitement of Blackjack into Discord servers, enhanced with Gojo Satoru’s flair!

### ✨ Features
- **Multiplayer**: Up to 8 players.
- **Persistent Economy**: Start with 40 coins and build your fortune.
- **Dynamic Betting**: Bet smart and dominate.
- **Gojo-themed Messages**: Hilarious and stylish interactions.

---

## ⚙️ ️Setup Instructions

### 🚀 EC2 Deployment Steps
1. **Launch an EC2 Instance** via AWS EC2 Dashboard.
2. **Key Pair**: Select `vockey` (for SSH).
3. **Security Group Rules**:
   - Allow **Port 22 (SSH)** and **Port 80 (HTTP)**.
4. **Advanced Details**:
   - Add user data from `userdata.sh`.
   - Edit your `.env`:
     ```bash
     echo "DISCORD_TOKEN=YOUR_DISCORD_TOKEN_HERE" > .env
     ```
5. **Launch Instance** — wait 2 minutes for the server to initialize.

---

## 🕹️ How to Play

### Commands
| Command | Description |
|:---|:---|
| `gojo` | List available bot commands |
| `!play` | Start a new Blackjack game |
| `!join` | Join an ongoing session |
| `!bet [amount]` | Place your wager |
| `!hit` | Request another card |
| `!stand` | Hold your hand |
| `!balance` | Check your current coins |

---

## 🧩 Game Mechanics

- Start with **40 coins**.
- Place bets each round.
- Play against the dealer (Gojo!).
- **Blackjack** pays **1.5x** your bet.
- Bust (over 21) and lose your bet.

---

## 🗺️ System Architecture

![System Diagram](src/main/java/DiscordUML.png)

### Key Components
- **Discord Message Handler**: Captures and processes user inputs.
- **Game Session Manager**: Tracks game states.
- **Database Manager**: Saves player data and balances.
- **Player Entity**: Tracks hands, bets, and balances.
- **Discord Message Manager**: Handles card operations and bot startup.
