package me.umbreon.didn.notifier;

import me.umbreon.didn.cache.GameDataCache;
import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.data.ClientGuild;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.gameevents.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static me.umbreon.didn.utils.StringUtil.NEW_LINE;

public class Notifier {

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
    }

    public void runNotificationScheduler(JDA client) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (ClientGuild clientGuild : guildsCache.getAllGuilds().values()) {
                    if (clientGuild.getNotificationChannelCount() == 0) {
                        return;
                    }

                    StringBuilder notificationMessage = new StringBuilder();

                    for (NotificationChannel channel : clientGuild.getAllNotificationChannels()) {
                        if (channel.isVaultMessageEnabled())
                            notificationMessage.append(vaultEvent.appendVaultNotificationIfHappening(clientGuild, channel));
                        if (channel.isBattlegroundsMessageEnabled())
                            notificationMessage.append(battlegroundEvent.appendBattlegroundsNotificationIfHappening(clientGuild, channel));
                        if (channel.isAncientArenaMessageEnabled())
                            notificationMessage.append(ancientArenaEvent.appendAncientArenaNotificationIfHappening(clientGuild, channel));
                        if (channel.isDemonGatesMessageEnabled())
                            notificationMessage.append(demonGatesEvent.appendDemonGatesNotificationIfHappening(clientGuild, channel));
                        if (channel.isAssemblyMessageEnabled())
                            notificationMessage.append(assemblyEvent.appendAssemblyNotificationIfHappening(clientGuild, channel));
                        if (channel.isHauntedCarriageMessageEnabled())
                            notificationMessage.append(hauntedCarriageEvent.appendHauntedCarriageNotificationIfHappening(clientGuild, channel));
                        if (channel.isAncientNightmareMessageEnabled())
                            notificationMessage.append(ancientNightmareEvent.appendAncientNightmareNotificationIfHappening(clientGuild, channel));
                        if (channel.isShadowLotteryMessageEnabled())
                            notificationMessage.append(shadowLotteryEvent.appendShadowLotteryNotificationIfHappening(clientGuild, channel));
                        if (channel.isWrathborneInvasionEnabled())
                            notificationMessage.append(wrathborneInvasionEvent.appendWrathborneInvasionNotificationIfHappening(clientGuild, channel));

                        TextChannel textChannel = client.getTextChannelById(channel.getTextChannelID());
                        String guildID = channel.getGuildID();

                        if (notificationMessage.length() > 0) {
                            addMessageMention(notificationMessage, channel, textChannel.getGuild());
                            textChannel.sendMessage(notificationMessage.toString()).queue(message -> {
                            }, throwable -> {
                                if (throwable instanceof InsufficientPermissionException) {
                                    client.getGuildById(guildID).getOwner().getUser().openPrivateChannel().queue(privateChannel -> {
                                        privateChannel.sendMessage("I do not have enough permissions.").queue();
                                    });
                                }
                            });
                        }
                    }
                }
            }
        }, getNextFullMinute(), 60 * 1000);
    }

    private Date getNextFullMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int nextMinute = calendar.get(Calendar.MINUTE) + 1;
        calendar.set(Calendar.MINUTE, nextMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private void addMessageMention(StringBuilder sb, NotificationChannel channel, Guild guild) {
        String mentionID = channel.getMentionRoleID();
        if (mentionID == null) {
            return;
        }

        switch (mentionID) {
            case "HERE":
                sb.append(NEW_LINE).append("@here");
                break;
            case "EVERYONE":
                sb.append(NEW_LINE).append("@everyone");
                break;
            default:
                if (guild.getRoleById(mentionID) != null) {
                    sb.append(NEW_LINE).append(guild.getRoleById(mentionID).getAsMention());
                } else {
                    sb.append(NEW_LINE).append("Failed to append mention role. Please re-set your mention role.");
                }
        }
    }
}
