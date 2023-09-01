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
        this.shadowWarEvent = new ShadowWarEvent(gameDataCache);
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

                        hauntedCarriageEmbed.sendHauntedCarriageEmbedIfHappening(clientGuild, channel, client);
                        demonGatesEmbed.sendDemonGatesEmbedIfHappening(clientGuild, channel, client);
                        ancientArenaEmbed.sendAncientArenaEmbedIfHappening(clientGuild, channel, client);
                        ancientNightmareEmbed.sendAncientNightmareEmbedIfHappening(clientGuild, channel, client);

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

                        addMessageMention(notificationMessage, channel, textChannel.getGuild(), clientGuild.getLanguage());

                        try {
                            if (clientGuild.isAutoDeleteEnabled()) {
                                int autoDeleteTimeInHours = clientGuild.getAutoDeleteTimeInHours();
                                sendTimedMessage(textChannel, notificationMessage.toString(), autoDeleteTimeInHours);
                            }

                            if (!clientGuild.isAutoDeleteEnabled()) {
                                sendMessage(textChannel, notificationMessage.toString());
                            }
                        } catch (InsufficientPermissionException e) {
                            log.error(e.getMessage());
                        }

                        log.info("Sended notification message to channel: " + channel.getTextChannelID() + ", GuildID: " +
                                clientGuild.getGuildID());
                    }
                }
            }
        }, TimeUtil.getNextFullMinute(), 60L * 1000L);
    }

    private boolean isChannelCountZero(ClientGuild clientGuild) {
        return clientGuild.getNotificationChannelCount() == 0;
    }
}