#!/bin/bash

sudo docker compose down
sudo git pull origin main
sudo docker compose up --build -d