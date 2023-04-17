package net.purplegoose.didnb.commands.info;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.EventGameData;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.TimeUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.*;

import static net.purplegoose.didnb.utils.StringUtil.FORMATTED_MESSAGE;
import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * <p>
 * Send's the user a message with upcoming events.
 * <p>
 * Command: /upcoming
 */
@Slf4j
@AllArgsConstructor
public class UpComingCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final GameDataCache gameDataCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        String timeZone = guildsCache.getGuildTimeZone(guildID);
        Language language = guildsCache.getGuildLanguage(guildID);

        log.info("{} used /upcoming. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(), logInfo.getChannelID());

        replyEphemeralToUser(event, buildTodayEventMessage(timeZone, language));
    }

    private String buildTodayEventMessage(String timeZone, Language language) {
        StringBuilder stringBuilder = new StringBuilder(100);
        stringBuilder.append(FORMATTED_MESSAGE).append(NEW_LINE);
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getDemonGatesDataSet()), stringBuilder, LanguageController.getMessage(language, "DEMON-GATES"));
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getShadowLotteryDataSet()), stringBuilder, LanguageController.getMessage(language, "SHADOW-LOTTERY"));
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getHauntedCarriageDataSet()), stringBuilder, LanguageController.getMessage(language, "HAUNTED-CARRIAGE"));
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getBattlegroundDataSet()), stringBuilder, LanguageController.getMessage(language, "BATTLEGROUND"));
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getAssemblyDataSet()), stringBuilder, LanguageController.getMessage(language, "ASSEMBLY"));
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getAncientNightmareDataSet()), stringBuilder, LanguageController.getMessage(language, "ANCIENT-NIGHTMARE"));
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getAncientArenaDataSet()), stringBuilder, LanguageController.getMessage(language, "ANCIENT-ARENA"));
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getWrathborneInvasionDataSet()), stringBuilder, LanguageController.getMessage(language, "WRATHBORNE-INVASION"));
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getVaultGameDataSet()), stringBuilder, LanguageController.getMessage(language, "VAULT"));
        buildEventInfoMessage(getTodayUpComingEvents(timeZone, gameDataCache.getOnSlaughtDataSet()), stringBuilder, LanguageController.getMessage(language, "ON-SLAUGHT"));
        stringBuilder.append(FORMATTED_MESSAGE);
        return stringBuilder.toString();
    }

    private List<String> getTodayUpComingEvents(String timeZone, Set<EventGameData> eventList) {
        List<String> result = new ArrayList<>();
        String weekday = TimeUtil.getCurrentWeekday(timeZone);

        for (EventGameData eventGameData : eventList) {
            if (eventGameData.getWeekday() == null || eventGameData.getWeekday().equalsIgnoreCase(weekday)) {
                Date eventStartDate = TimeUtil.getTimeAsCalendar(eventGameData.getEventStartTime());
                Date nowDate = TimeUtil.getTimeAsCalendar(TimeUtil.getTime(timeZone));
                if (eventStartDate != null && eventStartDate.after(nowDate)) {
                    result.add(eventGameData.getEventStartTime());
                }
            }
        }

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
}
