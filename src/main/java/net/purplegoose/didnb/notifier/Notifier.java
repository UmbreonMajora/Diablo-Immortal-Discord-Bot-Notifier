package net.purplegoose.didnb.notifier;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.purplegoose.didnb.cache.ErrorCache;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.gameevents.*;
import net.purplegoose.didnb.gameevents.embedded.AncientArenaEmbed;
import net.purplegoose.didnb.gameevents.embedded.AncientNightmareEmbed;
import net.purplegoose.didnb.gameevents.embedded.DemonGatesEmbed;
import net.purplegoose.didnb.gameevents.embedded.HauntedCarriageEmbed;
import net.purplegoose.didnb.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class Notifier extends NotifierHelper {

    private final GuildsCache guildsCache;
    private final ErrorCache errorCache;
    // Notification Messages
    private final VaultEvent vaultEvent;
    private final AncientArenaEvent ancientArenaEvent;
    private final AncientNightmareEvent ancientNightmareEvent;
    private final AssemblyEvent assemblyEvent;
    private final BattlegroundEvent battlegroundEvent;
    private final HauntedCarriageEvent hauntedCarriageEvent;
    private final ShadowLotteryEvent shadowLotteryEvent;
    private final WrathborneInvasionEvent wrathborneInvasionEvent;
    private final DemonGatesEvent demonGatesEvent;
    private final OnSlaughtEvent onSlaughtEvent;
    private final TowerOfVictoryEvent towerOfVictoryEvent;
    private final ShadowWarEvent shadowWarEvent;
    // Embed Messages
    private final HauntedCarriageEmbed hauntedCarriageEmbed;
    private final DemonGatesEmbed demonGatesEmbed;
    private final AncientNightmareEmbed ancientNightmareEmbed;
    private final AncientArenaEmbed ancientArenaEmbed;

    public Notifier(GuildsCache guildsCache, GameDataCache gameDataCache, ErrorCache errorCache) {
        this.guildsCache = guildsCache;
        this.errorCache = errorCache;

        GameEventBuilder gameEventBuilder = new GameEventBuilder().withGameDataCache(gameDataCache);

        this.vaultEvent = gameEventBuilder.buildVaultEvent();
        this.ancientArenaEvent = gameEventBuilder.buildAncientArenaEvent();
        this.onSlaughtEvent = gameEventBuilder.buildOnSlaughtEvent();
        this.battlegroundEvent = gameEventBuilder.buildBattlegroundEvent();
        this.towerOfVictoryEvent = gameEventBuilder.buildTowerOfVictoryEvent();
        this.assemblyEvent = gameEventBuilder.buildAssemblyEvent();
        this.ancientNightmareEvent = gameEventBuilder.buildAncientNightmareEvent();
        this.shadowWarEvent = gameEventBuilder.buildShadowWarEvent();
        this.demonGatesEvent = gameEventBuilder.buildDemonGatesEvent();
        this.hauntedCarriageEvent = gameEventBuilder.buildHauntedCarriageEvent();
        this.wrathborneInvasionEvent = gameEventBuilder.buildWrathborneInvasionEvent();
        this.shadowLotteryEvent = gameEventBuilder.buildShadowLotteryEvent();
        // Embedded
        this.hauntedCarriageEmbed = new HauntedCarriageEmbed(gameDataCache);
        this.demonGatesEmbed = new DemonGatesEmbed(gameDataCache);
        this.ancientArenaEmbed = new AncientArenaEmbed(gameDataCache);
        this.ancientNightmareEmbed = new AncientNightmareEmbed(gameDataCache);
    }

    public void runNotificationScheduler(JDA client) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                for (ClientGuild clientGuild : guildsCache.getAllGuilds().values()) {
                    String guildID = clientGuild.getGuildID();

                    if (isChannelCountZero(clientGuild)) {
                        log.info("{} has no registered channels, skipping...", guildID);
                        continue;
                    }

                    for (NotificationChannel channel : clientGuild.getAllNotificationChannels()) {
                        StringBuilder notificationMessage = new StringBuilder();
                        appendEventNotifications(clientGuild, channel, notificationMessage);
                        sendEmbedMessages(clientGuild, channel, client);
                        TextChannel textChannel = client.getTextChannelById(channel.getTextChannelID());


                        if (!doChannelExist(textChannel)) {
                            log.info("Tried to send a message to a non existent channel in {}, adding to ErrorCache " +
                                    "and skipping...", guildID);
                            errorCache.addChannelError(channel);
                            continue;
                        }

                        if (isNoEventRunning(notificationMessage)) {
                            log.info("No currently running events in {}, skipping sending this message...",
                                    guildID);
                            continue;
                        }

                        addMessageMention(notificationMessage, channel, textChannel.getGuild());

                        try {
                            sendNotificationMessage(clientGuild, notificationMessage, textChannel);
                        } catch (InsufficientPermissionException e) {
                            log.error("{} in guild {} in channel {}", e.getMessage(), guildID, textChannel.getId());
                        }

                        log.info("Notification message was sent to guild {} in channel {}.", guildID, textChannel.getId());
                    }
                }
            }
        }, TimeUtil.getNextFullMinute(), 60L * 1000L);
    }

    private void sendNotificationMessage(ClientGuild clientGuild, StringBuilder notificationMessage, TextChannel textChannel) {
        if (clientGuild.isAutoDeleteEnabled()) {
            int autoDeleteTimeInHours = clientGuild.getAutoDeleteTimeInHours();
            sendTimedMessage(textChannel, notificationMessage.toString(), autoDeleteTimeInHours);
        } else {
            sendMessage(textChannel, notificationMessage.toString());
        }
    }

    private void sendEmbedMessages(ClientGuild clientGuild, NotificationChannel channel, JDA client) {
        hauntedCarriageEmbed.sendHauntedCarriageEmbedIfHappening(clientGuild, channel, client);
        demonGatesEmbed.sendDemonGatesEmbedIfHappening(clientGuild, channel, client);
        ancientArenaEmbed.sendAncientArenaEmbedIfHappening(clientGuild, channel, client);
        ancientNightmareEmbed.sendAncientNightmareEmbedIfHappening(clientGuild, channel, client);
    }

    private void appendEventNotifications(ClientGuild clientGuild, NotificationChannel channel, StringBuilder notificationMessage) {
        notificationMessage.append(vaultEvent.appendVaultNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(battlegroundEvent.appendBattlegroundsNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(ancientArenaEvent.appendAncientArenaNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(demonGatesEvent.appendDemonGatesNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(assemblyEvent.appendAssemblyNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(hauntedCarriageEvent.appendHauntedCarriageNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(ancientNightmareEvent.appendAncientNightmareNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(shadowLotteryEvent.appendShadowLotteryNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(wrathborneInvasionEvent.appendWrathborneInvasionNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(onSlaughtEvent.appendOnSlaughtEventIfHappening(clientGuild, channel));
        notificationMessage.append(towerOfVictoryEvent.appendTowerOfVictoryNotificationIfHappening(clientGuild, channel));
        notificationMessage.append(shadowWarEvent.appendShadowWarNotificationIfHappening(clientGuild, channel));
    }

    private boolean isNoEventRunning(StringBuilder stringBuilder) {
        return stringBuilder.length() <= 0;
    }

    private boolean isChannelCountZero(ClientGuild clientGuild) {
        return clientGuild.getNotificationChannelCount() == 0;
    }

    private boolean doChannelExist(TextChannel textChannel) {
        return textChannel != null;
    }
}