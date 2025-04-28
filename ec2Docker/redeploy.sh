#!/bin/bash

sudo git config --global --add safe.directory /home/ec2-user/Aiden-Sean-Kris
sudo docker compose down
sudo git pull origin main
sudo docker compose up --build -d
