package net.purplegoose.didnb.notifier;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.purplegoose.didnb.cache.ErrorCache;
import net.purplegoose.didnb.cache.GameDataCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.gameevents.*;
import net.purplegoose.didnb.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;

import static net.purplegoose.didnb.utils.StringUtil.NEW_LINE;
@Slf4j
public class Notifier {

    private final GuildsCache guildsCache;
    private final ErrorCache errorCache;

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
    }

    public void runNotificationScheduler(JDA client) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                for (ClientGuild clientGuild : guildsCache.getAllGuilds().values()) {

                    if (clientGuild.getNotificationChannelCount() == 0) {
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
                            textChannel.sendMessage(notificationMessage.toString()).queue();
                        } catch (InsufficientPermissionException e) {
                            String guildID = clientGuild.getGuildID();
                            Guild guild = client.getGuildById(guildID);
                            if (guild != null) {
                                Member guildOwner = guild.getOwner();
                                if (guildOwner != null) {
                                    User guildOwnerUser = guildOwner.getUser();
                                    guildOwnerUser.openPrivateChannel().queue(privateChannel -> {
                                        privateChannel.sendMessage("Hey, I am missing permissions to send " +
                                                "message in " + textChannel.getName()).queue();
                                    });
                                }
                            }
                            log.info("Failed to send notification.");
                            continue;
                        }

                        log.info("Sended notification message to " + channel.getTextChannelID());
                    }
                }
            }
        }, TimeUtil.getNextFullMinute(), 60 * 1000);
    }

    private void addMessageMention(StringBuilder sb, NotificationChannel channel, Guild guild) {
        String mentionID = channel.getMentionRoleID();

        if (mentionID == null) {
            return; // add no mention role if null.
        }

        switch (mentionID.toUpperCase()) {
            case "HERE":
                sb.append(NEW_LINE).append("@here");
                break;
            case "EVERYONE":
                sb.append(NEW_LINE).append("@everyone");
                break;
            default:
                Role mentionRole = guild.getRoleById(mentionID);
                if (mentionRole != null) {
                    sb.append(NEW_LINE).append(mentionRole.getAsMention());
                } else {
                    sb.append(NEW_LINE).append("Failed to append mention role. Please re-set your mention role.");
                    String guildID = guild.getId();
                    String guildName = guild.getName();
                    String textChannelID = channel.getTextChannelID();
                    log.info("Failed to append mention role for guild " +
                            guildName + "(" + guildID + ") in " + textChannelID);
                }
        }
    }
}
