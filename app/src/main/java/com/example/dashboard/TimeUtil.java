package com.example.dashboard;

public class TimeUtil {

    public static String formatTimeUnit(int time) {
        if (time<10)
            return String.valueOf("0"+time);
        else
            return String.valueOf(time);

    }

    public static String formatTimeString(int format_time) {
        String hours = formatTimeUnit(format_time / 3600); //h
        String minutes = formatTimeUnit((format_time / 60) % 60); //m
        String seconds = formatTimeUnit(format_time % 60); //s

        return hours +":" +minutes + ":"+seconds;
    }

    public static String getFormatTime(int current_time) {
        current_time = Math.abs(current_time); // absolute value
        return formatTimeString(current_time) ;
    }
}
