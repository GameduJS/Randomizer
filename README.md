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
<img src="https://github.com/GameduJS/Randomizer/assets/64703035/eff38b7c-868f-49b7-9edb-d5113615fe38">

### Settings
Any operator or player with the permission ``randomizer.game``can edit the game settings[^1]. <br>
> **_NOTE:_** The list  of changeable settings is not finished

[^1]: Players should not edit settings simultaneously

<img src="https://github.com/GameduJS/Randomizer/assets/64703035/ea50109a-0400-464d-b917-43791e52a240">

### Gameplay
To start the game execute ``/game start`` <br>
Every ``x``seconds a random item is dropped on each platform[^2]. <br>
The goal of each player is to get as far as possible in the given time

[^2]: As long as the player is online

<img src="https://github.com/GameduJS/Randomizer/assets/64703035/38fd2fa7-b02a-4a3d-9c68-35e2c971df46">


### End of game
The game ends when the time is up or ```/game stop``` is executed. <br>
The winner will be announced and the server will shut down and delete the world.

<img src="https://github.com/GameduJS/Randomizer/assets/64703035/13840174-978b-4fde-a271-d59d24c90db2" alt="End of the game">


### Upcoming changes
The plugin is still in development. <br>
Ideas/Plans for the future:

>
