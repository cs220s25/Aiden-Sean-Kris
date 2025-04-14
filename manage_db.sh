#!/bin/bash

ACTION=$1

if [ "$ACTION" == "create" ]; then
    mysql -u root -p < create_db.sql
elif [ "$ACTION" == "drop" ]; then
    mysql -u root -p < drop_db.sql
else
    echo "Usage: $0 {create|drop}"
fi
