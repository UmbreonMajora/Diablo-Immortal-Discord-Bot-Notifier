package net.purplegoose.didnb.data;

import net.purplegoose.didnb.enums.Language;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientGuild {

    private final String guildID;
    private Language guildLanguage;
    private String guildTimeZone;
    private String guildAdminRoleID;
    private int warnTimeInMinutes;
    private boolean isWarnMessagesEnabled;
    private boolean isEventMessageEnabled;
    private Map<Integer, CustomNotification> customNotifications;
    private Map<String, NotificationChannel> notificationChannels;
    private boolean isDaylightTimeEnabled;
    private boolean isPremiumServer;

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
                       int warnTimeInMinutes, boolean isEventMessageEnabled, boolean isWarnMessagesEnabled, boolean isDaylightTimeEnabled, boolean isPremiumServer) {
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

    public String getGuildID() {
        return guildID;
    }

    public Language getGuildLanguage() {
        return guildLanguage;
    }

    public void setGuildLanguage(Language guildLanguage) {
        this.guildLanguage = guildLanguage;
    }

    public String getGuildTimeZone() {
        return guildTimeZone;
    }

    public void setGuildTimeZone(String guildTimeZone) {
        this.guildTimeZone = guildTimeZone;
    }

    public String getGuildAdminRoleID() {
        return guildAdminRoleID;
    }

    public void setGuildAdminRoleID(String guildAdminRoleID) {
        this.guildAdminRoleID = guildAdminRoleID;
    }

    public boolean isWarnMessagesEnabled() {
        return isWarnMessagesEnabled;
    }

    public void setWarnMessagesEnabled(boolean warnMessagesEnabled) {
        isWarnMessagesEnabled = warnMessagesEnabled;
    }

    public boolean isEventMessageEnabled() {
        return isEventMessageEnabled;
    }

    public void setEventMessageEnabled(boolean eventMessageEnabled) {
        isEventMessageEnabled = eventMessageEnabled;
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

    public int getWarnTimeInMinutes() {
        return warnTimeInMinutes;
    }

    public void setWarnTimeInMinutes(int warnTimeInMinutes) {
        this.warnTimeInMinutes = warnTimeInMinutes;
    }

    public int getNotificationChannelCount() {
        return notificationChannels.size();
    }

    public Collection<NotificationChannel> getAllNotificationChannels() {
        return notificationChannels.values();
    }

    public boolean isDaylightTimeEnabled() {
        return isDaylightTimeEnabled;
    }

    public void setDaylightTimeEnabled(boolean daylightTimeEnabled) {
        isDaylightTimeEnabled = daylightTimeEnabled;
    }

    public boolean isPremiumServer() {
        return isPremiumServer;
    }

    public void setPremiumServer(boolean premiumServer) {
        isPremiumServer = premiumServer;
    }

    public int getCustomNotificationSize() {
        return customNotifications.size();
    }
}
