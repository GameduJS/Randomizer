package de.gamdude.randomizer.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class TimeConverter {
    public static String getTimeString(int seconds) {
        return DurationFormatUtils.formatDuration(seconds * 1000L, "HH:mm:ss", true);
    }
}