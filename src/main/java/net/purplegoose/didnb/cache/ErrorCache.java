package net.purplegoose.didnb.cache;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.database.DatabaseRequests;

import java.util.HashMap;

@Slf4j
@AllArgsConstructor
public class ErrorCache {

    private final DatabaseRequests databaseRequests;

    /**
     * Key is the channel ID.
     * Value is how many errors occurred.
     */
    private final HashMap<String, Integer> channelErrorCountMap = new HashMap<>();
    private final int maximumErrors = 50;

    public void addChannelError(NotificationChannel channel) {
        String channelID = channel.getTextChannelID();
        if (!doCacheContainsChannel(channelID)) {
            addChannelToErrorCache(channelID);
        } else {
            addNewErrorForChannelToErrorCache(channelID);
        }

        if (isMaximumAnErrorsReachedForChannel(channelID)) {
            databaseRequests.deleteNotificationChannelByID(channelID);
            log.info("{} reached the maximum of errors occurred an was removed from the system.", channelID);
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
