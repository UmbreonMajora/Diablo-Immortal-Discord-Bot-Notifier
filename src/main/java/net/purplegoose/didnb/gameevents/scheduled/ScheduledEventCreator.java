package net.purplegoose.didnb.gameevents.scheduled;

import net.dv8tion.jda.api.entities.Guild;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.EventTime;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;

import java.time.*;

public class ScheduledEventCreator {

    public void createScheduledEvent(Guild guild, EventTime eventTime, ClientGuild clientGuild) {
        DayOfWeek weekday = DayOfWeek.valueOf(String.valueOf(eventTime.getWeekday()));
        String timeZone = clientGuild.getTimeZone();
        Language language = clientGuild.getLanguage();
        ZoneOffset offset = getZoneOffsetByTimeZone(timeZone);
        OffsetDateTime nextWeekday = getNextWeekday(weekday, offset);
        OffsetDateTime startTime = getOffsetDateTimeWithHourAndMinute(nextWeekday ,eventTime.getStartHour(), eventTime.getStartMinute());
        OffsetDateTime endTime = getOffsetDateTimeWithHourAndMinute(nextWeekday ,eventTime.getEndHour(), eventTime.getEndMinute());

        guild.createScheduledEvent(LanguageController.getMessage(language, eventTime.getEventName().languageKey),
                        "EVENT-LOCATION", startTime, endTime).queue();
    }

    private OffsetDateTime getOffsetDateTimeWithHourAndMinute(OffsetDateTime offsetDateTime, int hour, int minute) {
        return offsetDateTime.withHour(hour).withMinute(minute).withSecond(0).withNano(0);
    }

    private ZoneOffset getZoneOffsetByTimeZone(String timeZone) {
        String formattedTimeZone = StringUtil.removeAlphabeticCharacters(timeZone);
        return formattedTimeZone.equals(StringUtil.EMPTY_STRING) ? ZoneOffset.UTC : ZoneOffset.of(formattedTimeZone);
    }

    public OffsetDateTime getNextWeekday(DayOfWeek weekday, ZoneOffset offset) {
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



