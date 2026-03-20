package io.github.knightmareleon.shared.utils;

import io.github.knightmareleon.shared.constants.TimeSetting;

public class Converter {
    public static TimeSetting toTimeSetting(String timeString){
        return switch(timeString){
            case "0:30" -> TimeSetting.THIRTY_SECONDS;
            case "1:00" -> TimeSetting.ONE_MINUTE;
            case "3:00" -> TimeSetting.THREE_MINUTES;
            case "5:00" -> TimeSetting.FIVE_MINUTES;
            case "10:00" -> TimeSetting.TEN_MINUTES;
            default -> null;
        };
    }

    public static String minuteTextForm(int seconds){
        int minute = seconds / 60;
        int second = (seconds - (minute * 60));
        return String.format("%02d:%02d", minute, second);
    }
}
