#!/bin/bash
# Set up EC2 instance for Blackjackbot

# Update and install dependencies
sudo yum update -y
sudo yum install -y git expect maven-amazon-corretto21

# Install MariaDB
sudo dnf install -y mariadb105 mariadb105-server
sudo systemctl enable mariadb
sudo systemctl start mariadb

# Secure MariaDB with 'expect'
expect <<EOF
spawn sudo mysql_secure_installation
expect "Enter current password for root (enter for none):"
send "\r"
expect "Switch to unix_socket authentication"
send "n\r"
expect "Change the root password?"
send "y\r"
expect "New password:"
send "rootpass\r"
expect "Re-enter new password:"
send "rootpass\r"
expect "Remove anonymous users?"
send "y\r"
expect "Disallow root login remotely?"
send "y\r"
expect "Remove test database and access to it?"
send "y\r"
expect "Reload privilege tables now?"
send "y\r"
expect eof
EOF

# Clone the repo
cd /home/ec2-user
git clone https://github.com/cs220s25/Aiden-Sean-Kris.git
cd Aiden-Sean-Kris

# Create the .env file (replace YOUR_DISCORD_TOKEN_HERE)
echo "DISCORD_TOKEN=YOUR_DISCORD_TOKEN_HERE" > .env

# Create database
expect <<EOF
spawn sh manage_db.sh create
expect "Enter password:"
send "rootpass\r"
expect eof
EOF

# Build the bot
mvn clean package

# Copy .env into target directory
cp .env target/.env

# Create the systemd service file
sudo tee /etc/systemd/system/Blackjackbot.service > /dev/null <<EOL
[Unit]
Description=Blackjackbot Service
After=network.target

[Service]
User=ec2-user
WorkingDirectory=/home/ec2-user/Aiden-Sean-Kris
ExecStart=/usr/bin/java -jar /home/ec2-user/Aiden-Sean-Kris/target/dbot-1.0-SNAPSHOT.jar
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
EOL

# Enable and start the service
sudo systemctl daemon-reload
sudo systemctl enable Blackjackbot.service
sudo systemctl start Blackjackbot.service
