package net.purplegoose.didnb.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.news.dto.NewsChannelDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class ClientGuild {

    @Setter(value = AccessLevel.NONE)
    private final String guildID;
    private final Map<String, CustomNotification> customNotifications;
    private final Map<String, NotificationChannel> notificationChannels;
    private Language language;
    private String timeZone;
    private String guildAdminRoleID;
    private int warnTimeInMinutes;
    private boolean isWarnMessagesEnabled;
    private boolean isEventMessageEnabled;
    private boolean isDaylightTimeEnabled;
    private boolean isPremiumServer;
    private boolean isAutoDeleteEnabled;
    private int autoDeleteTimeInHours;
    private long embedLeadTime;
    private ScheduledEventsSetting seSetting;
    private Map<String, NewsChannelDTO> newsChannels;

    public ClientGuild(String guildID, Language language, String timeZone, String guildAdminRoleID,
                       int warnTimeInMinutes, boolean isEventMessageEnabled, boolean isWarnMessagesEnabled,
                       boolean isDaylightTimeEnabled, boolean isPremiumServer, int autoDeleteTimeInHours,
                       boolean isAutoDeleteEnabled, long embedLeadTime, ScheduledEventsSetting seSetting) {
        this.guildID = guildID;
        this.language = language;
        this.timeZone = timeZone;
        this.guildAdminRoleID = guildAdminRoleID;
        this.warnTimeInMinutes = warnTimeInMinutes;
        this.isWarnMessagesEnabled = isEventMessageEnabled;
        this.isEventMessageEnabled = isWarnMessagesEnabled;
        this.customNotifications = new ConcurrentHashMap<>();
        this.notificationChannels = new ConcurrentHashMap<>();
        this.newsChannels = new ConcurrentHashMap<>();
        this.isDaylightTimeEnabled = isDaylightTimeEnabled;
        this.isPremiumServer = isPremiumServer;
        this.autoDeleteTimeInHours = autoDeleteTimeInHours;
        this.isAutoDeleteEnabled = isAutoDeleteEnabled;
        this.embedLeadTime = embedLeadTime;
        this.seSetting = seSetting;
    }

    public ClientGuild(String guildID) {
        this.guildID = guildID;
        this.language = Language.ENGLISH;
        this.timeZone = "GMT";
        this.guildAdminRoleID = null;
        this.warnTimeInMinutes = 15;
        this.isWarnMessagesEnabled = true;
        this.isEventMessageEnabled = true;
        this.customNotifications = new ConcurrentHashMap<>();
        this.notificationChannels = new ConcurrentHashMap<>();
        this.newsChannels = new ConcurrentHashMap<>();
        this.isDaylightTimeEnabled = false;
        this.isPremiumServer = false;
        this.autoDeleteTimeInHours = 1;
        this.isAutoDeleteEnabled = false;
        this.embedLeadTime = 1;
        this.seSetting = new ScheduledEventsSetting(guildID);
    }

    public Map<String, CustomNotification> getCustomNotifications() {
        return customNotifications;
    }

    public boolean isChannelRegistered(String textChannelID) {
        return notificationChannels.containsKey(textChannelID);
    }

    public NotificationChannel getNotificationChannel(String textChannelID) {
        return notificationChannels.get(textChannelID);
    }

    public void addNewNotificationChannel(NotificationChannel notificationChannel) {
        notificationChannels.put(notificationChannel.getTextChannelID(), notificationChannel);
    }

    public void deleteNotificationChannelByID(String textChannelID) {
        notificationChannels.remove(textChannelID);
    }

    public void addCustomNotification(CustomNotification customNotification) {
        customNotifications.put(customNotification.getCustomMessageID(), customNotification);
    }

    public CustomNotification getCustomNotificationByID(String customNotificationID) {
        return customNotifications.get(customNotificationID);
    }

    public void deleteCustomNotificationByID(String customNotificationID) {
        customNotifications.remove(customNotificationID);
    }

    public boolean doNotificationChannelExist(String textChannelID) {
        return notificationChannels.containsKey(textChannelID);
    }

    public int getNotificationChannelCount() {
        return notificationChannels.size();
    }

    public Collection<NotificationChannel> getAllNotificationChannels() {
        return notificationChannels.values();
    }

    public int getCustomNotificationSize() {
        return customNotifications.size();
    }

    // custom messages

    public boolean doCustomMessageExists(String customMessageID) {
        return customNotifications.containsKey(customMessageID);
    }

    /**
     * Adds a news channel to a client guild.
     * @param newsChannelDTO news channel object with information about the discord channel.
     */
    public void addNewsChannel(NewsChannelDTO newsChannelDTO) {
        newsChannels.put(newsChannelDTO.getChannelID(), newsChannelDTO);
    }

    /**
     * Removed a news channel from a client guild.
     * @param channelID The discord channel id of the registered news channel.
     */
    public void deleteNewsChannel(String channelID) {
        newsChannels.remove(channelID);
    }

    /**
     * Gets a news channel by the discord channel id.
     * @param channelID The discord channel id of the registered news channel.
     * @return the news channel with the associated channel id or throws NullPointerException.
     */
    public NewsChannelDTO getNewsChannelByChannelID(String channelID) {
        return newsChannels.get(channelID);
    }

    /**
     * Updates a news channel of a client guild.
     * @param newsChannelDTO The updated news channel object.
     */
    public void replaceNewsChannel(NewsChannelDTO newsChannelDTO) {
        newsChannels.replace(newsChannelDTO.getChannelID(), newsChannelDTO);
    }
}
