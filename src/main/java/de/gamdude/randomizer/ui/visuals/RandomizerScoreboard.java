package de.gamdude.randomizer.ui.visuals;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.base.LeaderboardHandler;
import de.gamdude.randomizer.base.PlayerProgressTracker;
import de.gamdude.randomizer.game.goals.Goal;
import de.gamdude.randomizer.game.goals.GoalHandler;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.utils.TimeConverter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
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

    private final ArrayList<RScore> scores;

    public RandomizerScoreboard(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.leaderboardHandler = gameDispatcher.getHandler(LeaderboardHandler.class);
        this.goalHandler = gameDispatcher.getHandler(GoalHandler.class);
        this.playerProgressTracker = gameDispatcher.getHandler(PlayerProgressTracker.class);

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
        scoreBuilder.add(new RScore.Builder().score(16).prefix("<light_purple>˂------------------------------˃"));
        scoreBuilder.add(new RScore.Builder().score(15).prefix(MessageHandler.getString(player, "scoreboardYourRankTitle")));
        scoreBuilder.add(new RScore.Builder().score(14).prefix("<dark_gray> »").suffix("<gray>✘").onUpdate((team, integer) -> team.suffix(miniMessage.deserialize(MessageHandler.getString(player, "scoreboardYourRankValue"
        , leaderboardHandler.getPosition(player.getUniqueId()) + "", playerProgressTracker.getBlocksBuilt(player.getUniqueId()) + "")))));
        scoreBuilder.add(new RScore.Builder().blankScore(13));
        scoreBuilder.add(new RScore.Builder().score(12).prefix(MessageHandler.getString(player, "scoreboardTop3Title")));
        for(int i = 1; i <=3; ++i) {
            int index = i;
            scoreBuilder.add(new RScore.Builder().score(12 - i).prefix(getTopPlayersPrefix(i)).suffix("<red>-").onUpdate((team, integer) ->  {
                var topPlayers = leaderboardHandler.getTopPlayers();

                if(topPlayers.size() < index) {
                    team.suffix(miniMessage.deserialize("<gray>✘"));
                    return;
                }
                UUID topPlayerUUID = topPlayers.get(index - 1);
                OfflinePlayer topPlayer = Bukkit.getOfflinePlayer(topPlayerUUID);
                String playerString = ((topPlayerUUID == player.getUniqueId()) ? "<bold>" : "") + topPlayer.getName();
                int rank = leaderboardHandler.getPosition(topPlayerUUID);
                team.prefix(miniMessage.deserialize(getTopPlayersPrefix(rank)));
                team.suffix(MessageHandler.getMessage(player, "scoreboardListPlayer", playerString, playerProgressTracker.getBlocksBuilt(topPlayerUUID) + ""));
            }));
        }
        scoreBuilder.add(new RScore.Builder().blankScore(8));
        scoreBuilder.add(new RScore.Builder().score(7).prefix(MessageHandler.getString(player, "scoreboardPreGoalTitle")));
        scoreBuilder.add(new RScore.Builder().score(6).prefix("<gray>>").suffix(MessageHandler.getString(player, "scoreboardNotStarted"))
                .onUpdate((team, integer) -> team.suffix(miniMessage.deserialize(goalHandler.getActiveGoal().getScoreboardGoalDescription(player)))));

        scoreBuilder.add(new RScore.Builder().score(5).prefix("<gray>>").suffix(MessageHandler.getString(player, "scoreboardNotStarted"))
                .onUpdate((team, integer) -> team.suffix(miniMessage.deserialize(goalHandler.getActiveGoal().getScoreboardGoalValue(player.getUniqueId())))));

        scoreBuilder.add(new RScore.Builder().blankScore(4));
        scoreBuilder.add(new RScore.Builder().blankScore(3));
        scoreBuilder.add(new RScore.Builder().score(2).prefix(MessageHandler.getString(player, "scoreboardTimeElapsedTitle")).suffix("<gray>✘")
                .onUpdate((team, integer) -> team.suffix(MessageHandler.getMessage(player, "scoreboardTimeElapsedSuffix", TimeConverter.getTimeString(integer)))));

        scoreBuilder.forEach(builder -> scores.add(builder.scoreboard(scoreboard).build(gameDispatcher)));
    }

    private String getTopPlayersPrefix(int i) {
        return switch (i) {
            case 1 -> "<dark_gray> »  <color:#d3af37>1.";
            case 2 -> "<dark_gray> »  <color:#C0C0C0>2.";
            case 3 -> "<dark_gray> »  <color:#bf8970>3.";
            default -> "<dark_gray> » <gray>" + i + ". ";
        };
    }

}
