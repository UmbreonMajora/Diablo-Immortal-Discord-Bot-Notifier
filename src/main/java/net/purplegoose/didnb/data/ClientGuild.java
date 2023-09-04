package net.purplegoose.didnb.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.purplegoose.didnb.enums.Language;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class ClientGuild {

    @Setter(value = AccessLevel.NONE)
    private final String guildID;
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

    private final Map<String, CustomNotification> customNotifications;
    private final Map<String, NotificationChannel> notificationChannels;

    public ClientGuild(String guildID, Language language, String timeZone, String guildAdminRoleID,
                       int warnTimeInMinutes, boolean isEventMessageEnabled, boolean isWarnMessagesEnabled,
                       boolean isDaylightTimeEnabled, boolean isPremiumServer, int autoDeleteTimeInHours,
                       boolean isAutoDeleteEnabled, long embedLeadTime) {
        this.guildID = guildID;
        this.language = language;
        this.timeZone = timeZone;
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
        this.timeZone = "GMT";
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
