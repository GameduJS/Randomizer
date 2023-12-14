package de.gamdude.randomizer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeConverter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String getTimeString(int seconds) {
        return dateFormat.format(new Date(seconds * 1000L));
    }

}
