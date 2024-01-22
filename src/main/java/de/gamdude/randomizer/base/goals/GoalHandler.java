package de.gamdude.randomizer.base.goals;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.base.Handler;
import de.gamdude.randomizer.config.Config;

import java.util.Arrays;

public class GoalHandler implements Handler {

    private final GameDispatcher gameDispatcher;
    public final Goal[] GOALS;
    private Goal activeGoal;

    public GoalHandler(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.GOALS = new Goal[]{new TimeGoal(gameDispatcher), new BlockGoal(gameDispatcher)};
        this.activeGoal = GOALS[0];
    }

    @Override
    public void loadConfig(Config config) {
        int goalId = config.getProperty("currentGoal").getAsInt();
        Arrays.stream(GOALS).forEach(g -> g.loadConfig(config));
        this.activeGoal = GOALS[goalId];
    }

    @Override
    public void reloadConfig(Config config) {
        this.loadConfig(config);
    }

    public boolean isFinished() {
        return activeGoal.isFinished(gameDispatcher);
    }

    public Goal getActiveGoal() {
        return activeGoal;
    }

    public void setActiveGoal(int id) {
        gameDispatcher.getConfig().addProperty("currentGoal", id);
        this.activeGoal = GOALS[id];
    }
}
