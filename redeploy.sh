#!/bin/bash

sudo systemctl stop Blackjackbot.service
sudo git pull origin main
sudo mvn clean package
sudo systemctl start Blackjackbot.service
