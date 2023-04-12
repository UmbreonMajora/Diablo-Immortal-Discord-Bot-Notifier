package net.purplegoose.didnb.data;

import lombok.Data;

@Data
public class NotificationChannel {

    private String mentionRoleID;
    private String guildID;
    private String textChannelID;

    private boolean eventWarnMessage;
    private boolean eventMessageEnabled;
    private boolean assemblyMessageEnabled;
    private boolean vaultMessageEnabled;
    private boolean demonGatesMessageEnabled;
    private boolean ancientArenaMessageEnabled;
    private boolean shadowLotteryMessageEnabled;
    private boolean battlegroundsMessageEnabled;
    private boolean hauntedCarriageMessageEnabled;
    private boolean ancientNightmareMessageEnabled;
    private boolean wrathborneInvasionEnabled;
    private boolean isOnSlaughtMessagesEnabled;

    private boolean demonGatesMessageEmbedEnabled;
    private boolean ancientArenaMessageEmbedEnabled;
    private boolean hauntedCarriageMessageEmbedEnabled;
    private boolean ancientNightmareMessageEmbedEnabled;

    public NotificationChannel(final String textChannelID, final String guildID) {
        this.eventWarnMessage = true;
        this.eventMessageEnabled = true;
        this.assemblyMessageEnabled = true;
        this.vaultMessageEnabled = true;
        this.demonGatesMessageEnabled = true;
        this.ancientArenaMessageEnabled = true;
        this.shadowLotteryMessageEnabled = true;
        this.battlegroundsMessageEnabled = true;
        this.hauntedCarriageMessageEnabled = true;
        this.ancientNightmareMessageEnabled = true;
        this.wrathborneInvasionEnabled = true;
        this.isOnSlaughtMessagesEnabled = true;

        this.demonGatesMessageEmbedEnabled = false;
        this.ancientArenaMessageEmbedEnabled = false;
        this.hauntedCarriageMessageEmbedEnabled = false;
        this.ancientNightmareMessageEmbedEnabled = false;

        this.mentionRoleID = "EVERYONE";
        this.guildID = guildID;
        this.textChannelID = textChannelID;
    }

    public NotificationChannel(String mentionRoleID, final String guildID, String textChannelID,
                               boolean eventWarnMessage, boolean eventMessageEnabled, boolean assemblyMessageEnabled,
                               boolean vaultMessageEnabled, boolean demonGatesMessageEnabled,
                               boolean ancientArenaMessageEnabled,
                               boolean shadowLotteryMessageEnabled, boolean battlegroundsMessageEnabled,
                               boolean hauntedCarriageMessageEnabled, boolean ancientNightmareMessageEnabled,
                               boolean demonGatesMessageEmbedEnabled, boolean ancientArenaMessageEmbedEnabled,
                               boolean hauntedCarriageMessageEmbedEnabled, boolean ancientNightmareMessageEmbedEnabled,
                               boolean wrathborneInvasionEnabled, boolean isOnSlaughtMessagesEnabled) {
        this.mentionRoleID = mentionRoleID;
        this.guildID = guildID;
        this.textChannelID = textChannelID;
        this.eventWarnMessage = eventWarnMessage;
        this.eventMessageEnabled = eventMessageEnabled;
        this.assemblyMessageEnabled = assemblyMessageEnabled;
        this.vaultMessageEnabled = vaultMessageEnabled;
        this.demonGatesMessageEnabled = demonGatesMessageEnabled;
        this.ancientArenaMessageEnabled = ancientArenaMessageEnabled;
        this.shadowLotteryMessageEnabled = shadowLotteryMessageEnabled;
        this.battlegroundsMessageEnabled = battlegroundsMessageEnabled;
        this.hauntedCarriageMessageEnabled = hauntedCarriageMessageEnabled;
        this.ancientNightmareMessageEnabled = ancientNightmareMessageEnabled;
        this.demonGatesMessageEmbedEnabled = demonGatesMessageEmbedEnabled;
        this.ancientArenaMessageEmbedEnabled = ancientArenaMessageEmbedEnabled;
        this.hauntedCarriageMessageEmbedEnabled = hauntedCarriageMessageEmbedEnabled;
        this.ancientNightmareMessageEmbedEnabled = ancientNightmareMessageEmbedEnabled;
        this.wrathborneInvasionEnabled = wrathborneInvasionEnabled;
        this.isOnSlaughtMessagesEnabled = isOnSlaughtMessagesEnabled;
    }

}
