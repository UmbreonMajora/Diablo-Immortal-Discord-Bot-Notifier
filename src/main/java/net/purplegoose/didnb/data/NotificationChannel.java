package net.purplegoose.didnb.data;

import lombok.Data;
import net.purplegoose.didnb.annotations.GameEvent;

@Data
public class NotificationChannel {

    private String mentionRoleID;
    private String guildID;
    private String textChannelID;

    private boolean eventWarnMessage;
    private boolean eventMessageEnabled;
    @GameEvent(eventName = "Assembly")
    private boolean assemblyMessageEnabled;
    @GameEvent(eventName = "Vault")
    private boolean vaultMessageEnabled;
    @GameEvent(eventName = "Demon Gates")
    private boolean demonGatesMessageEnabled;
    @GameEvent(eventName = "Ancient Arena")
    private boolean ancientArenaMessageEnabled;
    @GameEvent(eventName = "Shadow Lottery")
    private boolean shadowLotteryMessageEnabled;
    @GameEvent(eventName = "Battlegrounds")
    private boolean battlegroundsMessageEnabled;
    @GameEvent(eventName = "Haunted Carriage")
    private boolean hauntedCarriageMessageEnabled;
    @GameEvent(eventName = "Ancient Nightmare")
    private boolean ancientNightmareMessageEnabled;
    @GameEvent(eventName = "Wrathborne Invasion")
    private boolean wrathborneInvasionEnabled;
    @GameEvent(eventName = "Onslaught")
    private boolean isOnSlaughtMessagesEnabled;
    @GameEvent(eventName = "Tower of Victory")
    private boolean towerOfVictoryMessagesEnabled;
    @GameEvent(eventName = "Shadow War")
    private boolean shadowWarMessagesEnabled;

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
        this.towerOfVictoryMessagesEnabled = true;
        this.shadowWarMessagesEnabled = true;

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
                               boolean wrathborneInvasionEnabled, boolean isOnSlaughtMessagesEnabled,
                               boolean towerOfVictoryMessagesEnabled, boolean shadowWarMessagesEnabled) {
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
        this.towerOfVictoryMessagesEnabled = towerOfVictoryMessagesEnabled;
        this.shadowWarMessagesEnabled = shadowWarMessagesEnabled;
    }

}
