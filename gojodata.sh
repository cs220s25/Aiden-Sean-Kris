#!/bin/bash
yum install git -y
yum install -y maven-amazon-corretto21
dnf install mariadb105 -y
dnf install mariadb105-server -y
systemctl enable mariadb
systemctl start mariadb
