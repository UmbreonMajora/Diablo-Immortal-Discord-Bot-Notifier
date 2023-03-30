package net.purplegoose.didnb.gameevents;

import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.EventGameData;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.ConfigUtil;
import net.purplegoose.didnb.utils.TimeUtil;

import java.util.Set;

import static net.purplegoose.didnb.utils.StringUtil.EMPTY_STRING;
import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;
/**
 * @author Umbreon Majora
 * Class for Diablo Immortal Ancient Arena event.
 */
public class AncientArenaEvent implements IGameEvent {

    private final GameDataCache gameDataCache;

    public AncientArenaEvent(GameDataCache gameDataCache) {
        this.gameDataCache = gameDataCache;
    }

    public String appendAncientArenaNotificationIfHappening(ClientGuild guild, NotificationChannel channel) {
        if (!channel.isAncientArenaMessageEnabled()) {
            return EMPTY_STRING;
        }

        Set<EventGameData> eventGameDataSet = gameDataCache.getAncientArenaDataSet();
        String timeZone = guild.getGuildTimeZone();
        Language language = guild.getGuildLanguage();

        if (!isTimeInWarnRange(eventGameDataSet, timeZone).equals(EMPTY_STRING)) {

            String startTime = isTimeInWarnRange(eventGameDataSet, timeZone);
            int warnTime = ConfigUtil.getDefaultWarnTime() - guild.getWarnTimeInMinutes();
            String warnMessageTime = getWarnMessageTime(startTime, warnTime);

            if (TimeUtil.getTime(timeZone).equals(warnMessageTime) && isWarnMessageEnabled(guild, channel)) {
                return String.format(LanguageController.getMessage(language, "EVENT-ANCIENT-ARENA-HEAD-UP"), guild.getWarnTimeInMinutes()) + NEW_LINE;
            }
        }

        if (isEventStarting(eventGameDataSet, timeZone) && isEventMessageEnabled(guild, channel)) {
            return LanguageController.getMessage(language, "EVENT-ANCIENT-ARENA") + NEW_LINE;
        }

        return EMPTY_STRING;
    }
}
