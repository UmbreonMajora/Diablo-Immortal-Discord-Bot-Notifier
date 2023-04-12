package net.purplegoose.didnb.gameevents;

import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.EventGameData;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static net.purplegoose.didnb.utils.StringUtil.EMPTY_STRING;

public interface IGameEvent {

    default String isTimeInWarnRange(Set<EventGameData> eventGameDataSet, String timeZone) {
        String currentTimeAsString = TimeUtil.getTime(timeZone);

        for (EventGameData data : eventGameDataSet) {
            if (data.getWeekday() == null || data.getWeekday().equalsIgnoreCase(TimeUtil.getCurrentWeekday(timeZone))) {
                String[] warnRange = data.getWarnRange().split("-");
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
        return clientGuild.isWarnMessagesEnabled() && notificationChannel.isEventWarnMessage();
    }

    default boolean isEventMessageEnabled(ClientGuild clientGuild, NotificationChannel notificationChannel) {
        return clientGuild.isEventMessageEnabled() && notificationChannel.isEventMessageEnabled();
    }

}
