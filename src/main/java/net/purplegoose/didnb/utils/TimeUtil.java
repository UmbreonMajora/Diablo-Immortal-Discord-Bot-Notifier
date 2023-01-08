package net.purplegoose.didnb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    private TimeUtil() {
        // all static methods.
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }

    public static String getTimeWithWeekday(String timeZone) {
        ZonedDateTime dateTime = Instant.now().atZone(ZoneId.of(timeZone, ZoneId.SHORT_IDS));
        return dateTime.format(DateTimeFormatter.ofPattern("EEEE HH:mm"));
    }

    public static String getCurrentWeekday(String timeZone) {
        ZonedDateTime dateTime = Instant.now().atZone(ZoneId.of(timeZone, ZoneId.SHORT_IDS));
        return dateTime.format(DateTimeFormatter.ofPattern("EEEE"));
    }

    public static String getTime(String timezone) {
        ZonedDateTime dateTime = Instant.now().atZone(ZoneId.of(timezone, ZoneId.SHORT_IDS));
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static boolean isTimeInBetween(String currentTime, String startTime, String endTime) {
        try {
            Date currentTimeDate = new SimpleDateFormat("HH:mm").parse(currentTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentTimeDate);

            Calendar warnStartCalender = Calendar.getInstance();
            warnStartCalender.setTime(new SimpleDateFormat("HH:mm").parse(startTime));

            Calendar warnEndCalender = Calendar.getInstance();
            warnEndCalender.setTime(new SimpleDateFormat("HH:mm").parse(endTime));

            if (cal.equals(warnStartCalender)) {
                return true;
            }

            return currentTimeDate.after(warnStartCalender.getTime()) && currentTimeDate.before(warnEndCalender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
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
            Date date = new SimpleDateFormat("HH:mm").parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
