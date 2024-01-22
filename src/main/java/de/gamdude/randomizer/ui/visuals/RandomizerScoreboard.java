package de.gamdude.randomizer.ui.visuals;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.base.LeaderboardHandler;
import de.gamdude.randomizer.base.PlayerProgressTracker;
import de.gamdude.randomizer.base.goals.Goal;
import de.gamdude.randomizer.base.goals.GoalHandler;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.world.PlatformLoader;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TODO: <p></p>
 * - Clean up scoreboard <br>
 * - Make it easier to add/edit entries <br>
 * - Edit general design (take {@link  Goal} into account)
 */
public class RandomizerScoreboard {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final GameDispatcher gameDispatcher;
    private final LeaderboardHandler leaderboardHandler;
    private final GoalHandler goalHandler;
    private final PlayerProgressTracker playerProgressTracker;
    private final PlatformLoader platformLoader;

    private final ArrayList<RScore> scores;

    public RandomizerScoreboard(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.leaderboardHandler = gameDispatcher.getHandler(LeaderboardHandler.class);
        this.goalHandler = gameDispatcher.getHandler(GoalHandler.class);
        this.playerProgressTracker = gameDispatcher.getHandler(PlayerProgressTracker.class);
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);

        this.scores = new ArrayList<>();
    }

    public void updateScoreboard() {
        scores.forEach(RScore::updateScore);
    }

    public void setScoreboard(Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective sidebar = scoreboard.registerNewObjective("sideboard", Criteria.DUMMY, miniMessage.deserialize("<color:#f878ff><b>Randomizer</b></color>"));
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);

        List<RScore.Builder> scoreBuilder = new ArrayList<>();
        scoreBuilder.add(new RScore.Builder().score(17).prefix("<light_purple>˂------------------------------˃"));
        scoreBuilder.add(new RScore.Builder().score(16).prefix(MessageHandler.getString(player, "scoreboardPreGoalTitle")).onUpdate((team, seconds) ->  team.prefix(miniMessage.deserialize(goalHandler.getActiveGoal().getScoreboardGoalTitle(player)))));
        scoreBuilder.add(new RScore.Builder().score(15).prefix("<dark_gray> »").suffix(" <red>-").onUpdate((team, integer) -> team.suffix(miniMessage.deserialize(goalHandler.getActiveGoal().getScoreboardGoalValue(player.getUniqueId())))));
        scoreBuilder.add(new RScore.Builder().decoScoreBuild(14));
        scoreBuilder.add(new RScore.Builder().score(13).prefix(MessageHandler.getString(player, "scoreboardBlocksPlacedTitle")));
        scoreBuilder.add(new RScore.Builder().score(12).prefix("<dark_gray> »").onUpdate((team, integer) -> team.suffix(miniMessage.deserialize("<yellow>" + playerProgressTracker.getBlocksBuilt(player.getUniqueId())))));
        scoreBuilder.add(new RScore.Builder().decoScoreBuild(11));
        scoreBuilder.add(new RScore.Builder().score(10).prefix(MessageHandler.getString(player, "scoreboardTop3Title")));
        for(int i = 1; i <=3; ++i) {
            int index = i;
            scoreBuilder.add(new RScore.Builder().score(10 - i).prefix(getTopPlayersPrefix(i)).suffix("<red> -").onUpdate((team, integer) ->  {
                var topPlayers = leaderboardHandler.getTopPlayers().getPlayerList();

                if(topPlayers.size() < index) {
                    team.suffix(miniMessage.deserialize("<gray> ✘"));
                    return;
                }
                UUID topPlayerUUID = topPlayers.get(index - 1);
                OfflinePlayer topPlayer = Bukkit.getOfflinePlayer(topPlayerUUID);
                String playerString = ((topPlayerUUID == player.getUniqueId()) ? "<bold>" : "") + topPlayer.getName();
                int rank = leaderboardHandler.getPosition(topPlayerUUID);
                team.prefix(miniMessage.deserialize(getTopPlayersPrefix(rank)));
                team.suffix(miniMessage.deserialize(playerString + " <dark_gray>► <yellow>" + platformLoader.getPlatform(topPlayerUUID).getBlocksBuilt()));
            }));
        }
        scoreBuilder.add(new RScore.Builder().decoScoreBuild(6));
        scoreBuilder.add(new RScore.Builder().score(5).prefix(MessageHandler.getString(player, "scoreboardYourRankTitle")));
        scoreBuilder.add(new RScore.Builder().score(4).prefix("<dark_gray> »").suffix(" <red>-").onUpdate((team, integer) -> team.suffix(miniMessage.deserialize("<yellow>" + leaderboardHandler.getPosition(player.getUniqueId())))));

        scoreBuilder.forEach(builder -> scores.add(builder.scoreboard(scoreboard).build(gameDispatcher)));
    }

    private String getTopPlayersPrefix(int i) {
        return switch (i) {
            case 1 -> "<dark_gray> »  <color:#d3af37>1. ";
            case 2 -> "<dark_gray> »  <color:#C0C0C0>2. ";
            case 3 -> "<dark_gray> »  <color:#bf8970>3. ";
            default -> "<dark_gray> » <gray>" + i + ". ";
        };
    }

}
