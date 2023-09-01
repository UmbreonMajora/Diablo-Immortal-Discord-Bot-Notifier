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
    private Language language;
    @Getter
    @Setter
    private String TimeZone;
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
    @Getter
    @Setter
    private boolean isAutoDeleteEnabled;
    @Getter
    @Setter
    private int autoDeleteTimeInHours;
    @Getter
    @Setter
    private long embedLeadTime;

    private final Map<String, CustomNotification> customNotifications;
    private final Map<String, NotificationChannel> notificationChannels;

    public ClientGuild(String guildID, Language language, String TimeZone, String guildAdminRoleID,
                       int warnTimeInMinutes, boolean isEventMessageEnabled, boolean isWarnMessagesEnabled,
                       boolean isDaylightTimeEnabled, boolean isPremiumServer, int autoDeleteTimeInHours,
                       boolean isAutoDeleteEnabled, long embedLeadTime) {
        this.guildID = guildID;
        this.language = language;
        this.TimeZone = TimeZone;
        this.guildAdminRoleID = guildAdminRoleID;
        this.warnTimeInMinutes = warnTimeInMinutes;
        this.isWarnMessagesEnabled = isEventMessageEnabled;
        this.isEventMessageEnabled = isWarnMessagesEnabled;
        this.customNotifications = new ConcurrentHashMap<>();
        this.notificationChannels = new ConcurrentHashMap<>();
        this.isDaylightTimeEnabled = isDaylightTimeEnabled;
        this.isPremiumServer = isPremiumServer;
        this.autoDeleteTimeInHours = autoDeleteTimeInHours;
        this.isAutoDeleteEnabled = isAutoDeleteEnabled;
        this.embedLeadTime = embedLeadTime;
    }

    public ClientGuild(String guildID) {
        this.guildID = guildID;
        this.language = Language.ENGLISH;
        this.TimeZone = "GMT";
        this.guildAdminRoleID = null;
        this.warnTimeInMinutes = 15;
        this.isWarnMessagesEnabled = true;
        this.isEventMessageEnabled = true;
        this.customNotifications = new ConcurrentHashMap<>();
        this.notificationChannels = new ConcurrentHashMap<>();
        this.isDaylightTimeEnabled = false;
        this.isPremiumServer = false;
        this.autoDeleteTimeInHours = 1;
        this.isAutoDeleteEnabled = false;
        this.embedLeadTime = 1;
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
}
