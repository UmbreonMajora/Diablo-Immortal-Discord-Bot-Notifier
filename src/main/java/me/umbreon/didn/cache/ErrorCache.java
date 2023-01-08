package me.umbreon.didn.cache;

import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.database.DatabaseRequests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ErrorCache {

    private final DatabaseRequests databaseRequests;

    private final Logger logger = LoggerFactory.getLogger(ErrorCache.class);
    /**
     * Key is the channel ID.
     * Value is how many errors occurred.
     */
    private final HashMap<String, Integer> channelErrorCountMap = new HashMap<>();
    private final int maximumErrors = 50;

    public ErrorCache(DatabaseRequests databaseRequests) {
        this.databaseRequests = databaseRequests;
    }

    public void addChannelError(NotificationChannel channel) {
        String channelID = channel.getTextChannelID();
        if (!doCacheContainsChannel(channelID)) {
            addChannelToErrorCache(channelID);
        } else {
            addNewErrorForChannelToErrorCache(channelID);
        }

        if (isMaximumAnErrorsReachedForChannel(channelID)) {
            databaseRequests.deleteNotificationChannelByID(channelID);
            logger.info(channelID + " reached the maximum of errors occurred an was removed from the system.");
        }
    }

    private boolean doCacheContainsChannel(String channelID) {
        return channelErrorCountMap.containsKey(channelID);
    }

    private void addChannelToErrorCache(String channelID) {
        channelErrorCountMap.put(channelID, 1);
    }

    private void addNewErrorForChannelToErrorCache(String channelID) {
        int errorCount = channelErrorCountMap.get(channelID);
        channelErrorCountMap.replace(channelID, errorCount + 1);
    }

    private boolean isMaximumAnErrorsReachedForChannel(String channelID) {
        int errorCount = channelErrorCountMap.get(channelID);
        return errorCount >= maximumErrors;
    }
}
