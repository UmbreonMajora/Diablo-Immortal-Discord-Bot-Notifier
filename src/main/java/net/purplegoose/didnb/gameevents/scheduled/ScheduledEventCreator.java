package net.purplegoose.didnb.gameevents.scheduled;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.dv8tion.jda.api.entities.Guild;
import net.purplegoose.didnb.enums.Weekday;
import net.purplegoose.didnb.utils.StringUtil;

import java.time.*;

public class ScheduledEventCreator {

    public static void createScheduledEvent(Guild guild, EventTime eventTime, String timeZone) {
        DayOfWeek weekday = DayOfWeek.valueOf(eventTime.getWeekday().name);
        ZoneOffset offset = getZoneOffsetByTimeZone(timeZone);
        OffsetDateTime nextWeekday = getNextWeekday(weekday, offset);
        OffsetDateTime startTime = getOffsetDateTimeWithHourAndMinute(eventTime.getStartHour(), eventTime.getStartMinute());
        OffsetDateTime endTime = getOffsetDateTimeWithHourAndMinute(eventTime.getEndHour(), eventTime.getEndMinute());


        guild.createScheduledEvent("Event Name", "Diablo4 Location", startTime, endTime)
                .setDescription("EVENT DESCRIPTION?").queue();
    }

    public static void main(String[] args) {
        EventTime eventTime = new EventTime(Weekday.valueOf("monday"), "11:45", "12:00", 12, 0, 12, 30);
        String timeZone = "GMT-4";





    }

    private static OffsetDateTime getOffsetDateTimeWithHourAndMinute(int hour, int minute) {
        return OffsetDateTime.now()
                .withHour(hour).withMinute(minute).withSecond(0).withNano(0);
    }

    private static ZoneOffset getZoneOffsetByTimeZone(String timeZone) {
        String formattedTimeZone = StringUtil.removeAlphabeticCharacters(timeZone);
        return formattedTimeZone.equals(StringUtil.EMPTY_STRING) ? ZoneOffset.UTC : ZoneOffset.of(formattedTimeZone);
    }

    public static OffsetDateTime getNextWeekday(DayOfWeek weekday, ZoneOffset offset) {
        LocalDate today = LocalDate.now();
        DayOfWeek currentDayOfWeek = today.getDayOfWeek();

        int daysUntilNextMonday = weekday.getValue() - currentDayOfWeek.getValue();
        if (daysUntilNextMonday <= 0) {
            daysUntilNextMonday += 7;
        }

        LocalDate nextMondayDate = today.plusDays(daysUntilNextMonday);
        return nextMondayDate.atStartOfDay().atOffset(offset);
    }

}


@AllArgsConstructor
@Data
class EventTime {

    Weekday weekday;

    String warnStartTime;
    String warnEndTime;

    int startHour;
    int startMinute;

    int endHour;
    int endMinute;

}
