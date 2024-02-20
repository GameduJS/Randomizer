package de.gamdude.randomizer.ui.visuals;

import de.gamdude.randomizer.game.handler.GameDispatcher;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RScore {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacySection();

    private final BiConsumer<Team, Integer> updateConsumer;
    private final Supplier<Integer> secondsSupplier;
    private final Team team;

    private RScore(Scoreboard scoreboard, String name, int score, String prefix, String suffix, BiConsumer<Team, Integer> updateConsumer, Supplier<Integer> secondsSupplier) {
        Objective objective = Objects.requireNonNull(scoreboard.getObjective("sideboard"));
        this.team = createScore(scoreboard, objective, name, prefix, suffix, score);
        this.updateConsumer = updateConsumer;
        this.secondsSupplier = secondsSupplier;
    }

    public void updateScore() {
        this.updateConsumer.accept(team, secondsSupplier.get());
    }

    private Team createScore(Scoreboard scoreboard, Objective objective, String name, String prefix, String suffix, int score) {
        NamedTextColor color1 = NamedTextColor.NAMES.values().stream().sorted().toList().get(15 - score % 16);
        NamedTextColor color2 = NamedTextColor.NAMES.values().stream().sorted().toList().get(score % 16);

        String entry = legacyComponentSerializer.serialize(miniMessage.deserialize("<" + color1.toString() + ">" + " " + "<" + color2.toString() + ">"));
        Team team = scoreboard.registerNewTeam(name);
        team.addEntry(entry);
        objective.getScore(entry).setScore(score);
        team.prefix(miniMessage.deserialize(prefix));
        team.suffix(miniMessage.deserialize(suffix));

        return team;
    }

    public static class Builder {

        private Scoreboard scoreboard;
        private final String name;
        private int score = 0;
        private String prefix = "";
        private String suffix = "";
        private BiConsumer<Team, Integer> teamConsumer = (team1, seconds) -> {};

        public Builder() {
            this.name = UUID.randomUUID().toString();
        }

        public Builder scoreboard(Scoreboard scoreboard) {
            this.scoreboard = scoreboard;
            return this;
        }

        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder onUpdate(BiConsumer<Team, Integer> teamSecondsConsumer) {
            this.teamConsumer = teamSecondsConsumer;
            return this;
        }

        public RScore build(GameDispatcher gameDispatcher) {
            return new RScore(scoreboard, name, score, prefix, suffix, teamConsumer, gameDispatcher::getSecondsPlayed);
        }

        public RScore.Builder blankScore(int score) {
            return new Builder().score(score).prefix("   ");
        }

    }


}
