package net.purplegoose.didnb.notifier;

import kotlin.Triple;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.exeption.InvalidMentionException;
import net.purplegoose.didnb.gameevents.*;
import net.purplegoose.didnb.utils.ChannelLogger;
import net.purplegoose.didnb.utils.TimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class Notifier extends NotifierHelper {
    // Cache
    private final GuildsCache guildsCache;
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

    private int count = 0;

    public Notifier(GuildsCache guildsCache, GameDataCache gameDataCache) {
        this.guildsCache = guildsCache;

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
    }

    public void runNotificationScheduler(JDA jdaClient) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                int guildCount = 0;
                int channelCount = 0;

                for (ClientGuild clientGuild : guildsCache.getAllGuilds().values()) {
                    guildCount++;
                    if (isChannelCountZero(clientGuild)) {
                        continue;
                    }

                    for (NotificationChannel channel : clientGuild.getAllNotificationChannels()) {
                        channelCount++;
                        StringBuilder notificationMessage = new StringBuilder();
                        appendNotificationMessages(clientGuild, channel, notificationMessage);
                        TextChannel textChannel = jdaClient.getTextChannelById(channel.getTextChannelID());

                        if (!isNotificationValid(textChannel, notificationMessage.toString(), channel)) {
                            continue;
                        }

                        if (!sendMessage(notificationMessage, clientGuild, jdaClient, Objects.requireNonNull(textChannel), channel)) {
                            continue;
                        }

                        log.info("Sent notification message to channel: " + channel.getTextChannelID() + ", GuildID: " +
                                clientGuild.getGuildID());
                    }
                }
                String msg = String.format("Ran scheduler with %s guilds and %s channels.", guildCount, channelCount);
                log.info(msg);

                count++;
                if (count == 5) {
                    ChannelLogger.sendChannelLog(msg);
                    count = 0;
                }
            }
        }, TimeUtil.getNextFullMinute(), 60L * 1000L);
    }

    private boolean sendMessage(StringBuilder notificationMessage, ClientGuild clientGuild, JDA jdaClient,
                                TextChannel textChannel, NotificationChannel channel) {
        try {
            addMessageMention(notificationMessage, channel, textChannel.getGuild(), clientGuild.getLanguage());
        } catch (InvalidMentionException e) {
            handleInvalidMention(textChannel.getGuild(), textChannel);
            return false;
        }

        try {
            sendNotificationMessage(clientGuild, textChannel, notificationMessage.toString());
        } catch (InsufficientPermissionException e) {
            handleInsufficientPermission(e, jdaClient);
            return false;
        }
        return true;
    }

    private void handleInvalidMention(Guild jdaGuild, TextChannel jdaChannel) {
        sendInvalidMentionMessageToAdministrators(getAdminListFromGuild(jdaGuild), jdaGuild, jdaChannel);
    }

    private boolean isNotificationValid(Channel textChannel, String message, NotificationChannel channel) {
        if (isChannelNull(textChannel, channel)) {
            return false;
        }
        return !isMessageLengthZero(message);
    }

    private boolean isChannelNull(Channel textChannel, NotificationChannel channel) {
        if (textChannel == null) {
            String logMsg = "Tried to send notification message to " + channel.getTextChannelID() + " on " +
                    "guild " + channel.getGuildID() + ", but it failed because the channel was null!";
            log.error(logMsg);
            return true;
        }
        return false;
    }

    private boolean isMessageLengthZero(String message) {
        return message.length() == 0;
    }

    private void appendNotificationMessages(ClientGuild clientGuild, NotificationChannel channel, StringBuilder notificationMessage) {
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

    private boolean isChannelCountZero(ClientGuild clientGuild) {
        return clientGuild.getNotificationChannelCount() == 0;
    }

    private void sendNotificationMessage(ClientGuild clientGuild, TextChannel textChannel, String msg)
            throws InsufficientPermissionException {
        if (clientGuild.isAutoDeleteEnabled()) {
            int autoDeleteTimeInHours = clientGuild.getAutoDeleteTimeInHours();
            sendTimedMessage(textChannel, msg, autoDeleteTimeInHours);
        }

        if (!clientGuild.isAutoDeleteEnabled()) {
            sendMessage(textChannel, msg);
        }
    }

    private void handleInsufficientPermission(InsufficientPermissionException exception, JDA jdaClient) {
        Permission missingPermission = exception.getPermission();
        Guild jdaGuild = exception.getGuild(jdaClient);
        if (jdaGuild == null) {
            log.error("Failed to send insufficient permission message to administrators of guild with id {}. " +
                    "Missing permission: {}. Guild was null.", exception.getGuildId(), missingPermission.getName());
            return;
        }

        Channel jdaChannel = exception.getChannel(jdaClient);
        if (jdaChannel == null) {
            log.error("Failed to send insufficient permission message to administrators of guild with id {}. " +
                    "Missing permission: {}. Channel was null.", exception.getGuildId(), missingPermission.getName());
            return;
        }

        Triple<String, String, String> missingPermissionInfo =
                new Triple<>(jdaGuild.getName(), jdaChannel.getName(), missingPermission.getName());
        sendInsufficientPermissionMessageToAdministrators(getAdminListFromGuild(jdaGuild), missingPermissionInfo);
    }

    private List<Member> getAdminListFromGuild(Guild jdaGuild) {
        List<Member> memberList = jdaGuild.getMembers();
        return memberList.stream()
                .filter(member -> member.isOwner() || member.hasPermission(Permission.ADMINISTRATOR)).toList();
    }

    private void sendInsufficientPermissionMessageToAdministrators(List<Member> adminList,
                                                                   Triple<String, String, String> missingPermissionInfo) {
        for (Member admin : adminList) {
            admin.getUser().openPrivateChannel().queue(privateChannel -> {
                String msg = String.format("Hey, I've tried to send a message in your guild named %s but I " +
                                "am missing permission '%s' in channel %s.", missingPermissionInfo.getFirst(),
                        missingPermissionInfo.getThird(), missingPermissionInfo.getSecond());
                privateChannel.sendMessage(msg).queue();
            });
        }
    }

    private void sendInvalidMentionMessageToAdministrators(List<Member> adminList, Guild jdaGuild, TextChannel channel) {
        for (Member admin : adminList) {
            admin.getUser().openPrivateChannel().queue(privateChannel -> {
                String msg = String.format("Hey, I've tried to add the mention to your notification message but I " +
                                "couldn't find it. Guild: %s, Channel: %s", jdaGuild.getName(), channel.getName());
                privateChannel.sendMessage(msg).queue();
            });
        }
    }
}