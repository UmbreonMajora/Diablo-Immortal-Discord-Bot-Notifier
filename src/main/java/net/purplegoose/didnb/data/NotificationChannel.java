package net.purplegoose.didnb.data;

public class NotificationChannel {

    private String roleID;
    private String guildID;
    private String textChannelID;

    private boolean eventHeadUpEnabled;
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
        this.eventHeadUpEnabled = true;
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

        this.roleID = "EVERYONE";
        this.guildID = guildID;
        this.textChannelID = textChannelID;
    }

    public NotificationChannel(String roleID, final String guildID, String textChannelID,
                               boolean eventHeadUpEnabled, boolean eventMessageEnabled, boolean assemblyMessageEnabled,
                               boolean vaultMessageEnabled, boolean demonGatesMessageEnabled,
                               boolean ancientArenaMessageEnabled,
                               boolean shadowLotteryMessageEnabled, boolean battlegroundsMessageEnabled,
                               boolean hauntedCarriageMessageEnabled, boolean ancientNightmareMessageEnabled,
                               boolean demonGatesMessageEmbedEnabled, boolean ancientArenaMessageEmbedEnabled,
                               boolean hauntedCarriageMessageEmbedEnabled, boolean ancientNightmareMessageEmbedEnabled,
                               boolean wrathborneInvasionEnabled, boolean isOnSlaughtMessagesEnabled) {
        this.roleID = roleID;
        this.guildID = guildID;
        this.textChannelID = textChannelID;
        this.eventHeadUpEnabled = eventHeadUpEnabled;
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

    public String getMentionRoleID() {
        return roleID;
    }

    public void setRoleID(final String roleID) {
        this.roleID = roleID;
    }

    public String getTextChannelID() {
        return textChannelID;
    }

    public void setTextChannelID(final String textChannelID) {
        this.textChannelID = textChannelID;
    }

    public boolean isWarnMessagesEnabled() {
        return eventHeadUpEnabled;
    }

    public void setEventHeadUpEnabled(final boolean eventHeadUpEnabled) {
        this.eventHeadUpEnabled = eventHeadUpEnabled;
    }

    public boolean isEventMessageEnabled() {
        return eventMessageEnabled;
    }

    public void setEventMessageEnabled(final boolean eventMessageEnabled) {
        this.eventMessageEnabled = eventMessageEnabled;
    }

    public boolean isAssemblyMessageEnabled() {
        return assemblyMessageEnabled;
    }

    public void setAssemblyMessageEnabled(final boolean assemblyMessageEnabled) {
        this.assemblyMessageEnabled = assemblyMessageEnabled;
    }

    public boolean isVaultMessageEnabled() {
        return vaultMessageEnabled;
    }

    public void setVaultMessageEnabled(final boolean vaultMessageEnabled) {
        this.vaultMessageEnabled = vaultMessageEnabled;
    }

    public boolean isDemonGatesMessageEnabled() {
        return demonGatesMessageEnabled;
    }

    public void setDemonGatesMessageEnabled(final boolean demonGatesMessageEnabled) {
        this.demonGatesMessageEnabled = demonGatesMessageEnabled;
    }

    public boolean isAncientArenaMessageEnabled() {
        return ancientArenaMessageEnabled;
    }

    public void setAncientArenaMessageEnabled(final boolean ancientArenaMessageEnabled) {
        this.ancientArenaMessageEnabled = ancientArenaMessageEnabled;
    }

    public boolean isShadowLotteryMessageEnabled() {
        return shadowLotteryMessageEnabled;
    }

    public void setShadowLotteryMessageEnabled(final boolean shadowLotteryMessageEnabled) {
        this.shadowLotteryMessageEnabled = shadowLotteryMessageEnabled;
    }

    public boolean isBattlegroundsMessageEnabled() {
        return battlegroundsMessageEnabled;
    }

    public void setBattlegroundsMessageEnabled(final boolean battlegroundsMessageEnabled) {
        this.battlegroundsMessageEnabled = battlegroundsMessageEnabled;
    }

    public boolean isHauntedCarriageMessageEnabled() {
        return hauntedCarriageMessageEnabled;
    }

    public void setHauntedCarriageMessageEnabled(final boolean hauntedCarriageMessageEnabled) {
        this.hauntedCarriageMessageEnabled = hauntedCarriageMessageEnabled;
    }

    public boolean isAncientNightmareMessageEnabled() {
        return ancientNightmareMessageEnabled;
    }

    public void setAncientNightmareMessageEnabled(final boolean ancientNightmareMessageEnabled) {
        this.ancientNightmareMessageEnabled = ancientNightmareMessageEnabled;
    }

    public boolean isDemonGatesMessageEmbedEnabled() {
        return demonGatesMessageEmbedEnabled;
    }

    public void setDemonGatesMessageEmbedEnabled(final boolean demonGatesMessageEmbedEnabled) {
        this.demonGatesMessageEmbedEnabled = demonGatesMessageEmbedEnabled;
    }

    public boolean isAncientArenaMessageEmbedEnabled() {
        return ancientArenaMessageEmbedEnabled;
    }

    public void setAncientArenaMessageEmbedEnabled(final boolean ancientArenaMessageEmbedEnabled) {
        this.ancientArenaMessageEmbedEnabled = ancientArenaMessageEmbedEnabled;
    }

    public boolean isHauntedCarriageMessageEmbedEnabled() {
        return hauntedCarriageMessageEmbedEnabled;
    }

    public void setHauntedCarriageMessageEmbedEnabled(final boolean hauntedCarriageMessageEmbedEnabled) {
        this.hauntedCarriageMessageEmbedEnabled = hauntedCarriageMessageEmbedEnabled;
    }

    public boolean isAncientNightmareMessageEmbedEnabled() {
        return ancientNightmareMessageEmbedEnabled;
    }

    public void setAncientNightmareMessageEmbedEnabled(final boolean ancientNightmareMessageEmbedEnabled) {
        this.ancientNightmareMessageEmbedEnabled = ancientNightmareMessageEmbedEnabled;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(final String guildID) {
        this.guildID = guildID;
    }

    public boolean isWrathborneInvasionEnabled() {
        return wrathborneInvasionEnabled;
    }

    public void setWrathborneInvasionEnabled(final boolean wrathborneInvasionEnabled) {
        this.wrathborneInvasionEnabled = wrathborneInvasionEnabled;
    }

    public boolean isOnSlaughtMessagesEnabled() {
        return isOnSlaughtMessagesEnabled;
    }

    public void setOnSlaughtMessagesEnabled(boolean onSlaughtMessagesEnabled) {
        isOnSlaughtMessagesEnabled = onSlaughtMessagesEnabled;
    }
}
