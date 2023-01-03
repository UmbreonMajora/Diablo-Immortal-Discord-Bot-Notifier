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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static me.umbreon.didn.utils.StringUtil.FORMATTED_MESSAGE;
import static me.umbreon.didn.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * Send's the user a message with today's events.
 * Command: /today
 */
public class TodayCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(TodayCommand.class);

    private final GuildsCache guildsCache;
    private final GameDataCache gameDataCache;

    public TodayCommand(GuildsCache guildsCache, GameDataCache gameDataCache) {
        this.guildsCache = guildsCache;
        this.gameDataCache = gameDataCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        String timeZone = guildsCache.getGuildTimeZone(guildID);
        String weekday = TimeUtil.getCurrentWeekday(timeZone);
        Language language = guildsCache.getGuildLanguage(guildID);

        LOGGER.info(getFullUsernameWithDiscriminator(event.getUser()) + " used " + event.getCommandString() + " on " +
                event.getChannel().getId() + " at " + event.getGuild().getId());

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
