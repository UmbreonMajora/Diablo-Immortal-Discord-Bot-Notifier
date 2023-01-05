package me.umbreon.didn.gameevents;

import me.umbreon.didn.cache.GameDataCache;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.data.EventGameData;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.ConfigUtil;
import me.umbreon.didn.utils.TimeUtil;

import java.util.Set;

import static me.umbreon.didn.utils.StringUtil.EMPTY_STRING;
import static me.umbreon.didn.utils.StringUtil.NEW_LINE;
/**
 * @author Umbreon Majora
 * Class for Diablo Immortal Shadow Lottery event.
 */
public class ShadowLotteryEvent implements IGameEvent {

    private final GameDataCache gameDataCache;

    public ShadowLotteryEvent(GameDataCache gameDataCache) {
        this.gameDataCache = gameDataCache;
    }

    public String appendShadowLotteryNotificationIfHappening(ClientGuild guild, NotificationChannel channel) {
        if (!channel.isShadowLotteryMessageEnabled()) {
            return EMPTY_STRING;
        }

        Set<EventGameData> eventGameDataSet = gameDataCache.getShadowLotteryDataSet();
        String timeZone = guild.getGuildTimeZone();
        Language language = guild.getGuildLanguage();

        if (!isTimeInWarnRange(eventGameDataSet, timeZone).equals(EMPTY_STRING)) {
            String startTime = isTimeInWarnRange(eventGameDataSet, timeZone);
            int warnTime = ConfigUtil.getDefaultWarnTime() - guild.getHeadUpTime();
            String warnMessageTime = getWarnMessageTime(startTime, warnTime);
            if (TimeUtil.getTime(timeZone).equals(warnMessageTime) && isWarnMessageEnabled(guild, channel)) {
                return String.format(LanguageController.getMessage(language, "EVENT-SHADOW-LOTTERY-HEAD-UP"), guild.getHeadUpTime()) + NEW_LINE;
            }
        }

        if (isEventStarting(eventGameDataSet, timeZone) && isEventMessageEnabled(guild, channel)) {
            return LanguageController.getMessage(language, "EVENT-SHADOW-LOTTERY") + NEW_LINE;
        }

        return EMPTY_STRING;
    }
}