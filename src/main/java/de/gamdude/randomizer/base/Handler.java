package de.gamdude.randomizer.base;

import de.gamdude.randomizer.config.Config;

public interface Handler {

   default void loadConfig(Config config) {

   }

    default void reloadConfig(Config config) {

    }

}
