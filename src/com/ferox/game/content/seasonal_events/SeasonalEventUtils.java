package com.ferox.game.content.seasonal_events;

import com.ferox.game.world.World;

import java.util.Calendar;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 12, 2021
 */
public class SeasonalEventUtils {

    private static boolean spring() {
        int month = World.getWorld().getCalendar().get(Calendar.MONTH);
        return month == 2 || month == 3 || month == 4;
    }

    private static boolean easter() {
        int month = World.getWorld().getCalendar().get(Calendar.MONTH);
        return month == 3;//The whole month of April we have a easter event
    }

    private static boolean summer() {
        int month = World.getWorld().getCalendar().get(Calendar.MONTH);
        return month == 5 || month == 6 || month == 7;
    }

    private static boolean autumn() {
        int month = World.getWorld().getCalendar().get(Calendar.MONTH);
        return month == 8 || month == 9 || month == 10;
    }

    private static boolean halloween() {
        int month = World.getWorld().getCalendar().get(Calendar.MONTH);
        int week = World.getWorld().getCalendar().get(Calendar.WEEK_OF_MONTH);
        return month == 9 && (week == 2 || week == 3 || week == 4);
    }

    private static boolean blackFriday() {
        int month = World.getWorld().getCalendar().get(Calendar.MONTH);
        int day = World.getWorld().getCalendar().get(Calendar.DAY_OF_MONTH);
        return month == 10 && day == 26;
    }

    private static boolean christmas() {
        int month = World.getWorld().getCalendar().get(Calendar.MONTH);
        return month == 11 || month == 12;//The whole month of December we have a christmas event
    }

    public static final boolean SPRING_EVENT_ACTIVE = spring();
    public static final boolean EASTER_EVENT_ACTIVE = easter();
    public static final boolean SUMMER_EVENT_ACTIVE = summer();
    public static final boolean AUTUMN_EVENT_ACTIVE = autumn();
    public static final boolean HALLOWEEN_EVENT_ACTIVE = halloween();
    public static final boolean BLACK_FRIDAY_EVENT_ACTIVE = blackFriday();
    public static final boolean CHRISTMAS_EVENT_ACTIVE = christmas();

}
