#!/bin/bash

# Install SQL (MariaDB Example)
sudo apt update
sudo apt install -y mariadb-server
sudo systemctl start mariadb
sudo systemctl enable mariadb
sudo mysql_secure_installation

# Clone the Repository
git clone https://github.com/cs220s25/Aiden-Sean-Kris.git
cd Aiden-Sean-Kris

# Build the Project
mvn clean package

# Run the Application
java -jar target/dbot-1.0-SNAPSHOT.jar
