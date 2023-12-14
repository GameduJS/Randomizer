package de.gamdude.randomizer.ui.visuals;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.ui.base.LeaderboardObject;
import de.gamdude.randomizer.utils.TimeConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.examination.Examiner;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;


public class RandomizerScoreboard {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final GameDispatcher gameDispatcher;
    private int timeToPlay;

    public RandomizerScoreboard(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
    }

    public void loadConfig() {
        this.timeToPlay = gameDispatcher.getConfig().getProperty("playTime").getAsInt();
    }

    // Care attention for ties
    public void updateScoreboard(Player player) {
        Team playerCount = player.getScoreboard().getTeam("timePlay");
        playerCount.suffix(miniMessage.deserialize("<yellow>" + TimeConverter.getTimeString(timeToPlay - gameDispatcher.getSecondsPlayed())));

        Team blocksBuiltTeam = player.getScoreboard().getTeam("blocksBuilt");
        blocksBuiltTeam.suffix(miniMessage.deserialize("<yellow>" + gameDispatcher.getPlayerProgressHandle().getBlocksBuilt(player)));

        List<UUID> topPlayerList = gameDispatcher.getLeaderboardHandler().getTopPlayers().getPlayerList();
        for(int i = 1; i <= 3; ++i) {
            Team topPlayersTeam = player.getScoreboard().getTeam("topPlayer" + i);
            if(topPlayerList.size() >= i) {
                UUID topPlayerUUID = topPlayerList.get(i - 1);
                OfflinePlayer topPlayer = Bukkit.getOfflinePlayer(topPlayerUUID);
                String playerString = ( (topPlayerUUID == player.getUniqueId()) ? "<bold>" : "" ) + topPlayer.getName();
                topPlayersTeam.suffix(miniMessage.deserialize(playerString + " <dark_gray>► <yellow>" + gameDispatcher.getPlatformLoader().getPlatform(topPlayerUUID).getBlocksBuilt()));
            } else {
                topPlayersTeam.suffix(miniMessage.deserialize("<gray> ✘"));
            }
        }

        Team rankPlayerTeam = playerCount.getScoreboard().getTeam("rankPlayer");
        rankPlayerTeam.suffix(miniMessage.deserialize("<yellow>" + gameDispatcher.getLeaderboardHandler().getPosition(player.getUniqueId())));
    }

    public Scoreboard getScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective sidebar = scoreboard.registerNewObjective("sideboard", Criteria.DUMMY, miniMessage.deserialize("<color:#f878ff><b>Randomizer</b></color>"));
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        newScore(scoreboard, sidebar, "deco1", "<light_purple>˂--------------------------˃", 16);
        newScore(scoreboard, sidebar, "timePlayTitle", "<gray><b>Time:", 15);
        newScore(scoreboard, sidebar, "timePlay", "<dark_gray> »", 14);
        newScore(scoreboard, sidebar, "space1" ,"   ", 13);
        newScore(scoreboard, sidebar, "blocksBuiltTitle", "<gray><b>Blocks Placed:", 12);
        newScore(scoreboard, sidebar, "blocksBuilt", "<dark_gray> »", 11);
        newScore(scoreboard, sidebar, "space2" ,"   ", 10);
        newScore(scoreboard, sidebar, "top3PlayerTitle", "<gray><b>Top <yellow>3 <gray>Players:", 9);
        newScore(scoreboard, sidebar, "topPlayer1", getTopPlayersPrefix(1), 8);
        newScore(scoreboard, sidebar, "topPlayer2", getTopPlayersPrefix(2), 7);
        newScore(scoreboard, sidebar, "topPlayer3", getTopPlayersPrefix(3), 6);
        newScore(scoreboard, sidebar, "space3" ,"   ", 5);
        newScore(scoreboard, sidebar, "rankTitle", "<gray><b>Your Rank:", 4);
        newScore(scoreboard, sidebar, "rankPlayer", "<dark_gray> »", 3);

        return scoreboard;
    }

    private String stringToLegacy(String legacy) {
        return LegacyComponentSerializer.legacySection().serialize(miniMessage.deserialize(legacy));
    }

    private void newScore(Scoreboard scoreboard, Objective objective, String name, String content, int score) {
        NamedTextColor color1 = NamedTextColor.NAMES.values().stream().sorted().toList().get(score % 16);
        NamedTextColor color2 = NamedTextColor.NAMES.values().stream().sorted().toList().get(15 - score % 16);

        String entry = stringToLegacy("<" + color1.toString() + ">" + " " + "<" + color2.toString() + ">");

        Team team = scoreboard.registerNewTeam(name);
        team.addEntry(entry);
        objective.getScore(entry).setScore(score);
        team.prefix(miniMessage.deserialize(content));
    }

    private String getTopPlayersPrefix(int i) {
        return switch (i) {
            case 1 -> "<dark_gray> »  <color:#d3af37>1. ";
            case 2 -> "<dark_gray> »  <color:#C0C0C0>2. ";
            case 3 -> "<dark_gray> »  <color:#bf8970>3. ";
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }

}
