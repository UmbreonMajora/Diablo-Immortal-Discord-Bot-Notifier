package net.purplegoose.didnb.commands.info;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.EventGameData;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.purplegoose.didnb.utils.StringUtil.FORMATTED_MESSAGE;
import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * <p>
 * Send's the user a message with today's events.
 * <p>
 * Command: /today
 */
@Slf4j
@AllArgsConstructor
public class TodayCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final GameDataCache gameDataCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        String timeZone = guildsCache.getGuildTimeZone(guildID);
        String weekday = TimeUtil.getCurrentWeekday(timeZone);
        Language language = guildsCache.getGuildLanguage(guildID);

        log.info("{} used /today. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(), logInfo.getChannelID());
        replyEphemeralToUser(event, buildTodayEventMessage(weekday, language));
    }

    private String buildTodayEventMessage(String weekday, Language language) {
        StringBuilder stringBuilder = new StringBuilder(100);
        stringBuilder.append(FORMATTED_MESSAGE).append(NEW_LINE);
        appendEventToString(getTodayEvents(weekday, gameDataCache.getDemonGatesDataSet()), stringBuilder, LanguageController.getMessage(language, "DEMON-GATES"));
        appendEventToString(getTodayEvents(weekday, gameDataCache.getShadowLotteryDataSet()), stringBuilder, LanguageController.getMessage(language, "SHADOW-LOTTERY"));
        appendEventToString(getTodayEvents(weekday, gameDataCache.getHauntedCarriageDataSet()), stringBuilder, LanguageController.getMessage(language, "HAUNTED-CARRIAGE"));
        appendEventToString(getTodayEvents(weekday, gameDataCache.getBattlegroundDataSet()), stringBuilder, LanguageController.getMessage(language, "BATTLEGROUND"));
        appendEventToString(getTodayEvents(weekday, gameDataCache.getAssemblyDataSet()), stringBuilder, LanguageController.getMessage(language, "ASSEMBLY"));
        appendEventToString(getTodayEvents(weekday, gameDataCache.getAncientNightmareDataSet()), stringBuilder, LanguageController.getMessage(language, "ANCIENT-NIGHTMARE"));
        appendEventToString(getTodayEvents(weekday, gameDataCache.getAncientArenaDataSet()), stringBuilder, LanguageController.getMessage(language, "ANCIENT-ARENA"));
        appendEventToString(getTodayEvents(weekday, gameDataCache.getWrathborneInvasionDataSet()), stringBuilder, LanguageController.getMessage(language, "WRATHBORNE-INVASION"));
        appendEventToString(getTodayEvents(weekday, gameDataCache.getVaultGameDataSet()), stringBuilder, LanguageController.getMessage(language, "VAULT"));
        appendEventToString(getTodayEvents(weekday, gameDataCache.getOnSlaughtDataSet()), stringBuilder, LanguageController.getMessage(language, "ON-SLAUGHT"));
        stringBuilder.append(FORMATTED_MESSAGE);
        return stringBuilder.toString();
    }

    private void appendEventToString(List<String> listWithTodayEvents, StringBuilder stringBuilder, String header) {
        boolean addedHeader = false;
        for (String s : listWithTodayEvents) {
            if (!addedHeader) {
                stringBuilder.append("\n").append(header).append(":\n");
                addedHeader = true;
            }
            stringBuilder.append(s).append("\n");
        }
    }

    private List<String> getTodayEvents(String weekday, Set<EventGameData> eventList) {
        List<String> result = new ArrayList<>();

        for (EventGameData eventGameData : eventList) {
            if (eventGameData.getWeekday() == null || eventGameData.getWeekday().equals(weekday)) {
                result.add(eventGameData.getEventStartTime());
            }
        }

        return result;
    }

}
