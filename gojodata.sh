#!/bin/bash

sudo yum update -y
sudo yum install -y git
sudo yum install -y maven-amazon-corretto21
sudo dnf install -y mariadb105
sudo dnf install -y mariadb105-server
sudo yum install -y expect  
sudo systemctl enable mariadb
sudo systemctl start mariadb

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

git clone https://github.com/cs220s25/Aiden-Sean-Kris.git
cd Aiden-Sean-Kris

echo "DISCORD_TOKEN=" > .env

expect <<EOF
spawn sh manage_db.sh create
expect "Enter password:"
send "rootpass\r"
expect eof
EOF

mvn clean package


sudo cp Blackjackbot.service /etc/systemd/system
sudo systemctl daemon-reload
sudo systemctl enable Blackjackbot.service
sudo systemctl start Blackjackbot.service
