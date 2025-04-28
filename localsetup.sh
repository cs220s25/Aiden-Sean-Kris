#!/bin/bash

# Clone the repo
git clone https://github.com/cs220s25/Aiden-Sean-Kris.git
cd Aiden-Sean-Kris

# Create the .env file (replace YOUR_DISCORD_TOKEN_HERE)
echo "DISCORD_TOKEN=YOUR_DISCORD_TOKEN_HERE" > .env

# Build the Project
mvn clean package

# Run the Application
java -jar target/dbot-1.0-SNAPSHOT.jar
