#!/bin/bash

sudo git pull origin main
mvn clean package -DskipTests
sudo systemctl restart Blackjackbot.service
