package de.gamdude.randomizer.ui;

import de.gamdude.randomizer.base.GameDispatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;


public class RandomizerScoreboard {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final GameDispatcher gameDispatcher;

    public RandomizerScoreboard(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
    }

    public void updateScoreboard(Player player) {
        Team playerCount = player.getScoreboard().getTeam("playerCounter");
        playerCount.suffix(miniMessage.deserialize("<yellow>" + Bukkit.getOnlinePlayers().size()));

        Team blocksBuiltTeam = player.getScoreboard().getTeam("blocksBuilt");
        blocksBuiltTeam.suffix(miniMessage.deserialize("<yellow>" + gameDispatcher.getPlayerProgressHandle().getBlocksBuilt(player)));
    }

    public Scoreboard getScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective sidebar = scoreboard.registerNewObjective("sideboard", Criteria.DUMMY, miniMessage.deserialize("<color:#f878ff><b>Randomizer</b></color>"));
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score decoScore = sidebar.getScore(stringToLegacy("<light_purple>˂--------------------------˃"));
        decoScore.setScore(16);

        Score playersScore = sidebar.getScore(stringToLegacy("<grey>Players"));
        playersScore.setScore(15);

        Team playerCount = scoreboard.registerNewTeam("playerCounter");
        playerCount.addEntry(stringToLegacy("<blue>" + " " + "<yellow>"));
        sidebar.getScore(stringToLegacy("<blue>" + " " + "<yellow>")).setScore(14);
        playerCount.prefix(miniMessage.deserialize("<dark_gray> »"));

        Score blocksBuiltString = sidebar.getScore(stringToLegacy("<gray>Blocks built"));
        blocksBuiltString.setScore(13);

        Team builtBlocksTeam = scoreboard.registerNewTeam("blocksBuilt");
        builtBlocksTeam.addEntry(stringToLegacy("<red>" + " " + "<blue>"));
        sidebar.getScore(stringToLegacy("<red>" + " " + "<blue>")).setScore(12);
        builtBlocksTeam.prefix(miniMessage.deserialize("<dark_gray> »"));

        return scoreboard;
    }

    private String stringToLegacy(String legacy) {
        return LegacyComponentSerializer.legacySection().serialize(miniMessage.deserialize(legacy));
    }

}
