package me.umbreon.didn.notifier;

import com.mysql.cj.log.Log;
import me.umbreon.didn.cache.GameDataCache;
import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.server.ServerCommand;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.gameevents.*;
import me.umbreon.didn.logger.FileLogger;
import me.umbreon.didn.utils.TimeUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static me.umbreon.didn.utils.StringUtil.NEW_LINE;

public class Notifier {

    private final Logger LOGGER = LoggerFactory.getLogger(Notifier.class);

    private final GuildsCache guildsCache;

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
    }

    public void runNotificationScheduler(JDA client) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                boolean x = false;
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
                            String log = "Tried to send notification message to " + channel.getTextChannelID() + " on " +
                                    "guild " + channel.getGuildID() + ", but it failed because the channel was null!";
                            LOGGER.warn(log);
                            FileLogger.createClientFileLog(log);
                            continue;
                        }

                        addMessageMention(notificationMessage, channel, textChannel.getGuild());

                        try {
                            textChannel.sendMessage(notificationMessage.toString()).queue();
                        } catch (InsufficientPermissionException e) {
                            LOGGER.info("Failed to send notification.");
                            continue;
                        }

                        LOGGER.info("Sended notification message to " + channel.getTextChannelID());
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

        try {
            switch (mentionID.toUpperCase()) {
                case "HERE":
                    sb.append(NEW_LINE).append("@here");
                    break;
                case "EVERYONE":
                    sb.append(NEW_LINE).append("@everyone");
                    break;
                default:
                    if (guild.getRoleById(mentionID) != null) {
                        Role mentionRole = guild.getRoleById(mentionID);
                        sb.append(NEW_LINE).append(Objects.requireNonNull(mentionRole).getAsMention());
                    } else {
                        sb.append(NEW_LINE).append("Failed to append mention role. Please re-set your mention role.");
                        String guildID = guild.getId();
                        String guildName = guild.getName();
                        String textChannelID = channel.getTextChannelID();
                        FileLogger.createGuildFileLog(guildID, "Failed to append mention role for guild " +
                                guildName + "(" + guildID + ") in " + textChannelID);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
