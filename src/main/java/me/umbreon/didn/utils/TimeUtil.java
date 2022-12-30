package me.umbreon.didn.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    private static final String TIME_HH_MM = "HH:mm";
    private static final DateTimeFormatter WEEKDAY_WITH_TIME_HH_MM = DateTimeFormatter.ofPattern("EEEE HH:mm");
    private static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(TIME_HH_MM);

    private static final DateTimeFormatter WEEKDAY_FORMAT = DateTimeFormatter.ofPattern("EEEE");
    private static final DateFormat dd_MM_yyyy_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final DateFormat HH_mm_ss_SSS_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");

    private TimeUtil() {
        // all static methods.
    }

    public static String getCurrentDate() {
        return dd_MM_yyyy_FORMAT.format(new Date());
    }

    public static String getCurrentTime() {
        return HH_mm_ss_SSS_FORMAT.format(new Date());
    }

    public static String getTimeWithWeekday(String timeZone) {
        Instant timeStamp = Instant.now();
        ZonedDateTime timestamp = timeStamp.atZone(ZoneId.of(timeZone, ZoneId.SHORT_IDS));
        return timestamp.format(WEEKDAY_WITH_TIME_HH_MM);
    }

    public static String getCurrentWeekday(String timezone) {
        Instant timeStamp = Instant.now();
        ZonedDateTime timestampAtGMTPlus1 = timeStamp.atZone(ZoneId.of(timezone, ZoneId.SHORT_IDS));
        return timestampAtGMTPlus1.format(WEEKDAY_FORMAT);
    }

    public static Calendar getTimeAsCalendar(String time) throws ParseException {
        Date time1 = new SimpleDateFormat("HH:mm").parse(time);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time1);
        calendar1.add(Calendar.DATE, 1);
        return calendar1;
    }

    public static String getTime(String timezone) {
        Instant timeStamp = Instant.now();
        ZonedDateTime timestampAtGMTPlus1 = timeStamp.atZone(ZoneId.of(timezone, ZoneId.SHORT_IDS));
        return timestampAtGMTPlus1.format(formatter2);
    }

    /**
     * Format: HH:mm
     * @param currentTime is the current time.
     * @param startTime   is the start of the time range.
     * @param endTime     is the end of the time range.
     * @return true if currentTime is between startTime & endTime
     */
    public static boolean isTimeInBetween(String currentTime, String startTime, String endTime) {
        try {
            Date currentTimeDate = new SimpleDateFormat("HH:mm").parse(currentTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentTimeDate);

            Date betweenStartDate = new SimpleDateFormat("HH:mm").parse(startTime);
            Calendar warnStartCalender = Calendar.getInstance();
            warnStartCalender.setTime(betweenStartDate);

            Date betweenEndDate = new SimpleDateFormat("HH:mm").parse(endTime);
            Calendar warnEndCalender = Calendar.getInstance();
            warnEndCalender.setTime(betweenEndDate);

            return currentTimeDate.after(warnStartCalender.getTime()) && currentTimeDate.before(warnEndCalender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
