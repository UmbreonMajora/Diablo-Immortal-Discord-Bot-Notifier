package me.umbreon.didn.commands.info;

import me.umbreon.didn.cache.GameDataCache;
import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.EventGameData;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.TimeUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static me.umbreon.didn.utils.StringUtil.FORMATTED_MESSAGE;
import static me.umbreon.didn.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * Send's the user a message with upcoming events.
 * Command: /upcoming
 */
public class UpComingCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(UpComingCommand.class);

    private final GuildsCache guildsCache;
    private final GameDataCache gameDataCache;

    public UpComingCommand(GuildsCache guildsCache, GameDataCache gameDataCache) {
        this.guildsCache = guildsCache;
        this.gameDataCache = gameDataCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        String timeZone = guildsCache.getGuildTimeZone(guildID);
        Language language = guildsCache.getGuildLanguage(guildID);

        LOGGER.info(getFullUsernameWithDiscriminator(event.getUser()) + " used " + event.getCommandString() + " on " +
                event.getChannel().getId() + " at " + event.getGuild().getId());

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
