package net.purplegoose.didnb.data;

import lombok.Getter;
import lombok.Setter;
import net.purplegoose.didnb.enums.Language;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientGuild {

    @Getter
    private final String guildID;
    @Getter
    @Setter
    private Language guildLanguage;
    @Getter
    @Setter
    private String guildTimeZone;
    @Getter
    @Setter
    private String guildAdminRoleID;
    @Getter
    @Setter
    private int warnTimeInMinutes;
    @Getter
    @Setter
    private boolean isWarnMessagesEnabled;
    @Getter
    @Setter
    private boolean isEventMessageEnabled;
    @Getter
    @Setter
    private boolean isDaylightTimeEnabled;
    @Getter
    @Setter
    private boolean isPremiumServer;
    private final Map<Integer, CustomNotification> customNotifications;
    private final Map<String, NotificationChannel> notificationChannels;

    public ClientGuild(String guildID, Language guildLanguage, String guildTimeZone, String guildAdminRoleID,
                       int warnTimeInMinutes, boolean isWarnMessagesEnabled, boolean isEventMessageEnabled,
                       Map<Integer, CustomNotification> customNotifications,
                       Map<String, NotificationChannel> notificationChannels,
                       boolean isDaylightTimeEnabled, boolean isPremiumServer) {
        this.guildID = guildID;
        this.guildLanguage = guildLanguage;
        this.guildTimeZone = guildTimeZone;
        this.guildAdminRoleID = guildAdminRoleID;
        this.warnTimeInMinutes = warnTimeInMinutes;
        this.isWarnMessagesEnabled = isWarnMessagesEnabled;
        this.isEventMessageEnabled = isEventMessageEnabled;
        this.customNotifications = customNotifications;
        this.notificationChannels = notificationChannels;
        this.isDaylightTimeEnabled = isDaylightTimeEnabled;
        this.isPremiumServer = isPremiumServer;
    }

    public ClientGuild(String guildID, Language guildLanguage, String guildTimeZone, String guildAdminRoleID,
                       int warnTimeInMinutes, boolean isEventMessageEnabled, boolean isWarnMessagesEnabled,
                       boolean isDaylightTimeEnabled, boolean isPremiumServer) {
        this.guildID = guildID;
        this.guildLanguage = guildLanguage;
        this.guildTimeZone = guildTimeZone;
        this.guildAdminRoleID = guildAdminRoleID;
        this.warnTimeInMinutes = warnTimeInMinutes;
        this.isWarnMessagesEnabled = isEventMessageEnabled;
        this.isEventMessageEnabled = isWarnMessagesEnabled;
        this.customNotifications = new ConcurrentHashMap<>();
        this.notificationChannels = new ConcurrentHashMap<>();
        this.isDaylightTimeEnabled = isDaylightTimeEnabled;
        this.isPremiumServer = isPremiumServer;
    }

    public ClientGuild(String guildID) {
        this.guildID = guildID;
        this.guildLanguage = Language.ENGLISH;
        this.guildTimeZone = "GMT";
        this.guildAdminRoleID = null;
        this.warnTimeInMinutes = 15;
        this.isWarnMessagesEnabled = true;
        this.isEventMessageEnabled = true;
        this.customNotifications = new ConcurrentHashMap<>();
        this.notificationChannels = new ConcurrentHashMap<>();
        this.isDaylightTimeEnabled = false;
        this.isPremiumServer = false;
    }

    public Map<Integer, CustomNotification> getCustomNotifications() {
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

    public CustomNotification getCustomNotificationByID(int customNotificationID) {
        return customNotifications.get(customNotificationID);
    }

    public void deleteCustomNotificationByID(int customNotificationID) {
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

}
