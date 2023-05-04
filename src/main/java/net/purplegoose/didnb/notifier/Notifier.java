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
    // Embed Messages
    private final HauntedCarriageEmbed hauntedCarriageEmbed;
    private final DemonGatesEmbed demonGatesEmbed;
    private final AncientNightmareEmbed ancientNightmareEmbed;
    private final AncientArenaEmbed ancientArenaEmbed;

    public Notifier(GuildsCache guildsCache, GameDataCache gameDataCache, ErrorCache errorCache) {
        this.guildsCache = guildsCache;
        this.errorCache = errorCache;

        this.vaultEvent = new VaultEvent(gameDataCache);
        this.ancientArenaEvent = new AncientArenaEvent(gameDataCache);
        this.ancientNightmareEvent = new AncientNightmareEvent(gameDataCache);
        this.assemblyEvent = new AssemblyEvent(gameDataCache);
        this.battlegroundEvent = new BattlegroundEvent(gameDataCache);
        this.hauntedCarriageEvent = new HauntedCarriageEvent(gameDataCache);
        this.shadowLotteryEvent = new ShadowLotteryEvent(gameDataCache);
        this.wrathborneInvasionEvent = new WrathborneInvasionEvent(gameDataCache);
        this.demonGatesEvent = new DemonGatesEvent(gameDataCache);
        this.onSlaughtEvent = new OnSlaughtEvent(gameDataCache);
        this.towerOfVictoryEvent = new TowerOfVictoryEvent(gameDataCache);
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

                    if (isChannelCountZero(clientGuild)) {
                        continue;
                    }

                    for (NotificationChannel channel : clientGuild.getAllNotificationChannels()) {
                        StringBuilder notificationMessage = new StringBuilder();
                        appendNotificationMessageIfHappening(notificationMessage, clientGuild, channel);
                        sendEmbedMessages(clientGuild, channel, client);

                        TextChannel textChannel = client.getTextChannelById(channel.getTextChannelID());

                        if (notificationMessage.length() <= 0) {
                            continue;
                        }

                        if (textChannel == null) {
                            String logMsg = "Tried to send notification message to " + channel.getTextChannelID() + " on " +
                                    "guild " + channel.getGuildID() + ", but it failed because the channel was null!";
                            log.error(logMsg);
                            errorCache.addChannelError(channel);
                            continue;
                        }

                        addMessageMention(notificationMessage, channel, textChannel.getGuild());

                        try {
                            sendNotificationMessage(clientGuild, notificationMessage, textChannel);
                        } catch (InsufficientPermissionException e) {
                            log.error(e.getMessage(), e);
                        }

                        log.info("Sended notification message to " + channel.getTextChannelID());
                    }
                }
            }
        }, TimeUtil.getNextFullMinute(), 60L * 1000L);
    }

    private void sendNotificationMessage(final ClientGuild clientGuild, final StringBuilder notificationMessage, final TextChannel textChannel) {
        if (clientGuild.isAutoDeleteEnabled()) {
            int autoDeleteTimeInHours = clientGuild.getAutoDeleteTimeInHours();
            sendMessageWithAutoDelete(textChannel, notificationMessage.toString(), autoDeleteTimeInHours);
        }

        if (!clientGuild.isAutoDeleteEnabled()) {
            sendMessageWithoutAutoDelete(textChannel, notificationMessage.toString());
        }
    }

    private boolean isChannelCountZero(ClientGuild clientGuild) {
        return clientGuild.getNotificationChannelCount() == 0;
    }

    private void sendEmbedMessages(ClientGuild clientGuild, NotificationChannel channel, JDA client) {
        hauntedCarriageEmbed.sendHauntedCarriageEmbedIfHappening(clientGuild, channel, client);
        demonGatesEmbed.sendDemonGatesEmbedIfHappening(clientGuild, channel, client);
        ancientNightmareEmbed.sendAncientNightmareEmbedIfHappening(clientGuild, channel, client);
        ancientArenaEmbed.sendAncientArenaEmbedIfHappening(clientGuild, channel, client);
    }

    private void appendNotificationMessageIfHappening(StringBuilder sb, ClientGuild clientGuild, NotificationChannel channel) {
        sb.append(vaultEvent.appendVaultNotificationIfHappening(clientGuild, channel));
        sb.append(battlegroundEvent.appendBattlegroundsNotificationIfHappening(clientGuild, channel));
        sb.append(ancientArenaEvent.appendAncientArenaNotificationIfHappening(clientGuild, channel));
        sb.append(demonGatesEvent.appendDemonGatesNotificationIfHappening(clientGuild, channel));
        sb.append(assemblyEvent.appendAssemblyNotificationIfHappening(clientGuild, channel));
        sb.append(hauntedCarriageEvent.appendHauntedCarriageNotificationIfHappening(clientGuild, channel));
        sb.append(ancientNightmareEvent.appendAncientNightmareNotificationIfHappening(clientGuild, channel));
        sb.append(shadowLotteryEvent.appendShadowLotteryNotificationIfHappening(clientGuild, channel));
        sb.append(wrathborneInvasionEvent.appendWrathborneInvasionNotificationIfHappening(clientGuild, channel));
        sb.append(onSlaughtEvent.appendOnSlaughtEventIfHappening(clientGuild, channel));
        sb.append(towerOfVictoryEvent.appendTowerOfVictoryNotificationIfHappening(clientGuild, channel));
    }

}
