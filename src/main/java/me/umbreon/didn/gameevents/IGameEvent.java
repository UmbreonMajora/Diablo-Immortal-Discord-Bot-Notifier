package me.umbreon.didn.gameevents;

import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.data.EventGameData;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static me.umbreon.didn.utils.StringUtil.EMPTY_STRING;

public interface IGameEvent {

    default String isTimeInWarnRange(Set<EventGameData> eventGameDataSet, String timeZone) {
        String currentTimeAsString = TimeUtil.getTime(timeZone);

        for (EventGameData data : eventGameDataSet) {
            if (data.getWeekday() == null || data.getWeekday().equalsIgnoreCase(TimeUtil.getCurrentWeekday(timeZone))) {
                String[] warnRange = data.getWarnRange().split("-");

                System.out.println(">1 "+warnRange[0]);
                System.out.println(">2 "+warnRange[1]);
                System.out.println(">3 "+currentTimeAsString);
                System.out.println(">4 "+TimeUtil.isTimeInBetween(currentTimeAsString, warnRange[0], warnRange[1]));
                if (TimeUtil.isTimeInBetween(currentTimeAsString, warnRange[0], warnRange[1])) {
                    return warnRange[0];
                }
            }
        }

        return EMPTY_STRING;
    }

    default String getWarnMessageTime(String warnRangeStartTimeAsString, int warnTime) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = new SimpleDateFormat("HH:mm").parse(warnRangeStartTimeAsString);
            calendar.setTime(date);
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + warnTime);
        } catch (ParseException e) {
            return EMPTY_STRING;
        }
        String hours;
        if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
            hours = "0" + calendar.get(Calendar.HOUR_OF_DAY);
        } else {
            hours = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        }

        String minutes;
        if (calendar.get(Calendar.MINUTE) < 10) {
            minutes = "0" + calendar.get(Calendar.MINUTE);
        } else {
            minutes = String.valueOf(calendar.get(Calendar.MINUTE));
        }

        return hours + ":" + minutes;
    }

    default boolean isEventStarting(Set<EventGameData> eventGameDataSet, String timeZone) {
        String currentTimeAsString = TimeUtil.getTime(timeZone);

        for (EventGameData data : eventGameDataSet) {
            if (data.getWeekday() == null || data.getWeekday().equalsIgnoreCase(TimeUtil.getCurrentWeekday(timeZone))) {
                if (currentTimeAsString.equals(data.getEventStartTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    default boolean isWarnMessageEnabled(ClientGuild clientGuild, NotificationChannel notificationChannel) {
        return clientGuild.isWarnMessagesEnabled() && notificationChannel.isWarnMessagesEnabled();
    }

    default boolean isEventMessageEnabled(ClientGuild clientGuild, NotificationChannel notificationChannel) {
        return clientGuild.isEventMessageEnabled() && notificationChannel.isEventMessageEnabled();
    }

}
