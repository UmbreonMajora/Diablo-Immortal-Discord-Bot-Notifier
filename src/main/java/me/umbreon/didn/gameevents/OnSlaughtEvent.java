package me.umbreon.didn.gameevents;

import me.umbreon.didn.cache.GameDataCache;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.data.EventGameData;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.TimeUtil;

import java.util.Set;

import static me.umbreon.didn.utils.StringUtil.EMPTY_STRING;
import static me.umbreon.didn.utils.StringUtil.NEW_LINE;

public class OnSlaughtEvent implements IGameEvent {

    private final GameDataCache gameDataCache;

    public OnSlaughtEvent(GameDataCache gameDataCache) {
        this.gameDataCache = gameDataCache;
    }

    public String appendOnSlaughtEventIfHappening(ClientGuild guild, NotificationChannel channel) {
        Set<EventGameData> eventGameDataSet = gameDataCache.getOnSlaughtDataSet();
        String timeZone = guild.getGuildTimeZone();
        Language language = guild.getGuildLanguage();

        if (!isTimeInWarnRange(eventGameDataSet, timeZone).equals(EMPTY_STRING)) {
            String startTime = isTimeInWarnRange(eventGameDataSet, timeZone);
            int warnTime = 15 - guild.getHeadUpTime();
            String warnMessageTime = getWarnMessageTime(startTime, warnTime);
            if (TimeUtil.getTime(timeZone).equals(warnMessageTime) && isWarnMessageEnabled(guild, channel)) {
                return String.format(LanguageController.getMessage(language, "EVENT-STORM-POINT-WARN"), warnTime) + NEW_LINE;
            }
        }
        if (isEventStarting(eventGameDataSet, timeZone) && isEventMessageEnabled(guild, channel)) {
            return LanguageController.getMessage(language, "EVENT-STORM-POINT") + NEW_LINE;
        }

        return EMPTY_STRING;
    }

}
