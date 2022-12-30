package me.umbreon.didn.commands.info;

import me.umbreon.didn.cache.GameEventCache;
import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.EventGameData;
import me.umbreon.didn.enums.GameEvent;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.TimeUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Umbreon Majora
 * Send's the user a message with today's events.
 * Command: /today
 */
public class TodayCommand {
/* implements IClientCommand
    private final GuildsCache guildsCache;

    public TodayCommand(GuildsCache guildsCache) {
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        String timeZone = guildsCache.getGuildTimeZone(guildID);
        String weekday = TimeUtil.getCurrentWeekday(timeZone);
        Language language = guildsCache.getGuildLanguage(guildID);
        replyEphemeralToUser(event, buildTodayEventMessage(weekday, language));
    }

    private String buildTodayEventMessage(String weekday, Language language) {
        StringBuilder stringBuilder = new StringBuilder();
        appendEventToString(getTodayEvents(weekday, gameEventCache.getListWithDemonGatesTimes(), false), stringBuilder, LanguageController.getMessage(language, "DEMON-GATES"));
        appendEventToString(getTodayEvents(weekday, gameEventCache.getListWithShadowLotteryTimes(), false), stringBuilder, LanguageController.getMessage(language, "SHADOW-LOTTERY"));
        appendEventToString(getTodayEvents(weekday, gameEventCache.getListWithHauntedCarriageTimes(), false), stringBuilder, LanguageController.getMessage(language, "HAUNTED-CARRIAGE"));
        appendEventToString(getTodayEvents(weekday, gameEventCache.getListWithBattlegroundTimes(), true), stringBuilder, LanguageController.getMessage(language, "BATTLEGROUND"));
        appendEventToString(getTodayEvents(weekday, gameEventCache.getListWithAssemblyTimes(), false), stringBuilder, LanguageController.getMessage(language, "ASSEMBLY"));
        appendEventToString(getTodayEvents(weekday, gameEventCache.getListWithAncientNightmareTimes(), false), stringBuilder, LanguageController.getMessage(language, "ANCIENT-NIGHTMARE"));
        appendEventToString(getTodayEvents(weekday, gameEventCache.getListWithAncientAreaTimes(), false), stringBuilder, LanguageController.getMessage(language, "ANCIENT-ARENA"));
        appendEventToString(getTodayEvents(weekday, gameEventCache.getListWithWrathborneInvasionTimes(), true), stringBuilder, LanguageController.getMessage(language, "WRATHBORNE-INVASION"));
        appendEventToString(getTodayEvents(weekday, gameEventCache.getListWithVaultTimes(), false), stringBuilder, LanguageController.getMessage(language, "VAULT"));
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

    private List<String> getTodayEvents(String weekday, Set<EventGameData> eventList, boolean everyday) {
        List<String> result = new ArrayList<>();


        eventList.forEach(eventGameData -> {
            if (eventGameData.)
        });

        eventList.forEach((time, isHeadUp) -> {
            if (!isHeadUp) {
                if (everyday) {
                    result.add(time);
                } else {
                    String[] tmp = time.split(" ");
                    if (tmp[0].equalsIgnoreCase(weekday)) {
                        result.add(tmp[1]);
                    }
                }
            }
        });

        return result;
    }
*/
}
