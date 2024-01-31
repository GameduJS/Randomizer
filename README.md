# Randomizer
This repository is a Minecraft plugin of a minigame called 'Randomizer' or 'OneBlockRace' <br>
The player who bridges the furthest in a given time with random item/block drops wins.

## How to use the plugin
- Download the plugin
- Prepare a paper server (1.20.4)
- Put the plugin into the plugins folder
- Startup the server and close it when it is loaded
- Add the following to ``Bukkit.yml``:
````yaml
worlds:
  world:
    generator: Randomizer
````
- Restart the server

### World Generation
The plugin automatically generates a world with a bedrock block for each player in a different chunk
<img src="https://github.com/GameduJS/Randomizer/assets/64703035/5956ea07-a12b-4e9a-8b56-eda0f07d563e">

### Pre-Start of the game
Each player spawns on its platform. While the game hasn't started, players cannot move in survival mode. <br>
<img src="https://github.com/GameduJS/Randomizer/assets/64703035/277ad3e1-5c7b-49d6-9dfe-9a92ec929488">

### Settings
Any operator or player with the permission ``randomizer.game``can edit the game settings[^1]. <br>
> **_NOTE:_** The list  of changeable settings is not finished

[^1]: Players should not edit settings simultaneously

<img src="https://github.com/GameduJS/Randomizer/assets/64703035/30da2aa1-3ca2-4b29-977c-9e61fd122e31">

### Gameplay
To start the game execute ``/game start`` <br>
Every ``x``seconds a random item is dropped on each platform[^2]. <br>
The goal of each player is to get as far as possible in the given time

[^2]: As long as the player is online

<img src="https://github.com/GameduJS/Randomizer/assets/64703035/5535c128-dee7-4ec4-bdde-8bd6fcfd94c4">
<img src="https://github.com/GameduJS/Randomizer/assets/64703035/7cb5a7c3-8b26-45ae-a634-5622861a6cd7">

### End of game
The game ends when the time is up or ```/game stop``` is executed. <br>
The winner will be announced and the server will shut down and delete the world.

<img src="https://github.com/GameduJS/Randomizer/assets/64703035/9d0ec572-6491-4445-a6c9-7e3e96defddd">


### Upcoming changes
The plugin is still in development. <br>
Ideas/Plans for the future:

> - More adjustable setting (PVP, Allow moving to other platforms, Daytime, ...)
> - Disable advancements
> - Determine winner after criteria: ``x`` blocks builts
