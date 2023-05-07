package net.purplegoose.didnb.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class TimeUtil {

        private static final String HOURS_MINUTE_FORMAT = "HH:mm";
    private static final String WEEKDAY_FORMAT = "EEEE";
    public static final int HOURS_IN_SECONDS = 3600;

    private TimeUtil() {
        // all static methods.
    }

    public static String getTimeWithWeekday(String timeZone) {
        ZonedDateTime dateTime = Instant.now().atZone(ZoneId.of(timeZone, ZoneId.SHORT_IDS));
        return dateTime.format(DateTimeFormatter.ofPattern(WEEKDAY_FORMAT + " " + HOURS_MINUTE_FORMAT));
    }

    public static String getCurrentWeekday(String timeZone) {
        ZonedDateTime dateTime = Instant.now().atZone(ZoneId.of(timeZone, ZoneId.SHORT_IDS));
        return dateTime.format(DateTimeFormatter.ofPattern(WEEKDAY_FORMAT));
    }

    public static String getTime(String timezone) {
        ZonedDateTime dateTime = Instant.now().atZone(ZoneId.of(timezone, ZoneId.SHORT_IDS));
        return dateTime.format(DateTimeFormatter.ofPattern(HOURS_MINUTE_FORMAT));
    }

    public static boolean isTimeInBetween(String currentTime, String startTime, String endTime) {
        try {
            Date currentTimeDate = new SimpleDateFormat(HOURS_MINUTE_FORMAT).parse(currentTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentTimeDate);

            Calendar warnStartCalender = Calendar.getInstance();
            warnStartCalender.setTime(new SimpleDateFormat(HOURS_MINUTE_FORMAT).parse(startTime));

            Calendar warnEndCalender = Calendar.getInstance();
            warnEndCalender.setTime(new SimpleDateFormat(HOURS_MINUTE_FORMAT).parse(endTime));

            if (cal.equals(warnStartCalender)) {
                return true;
            }

            return currentTimeDate.after(warnStartCalender.getTime()) &&
                    currentTimeDate.before(warnEndCalender.getTime());
        } catch (ParseException e) {
            log.error("Failed to check if time is in between.", e);
        }
        return false;
    }

    public static Date getNextFullMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int nextMinute = calendar.get(Calendar.MINUTE) + 1;
        calendar.set(Calendar.MINUTE, nextMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getTimeAsCalendar(String time) {
        try {
            Date date = new SimpleDateFormat(HOURS_MINUTE_FORMAT).parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            return calendar.getTime();
        } catch (ParseException e) {
            log.error("Failed to get current time as calendar.", e);
        }
        return null;
    }

    public static long getCurrentUnixTime(String timeZoneId) {
        return Instant.now().atZone(ZoneId.of(timeZoneId)).toEpochSecond();
    }

    public static String getCurrentTimeMinusGivenHours(String timeZoneId, long hours) {
        return LocalTime.now(ZoneId.of(timeZoneId)).minusHours(hours).toString();
    }

}
