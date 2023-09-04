package net.purplegoose.didnb.notifier;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.EventTime;
import net.purplegoose.didnb.data.ScheduledEventsSetting;
import net.purplegoose.didnb.enums.GameEvent;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.exeption.ScheduledEventCreationException;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;
import net.purplegoose.didnb.utils.TimeUtil;

import java.time.*;
import java.util.Timer;
import java.util.TimerTask;

@AllArgsConstructor
public class ScheduledEventCreator {

    private final GuildsCache guildsCache;
    private final GameDataCache gameDataCache;

    public void scheduler(JDA jda) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                guildsCache.getAllGuilds().forEach((guildID, clientGuild) -> {
                    Guild guild = jda.getGuildById(guildID);
                    ClientGuild cGuild = guildsCache.getClientGuildByID(guildID);
                    if (guild != null && cGuild != null) {
                        for (EventTime eventTime : gameDataCache.getGameDataCache()) {
                            if (isScheduledEventEnabled(clientGuild, eventTime.getEventName())) {
                                createScheduledEvent(guild, eventTime, cGuild);
                            }
                        }
                    }
                });
            }
        }, TimeUtil.getNextFullMinute(), 300 * 1000);
    }

    private boolean isScheduledEventEnabled(ClientGuild clientGuild, GameEvent gameEvent) {
        ScheduledEventsSetting seSetting = clientGuild.getSeSetting();
        switch (gameEvent) {
            case VAULT -> {
                return seSetting.isVaultEnabled();
            }
            case ASSEMBLY -> {
                return seSetting.isAssemblyEnabled();
            }
            case ON_SLAUGHT -> {
                return seSetting.isStormpointEnabled();
            }
            case SHADOW_WAR -> {
                return seSetting.isShadowWarEnabled();
            }
            case BATTLEGROUNDS -> {
                return seSetting.isBattlegroundsEnabled();
            }
            case DEMON_GATES -> {
                return seSetting.isDemonGatesEnabled();
            }
            case ANCIENT_ARENA -> {
                return seSetting.isAncientArenaEnabled();
            }
            case SHADOW_LOTTERY -> {
                return seSetting.isShadowLotteryEnabled();
            }
            case HAUNTED_CARRIAGE -> {
                return seSetting.isHauntedCarriageEnabled();
            }
            case TOWER_OF_VICTORY -> {
                return seSetting.isAccursedTowerEnabled();
            }
            case ANCIENT_NIGHTMARE -> {
                return seSetting.isAncientNightmareEnabled();
            }
            case WRATHBORNE_INVASION -> {
                return seSetting.isWrathborneInvasionEnabled();
            }
            default -> throw new ScheduledEventCreationException(String.format("Failed to find boolean for %s.", gameEvent.rawName), null);
        }
    }

    public void createScheduledEvent(Guild guild, EventTime eventTime, ClientGuild clientGuild) {
        DayOfWeek weekday = DayOfWeek.valueOf(String.valueOf(eventTime.getWeekday()));
        String timeZone = clientGuild.getTimeZone();
        Language language = clientGuild.getLanguage();
        ZoneOffset offset = getZoneOffsetByTimeZone(timeZone);
        OffsetDateTime nextWeekday = getNextWeekday(weekday, offset);
        OffsetDateTime startTime = getOffsetDateTimeWithHourAndMinute(nextWeekday, eventTime.getStartHour(), eventTime.getStartMinute());
        OffsetDateTime endTime = getOffsetDateTimeWithHourAndMinute(nextWeekday, eventTime.getEndHour(), eventTime.getEndMinute());
        String displayName = LanguageController.getMessage(language, eventTime.getEventName().languageKey);
        if (doScheduledEventExists(guild, startTime, displayName)) return;
        guild.createScheduledEvent(displayName, "EVENT-LOCATION", startTime, endTime).queue();
    }

    private boolean doScheduledEventExists(Guild guild, OffsetDateTime startTime, String displayName) {
        for (ScheduledEvent scheduledEvent : guild.getScheduledEvents()) {
            if (scheduledEvent.getName().equalsIgnoreCase(displayName) && scheduledEvent.getStartTime().equals(startTime)) {
                return true;
            }
        }
        return false;
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



