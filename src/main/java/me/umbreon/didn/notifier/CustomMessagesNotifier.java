package me.umbreon.didn.notifier;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.utils.TimeUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CustomMessagesNotifier {

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public CustomMessagesNotifier(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    public void runCustomMessagesNotifier(JDA jda) {
        Date date = Calendar.getInstance().getTime();
        date.setMinutes(date.getMinutes() + 1);
        date.setSeconds(0);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                guildsCache.getAllGuilds().forEach((guildID, clientGuild) -> {
                    clientGuild.getCustomNotifications().forEach((integer, customNotification) -> {

                        if (!customNotification.isEnabled()) {
                            return;
                        }

                        String channel = customNotification.getChannelID();
                        String timezone = guildsCache.getGuildTimeZone(guildID);
                        String day = customNotification.getWeekday();
                        String time = customNotification.getTime();
                        String fullTime = day + " " + time;

                        if (day.equalsIgnoreCase("everyday")) {
                            if (!isTimeValid(timezone, time)) return; //Invalid time message
                        } else {
                            if (!isFulltimeValid(timezone, fullTime)) return; //Invalid time message
                        }

                        TextChannel textChannel;

                        try {
                            textChannel = jda.getTextChannelById(channel);
                        } catch (NullPointerException e) {
                            return;
                        }

                        String message = customNotification.getMessage();

                        if (textChannel != null) {
                            textChannel.sendMessage(message).queue();
                        }

                        if (!customNotification.isRepeating()) {
                            clientGuild.getCustomNotifications().remove(integer);
                            databaseRequests.deleteCustomNotificationByID(customNotification.getCustomMessageID());
                        }
                    });
                });
            }
        }, date, 60 * 1000);
    }

    private boolean isFulltimeValid(String timezone, String fullTime) {
        String time = TimeUtil.getTimeWithWeekday(timezone);
        return time.equalsIgnoreCase(fullTime);
    }

    private boolean isTimeValid(String timezone, String time) {
        String timeOnly = TimeUtil.getTime(timezone);
        return time.equalsIgnoreCase(timeOnly);
    }
}
