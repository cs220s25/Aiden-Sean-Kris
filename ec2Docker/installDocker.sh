#!bin/bash

# Main docker installation script for EC2
sudo yum install -y docker
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -a -G docker ec2-user

# Install Docker Compose
sudo mkdir -p /usr/local/lib/docker/cli-plugins
sudo curl -SL https://github.com/docker/compose/releases/download/v2.35.1/docker-compose-linux-x86_64   -o /usr/local/lib/docker/cli-plugins/docker-compose
sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

# Connect MYSQL & Bot Via Docker Network
sudo docker network create discord-network
sudo docker compose build
sudo docker compose up -d
