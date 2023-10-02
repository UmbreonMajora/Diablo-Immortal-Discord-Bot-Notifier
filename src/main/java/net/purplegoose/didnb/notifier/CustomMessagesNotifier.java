package net.purplegoose.didnb.notifier;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.exeption.InvalidMentionException;
import net.purplegoose.didnb.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class CustomMessagesNotifier extends NotifierHelper {

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public CustomMessagesNotifier(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    public void runCustomMessagesNotifier(JDA jda) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (ClientGuild clientGuild : guildsCache.getAllGuilds().values()) {
                    for (CustomNotification customNotification : clientGuild.getCustomNotifications().values()) {
                        if (!customNotification.isEnabled()) {
                            continue;
                        }

                        String guildID = clientGuild.getGuildID();
                        String timezone = guildsCache.getGuildTimeZone(guildID);
                        String day = customNotification.getWeekday();
                        String time = customNotification.getTime();
                        String fullTime = day + " " + time;

                        if (!isTimeValid(timezone, day, time, fullTime)) {
                            continue;
                        }

                        String channel = customNotification.getChannelID();
                        TextChannel textChannel = jda.getTextChannelById(channel);
                        String message = customNotification.getMessage();

                        if (textChannel == null) {
                            log.info("{} has a custom notification with an non existent text channel, skipping...",
                                    clientGuild.getGuildID());
                            continue;
                        }

                        // mention
                        NotificationChannel notificationChannel = guildsCache.getClientGuildByID(guildID)
                                .getNotificationChannel(textChannel.getId());
                        StringBuilder sb = new StringBuilder(message);
                        try {
                            addMessageMention(sb, notificationChannel, textChannel.getGuild(), clientGuild.getLanguage());
                        } catch (InvalidMentionException e) {
                            log.error(e.getMessage(), e);
                            continue;
                        }
                        message = sb.toString();

                        if (clientGuild.isAutoDeleteEnabled()) {
                            int autoDeleteTimeInHours = clientGuild.getAutoDeleteTimeInHours();
                            sendTimedMessage(textChannel, message, autoDeleteTimeInHours);
                        } else {
                            sendMessage(textChannel, message);
                        }

                        if (!customNotification.isRepeating()) {
                            clientGuild.getCustomNotifications().remove(customNotification.getCustomMessageID());
                            databaseRequests.deleteCustomNotificationByID(customNotification.getCustomMessageID());
                        }
                    }
                }
            }
        }, TimeUtil.getNextFullMinute(), 60L * 1000L);
    }

    private boolean isTimeValid(String timezone, String day, String time, String fullTime) {
        if (day.equalsIgnoreCase("everyday")) {
            return isTimeValid(timezone, time);
        } else {
            return isFullTimeValid(timezone, fullTime);
        }
    }

    private boolean isFullTimeValid(String timezone, String fullTime) {
        String time = TimeUtil.getTimeWithWeekday(timezone).toLowerCase();
        return time.equalsIgnoreCase(fullTime.toLowerCase());
    }

    private boolean isTimeValid(String timezone, String time) {
        String timeOnly = TimeUtil.getTime(timezone);
        return time.equalsIgnoreCase(timeOnly);
    }
}
