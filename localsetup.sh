#!/bin/bash

# Build the Project
mvn clean package

# Run the Application
java -jar target/dbot-1.0-SNAPSHOT.jar
