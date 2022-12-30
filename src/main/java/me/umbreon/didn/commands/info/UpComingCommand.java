package me.umbreon.didn.commands.info;

import me.umbreon.didn.cache.GameEventCache;
import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.TimeUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Umbreon Majora
 * Send's the user a message with upcoming events.
 * Command: /upcoming
 */
public class UpComingCommand {
/* implements IClientCommand
    private final GuildsCache guildsCache;
    private final GameEventCache gameEventCache;

    public UpComingCommand(GuildsCache guildsCache, GameEventCache gameEventCache) {
        this.guildsCache = guildsCache;
        this.gameEventCache = gameEventCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        String timeZone = guildsCache.getGuildTimeZone(guildID);
        String weekday = TimeUtil.getCurrentWeekday(timeZone);
        Language language = guildsCache.getGuildLanguage(guildID);
        replyEphemeralToUser(event, buildTodayEventMessage(weekday, timeZone, language));
    }

    private String buildTodayEventMessage(String weekday, String timeZone, Language language) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> upComingEventList;
        upComingEventList = getTodayUpComingEvents(weekday, timeZone, gameEventCache.getListWithDemonGatesTimes(), false);
        System.out.println(upComingEventList);
        buildEventInfoMessage(upComingEventList, stringBuilder, LanguageController.getMessage(language, "DEMON-GATES"));
        buildEventInfoMessage(getTodayUpComingEvents(weekday, timeZone, gameEventCache.getListWithShadowLotteryTimes(), false), stringBuilder, LanguageController.getMessage(language, "SHADOW-LOTTERY"));
        buildEventInfoMessage(getTodayUpComingEvents(weekday, timeZone, gameEventCache.getListWithHauntedCarriageTimes(), false), stringBuilder, LanguageController.getMessage(language, "HAUNTED-CARRIAGE"));
        buildEventInfoMessage(getTodayUpComingEvents(weekday, timeZone, gameEventCache.getListWithBattlegroundTimes(), true), stringBuilder, LanguageController.getMessage(language, "BATTLEGROUND"));
        buildEventInfoMessage(getTodayUpComingEvents(weekday, timeZone, gameEventCache.getListWithAssemblyTimes(), false), stringBuilder, LanguageController.getMessage(language, "ASSEMBLY"));
        buildEventInfoMessage(getTodayUpComingEvents(weekday, timeZone, gameEventCache.getListWithAncientNightmareTimes(), false), stringBuilder, LanguageController.getMessage(language, "ANCIENT-NIGHTMARE"));
        buildEventInfoMessage(getTodayUpComingEvents(weekday, timeZone, gameEventCache.getListWithAncientAreaTimes(), false), stringBuilder, LanguageController.getMessage(language, "ANCIENT-ARENA"));
        buildEventInfoMessage(getTodayUpComingEvents(weekday, timeZone, gameEventCache.getListWithWrathborneInvasionTimes(), true), stringBuilder, LanguageController.getMessage(language, "WRATHBORNE-INVASION"));
        buildEventInfoMessage(getTodayUpComingEvents(weekday, timeZone, gameEventCache.getListWithVaultTimes(), false), stringBuilder, LanguageController.getMessage(language, "VAULT"));
        return stringBuilder.toString();
    }

    private List<String> getTodayUpComingEvents(String weekday, String timeZone, Map<String, Boolean> eventList, boolean everyday) {
        List<String> result = new ArrayList<>();

        eventList.forEach((time, isHeadUp) -> {
            try {
                if (!isHeadUp) {
                    if (everyday) {
                        Date eventTime = TimeUtil.getTimeAsCalendar(time).getTime();
                        Date nowTime = TimeUtil.getTimeAsCalendar(TimeUtil.getTime(timeZone)).getTime();
                        if (eventTime.after(nowTime)) {
                            result.add(time);
                        }
                    } else {
                        if (time.toLowerCase().contains(weekday.toLowerCase())) {
                            String[] tmp = time.split(" ");
                            Date eventTime = TimeUtil.getTimeAsCalendar(tmp[1]).getTime();
                            Date nowTime = TimeUtil.getTimeAsCalendar(TimeUtil.getTime(timeZone)).getTime();
                            if (eventTime.after(nowTime)) {
                                result.add(tmp[1]);
                            }
                        }
                    }
                }
            } catch (ParseException ignored) {
            }
        });

        return result;
    }

    private void buildEventInfoMessage(List<String> upComingEventList, StringBuilder stringBuilder, String header) {
        boolean addedHeader = false;
        for (String s : upComingEventList) {
            if (!addedHeader) {
                stringBuilder.append("\n").append(header).append(":\n");
                addedHeader = true;
            }
            stringBuilder.append(s).append("\n");
        }
    }
 */
}
