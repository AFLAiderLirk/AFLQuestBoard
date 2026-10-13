
# AFLQuestBoard

A Minecraft quest plugin for Paper 1.20.4.

## Features

- Quest menu with the `/quests` command
- Zombie hunting quests
- Skeleton hunting quests
- Stone mining quests
- Customizable goals and rewards
- Persistent player progress
- Protection against duplicate rewards
- Automatic progress saving
- Protected inventory GUI

## Installation

1. Build the plugin using Maven.
2. Copy the generated JAR into the server's `plugins` folder.
3. Restart the Paper server.
4. Join the server and type `/quests`.

## Build

Requirements:
- Java 17+
- Maven

Run:

    mvn clean package

## Configuration

Quest goals and rewards can be configured in:

    plugins/AFLQuestBoard/config.yml

## Command

`/quests` - Opens the quest board.

## Author

AFLAiderLirk
