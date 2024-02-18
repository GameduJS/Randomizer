package de.gamdude.randomizer.game.handler;

import de.gamdude.randomizer.config.Config;

public interface Handler {

   default void loadConfig(Config config) {

   }

    default void reloadConfig(Config config) {

    }

}
