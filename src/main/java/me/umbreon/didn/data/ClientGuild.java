package me.umbreon.didn.data;

import me.umbreon.didn.enums.Language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientGuild {

    private final String guildID;
    private Language guildLanguage;
    private String guildTimeZone;
    private String guildAdminRoleID;
    private int headUpTime;
    private boolean isWarnMessagesEnabled;
    private boolean isEventMessageEnabled;
    private List<ReactionRole> reactionRoles;
    private Map<Integer, CustomNotification> customNotifications;
    private Map<String, NotificationChannel> notificationChannels;
    private boolean isDaylightTimeEnabled;
    private boolean isPremiumServer;

    public ClientGuild(String guildID, Language guildLanguage, String guildTimeZone, String guildAdminRoleID,
                       int headUpTime, boolean isWarnMessagesEnabled, boolean isEventMessageEnabled,
                       List<ReactionRole> reactionRoles,
                       Map<Integer, CustomNotification> customNotifications,
                       Map<String, NotificationChannel> notificationChannels,
                       boolean isDaylightTimeEnabled, boolean isPremiumServer) {
        this.guildID = guildID;
        this.guildLanguage = guildLanguage;
        this.guildTimeZone = guildTimeZone;
        this.guildAdminRoleID = guildAdminRoleID;
        this.headUpTime = headUpTime;
        this.isWarnMessagesEnabled = isWarnMessagesEnabled;
        this.isEventMessageEnabled = isEventMessageEnabled;
        this.reactionRoles = reactionRoles;
        this.customNotifications = customNotifications;
        this.notificationChannels = notificationChannels;
        this.isDaylightTimeEnabled = isDaylightTimeEnabled;
        this.isPremiumServer = isPremiumServer;
    }

    public ClientGuild(String guildID, Language guildLanguage, String guildTimeZone, String guildAdminRoleID,
                       int headUpTime, boolean isEventMessageEnabled, boolean isWarnMessagesEnabled, boolean isDaylightTimeEnabled, boolean isPremiumServer) {
        this.guildID = guildID;
        this.guildLanguage = guildLanguage;
        this.guildTimeZone = guildTimeZone;
        this.guildAdminRoleID = guildAdminRoleID;
        this.headUpTime = headUpTime;
        this.isWarnMessagesEnabled = isEventMessageEnabled;
        this.isEventMessageEnabled = isWarnMessagesEnabled;
        this.reactionRoles = new ArrayList<>();
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
        this.headUpTime = 15;
        this.isWarnMessagesEnabled = true;
        this.isEventMessageEnabled = true;
        this.reactionRoles = new ArrayList<>();
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

    public List<ReactionRole> getReactionRoles() {
        return reactionRoles;
    }

    public void setReactionRoles(List<ReactionRole> reactionRoles) {
        this.reactionRoles = reactionRoles;
    }

    public Map<Integer, CustomNotification> getCustomNotifications() {
        return customNotifications;
    }

    public void setCustomNotifications(Map<Integer, CustomNotification> customNotifications) {
        this.customNotifications = customNotifications;
    }

    public Map<String, NotificationChannel> getNotificationChannels() {
        return notificationChannels;
    }

    public void setNotificationChannels(Map<String, NotificationChannel> notificationChannels) {
        this.notificationChannels = notificationChannels;
    }

    public boolean isChannelRegistered(String textChannelID) {
        return notificationChannels.containsKey(textChannelID);
    }

    public NotificationChannel getNotificationChannel(String textChannelID) {
        return notificationChannels.get(textChannelID);
    }

    public void replaceNotificationChannel(NotificationChannel notificationChannel) {
        notificationChannels.replace(notificationChannel.getTextChannelID(), notificationChannel);
    }

    public void addNewNotificationChannel(NotificationChannel notificationChannel) {
        notificationChannels.put(notificationChannel.getTextChannelID(), notificationChannel);
    }

    public void deleteNotificationChannelByID(String textChannelID) {
        notificationChannels.remove(textChannelID);
    }

    public int getReactionRolesAmount(String messageID) {
        int count = 0;
        for (ReactionRole reactionRole : reactionRoles) {
            if (reactionRole.getMessageID().equals(messageID)) {
                count++;
            }
        }
        return count;
    }

    public void addReactionRole(ReactionRole reactionRole) {
        reactionRoles.add(reactionRole);
    }

    public ReactionRole getReactionRoleByMessageIDAndEmojiID(String messageID, String emojiCode) {
        for (ReactionRole reactionRole : reactionRoles) {
            if (reactionRole.getMessageID().equals(messageID)) {
                if (reactionRole.getReactionID().equals(emojiCode)) {
                    return reactionRole;
                }
            }
        }
        return null;
    }

    public void removeReactionRoleByMessageIDAndEmojiID(final String messageID, final String codifiedEmote) {
        for (ReactionRole reactionRole : reactionRoles) {
            String tmpListMessageID = reactionRole.getMessageID();
            String tmpListCodifiedEmote = reactionRole.getReactionID();
            if (tmpListMessageID.equalsIgnoreCase(messageID) && tmpListCodifiedEmote.equalsIgnoreCase(codifiedEmote)) {
                reactionRoles.remove(reactionRole);
                break;
            }
        }
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

    public boolean doReactionRoleExist(String messageID) {
        return reactionRoles.stream()
                .filter(reactionRole -> reactionRole.getMessageID().equals(messageID))
                .findFirst()
                .orElse(null) != null;
    }

    public boolean doReactionRoleExist(String messageID, String emojiCode) {
        return reactionRoles.stream()
                .filter(reactionRole -> reactionRole.getMessageID().equals(messageID))
                .filter(reactionRole -> reactionRole.getReactionID().equals(emojiCode))
                .findFirst()
                .orElse(null) != null;
    }

    public void deleteReactionRoleByMessageID(String messageID) {
        reactionRoles.removeIf(reactionRole -> reactionRole.getMessageID().equals(messageID));
    }

    public int getHeadUpTime() {
        return headUpTime;
    }

    public void setHeadUpTime(int headUpTime) {
        this.headUpTime = headUpTime;
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
