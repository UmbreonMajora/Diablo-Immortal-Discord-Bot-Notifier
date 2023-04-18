package net.purplegoose.didnb.notifier;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CustomMessagesNotifier {

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

                        if ()

                        if (textChannel != null) {
                            textChannel.sendMessage(message).queue();
                        }

                        if (!customNotification.isRepeating()) {
                            clientGuild.getCustomNotifications().remove(customNotification.getCustomMessageID());
                            databaseRequests.deleteCustomNotificationByID(customNotification.getCustomMessageID());
                        }
                    }
                }
            }
        }, TimeUtil.getNextFullMinute(), 60 * 1000);
    }

    private void sendMessageWithAutoDelete(TextChannel textChannel, String messageContext, int deleteTimeInHours) {
        textChannel.sendMessage(messageContext).queue(message ->
                message.delete().queueAfter(deleteTimeInHours, TimeUnit.HOURS));
    }

    private void sendMessageWithoutAutoDelete(TextChannel textChannel, String messageContext) {
        textChannel.sendMessage(messageContext).queue();
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
