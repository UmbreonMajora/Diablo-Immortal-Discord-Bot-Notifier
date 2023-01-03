package me.umbreon.didn.commands.channel;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import me.umbreon.didn.utils.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static me.umbreon.didn.utils.StringUtil.FORMATTED_MESSAGE;
import static me.umbreon.didn.utils.StringUtil.NEW_LINE;

/**
 * @author Umbreon Majora
 * This command show's a user all infos about a registered channel.
 * Command: /info [Not required: targetchannel]
 */
public class InfoCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(InfoCommand.class);

    private final GuildsCache guildsCache;

    public InfoCommand(GuildsCache guildsCache) {
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String executingUser = getFullUsernameWithDiscriminator(user);

        TextChannel targetTextChannel = getTargetTextChannel(event);
        String targetTextChannelName = targetTextChannel.getName();
        String targetTextChannelID = targetTextChannel.getId();

        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Language language = guildsCache.getGuildLanguage(guildID);

        String guildName = event.getGuild().getName();
        if (!isTextChannelRegistered(guildID, targetTextChannelID)) {
            createLog(LOGGER, guildID, executingUser + " tried to use /info on an unregistered channel" +
                    " in " + guildName + "(" + guildID + ")," +
                    " Channel: " + targetTextChannelName + "(" + targetTextChannelID + ")");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        createLog(LOGGER, guildID, executingUser + " executed /info in " + guildName + "(" + guildID + ")," +
                " Channel: " + targetTextChannelName + "(" + targetTextChannelID + ")");
        String mentionRoleID = getMentionRoleID(guildID, targetTextChannelID);
        replyEphemeralToUser(event, buildInfoEmbed(targetTextChannel, mentionRoleID, guildID));
    }

    private String getMentionRoleID(String guildID, String targetTextChannelID) {
        return guildsCache.getClientGuildByID(guildID).getNotificationChannel(targetTextChannelID).getMentionRoleID();
    }

    private boolean isTextChannelRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }

    //todo: loop with gameevents enum?
    //TODO: FIX THIS STUFF WTF? so ugly
    private MessageEmbed buildInfoEmbed(TextChannel textChannel, String mentionRoleID, String guildID) {
        String textChannelID = textChannel.getId();
        String textChannelName = textChannel.getName();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        NotificationChannel notificationChannel = guildsCache.getClientGuildByID(guildID).getNotificationChannel(textChannelID);
        embedBuilder.setTitle(textChannelName);

        String timezone = guildsCache.getGuildTimeZone(notificationChannel.getGuildID());
        Language language = guildsCache.getGuildLanguage(guildID);

        embedBuilder.addField(LanguageController.getMessage(language, "TIMEZONE"), timezone, true);
        embedBuilder.addField(LanguageController.getMessage(language, "CURRENT-TIME"), TimeUtil.getTimeWithWeekday(timezone), true);
        embedBuilder.addField(LanguageController.getMessage(language, "TEXT-CHANNEL-ID"), "<#" + notificationChannel.getTextChannelID() + '>', false);

        if (mentionRoleID.equalsIgnoreCase("EVERYONE")) {
            embedBuilder.addField(LanguageController.getMessage(language, "CHANNEL-MENTION-ROLE"), "null", false);
        } else {
            Role mentionRole = textChannel.getGuild().getRoleById(mentionRoleID);
            embedBuilder.addField(LanguageController.getMessage(language, "CHANNEL-MENTION-ROLE"), String.valueOf(mentionRole), false);
        }

        String yes = LanguageController.getMessage(language, "TEXT-YES");
        String no = LanguageController.getMessage(language, "TEXT-NO");

        String eventMessageEnabled = notificationChannel.isEventMessageEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "EVENT-MESSAGES-ENABLED"), eventMessageEnabled, true);

        String eventHeadUpEnabled = notificationChannel.isWarnMessagesEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "WARN-MESSAGES-ENABLED"), eventHeadUpEnabled, true);

        String eventAncientNightmareEnabled = notificationChannel.isAncientNightmareMessageEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "ANCIENT-NIGHTMARE-MESSAGES-ENABLED"), eventAncientNightmareEnabled, true);

        String eventAssemblyEnabled = notificationChannel.isAssemblyMessageEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "ASSEMBLY-MESSAGES-ENABLED"), eventAssemblyEnabled, true);

        String eventBattlegroundsEnabled = notificationChannel.isBattlegroundsMessageEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "BATTLEGROUND-MESSAGES-ENABLED"), eventBattlegroundsEnabled, true);

        String eventRaidVaultEnabled = notificationChannel.isVaultMessageEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "VAULT-MESSAGES-ENABLED"), eventRaidVaultEnabled, true);

        String eventDemonGatesEnabled = notificationChannel.isDemonGatesMessageEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "DEMON-GATES-MESSAGES-ENABLED"), eventDemonGatesEnabled, true);

        String eventShadowLotteryEnabled = notificationChannel.isShadowLotteryMessageEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "SHADOW-LOTTERY-MESSAGES-ENABLED"), eventShadowLotteryEnabled, true);

        String eventHauntedCarriageEnabled = notificationChannel.isHauntedCarriageMessageEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "HAUNTED-CARRIAGE-MESSAGES-ENABLED"), eventHauntedCarriageEnabled, true);

        String eventWrathborneInvasionEnabled = notificationChannel.isWrathborneInvasionEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "WRATHBORNE-INVASION-MESSAGES-ENABLED"), eventWrathborneInvasionEnabled, true);

        String eventAncientArenaEnabled = notificationChannel.isAncientArenaMessageEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "ANCIENT-ARENA-MESSAGES-ENABLED"), eventAncientArenaEnabled, true);

        String eventOnSlaughtEnabled = notificationChannel.isOnSlaughtMessagesEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "ON-SLAUGHT-ENABLED"), eventOnSlaughtEnabled, true);

/* embedded messages are disabled.
        String eventHauntedCarriageEmbedEnabled = notificationChannel.isHauntedCarriageMessageEmbedEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "HAUNTED-CARRIAGE-EMBED-MESSAGES-ENABLED"), eventHauntedCarriageEmbedEnabled, true);

        String eventDemonGatesEmbedEnabled = notificationChannel.isDemonGatesMessageEmbedEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "DEMON-GATES-EMBED-MESSAGES-ENABLED"), eventDemonGatesEmbedEnabled, true);

        String eventAncientNightmareEmbedEnabled = notificationChannel.isAncientNightmareMessageEmbedEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "ANCIENT-NIGHTMARE-EMBED-MESSAGES-ENABLED"), eventAncientNightmareEmbedEnabled, true);

        String eventAncientArenaEmbedEnabled = notificationChannel.isAncientArenaMessageEmbedEnabled() ? yes : no;
        embedBuilder.addField(LanguageController.getMessage(language, "ANCIENT-ARENA-EMBED-MESSAGES-ENABLED"), eventAncientArenaEmbedEnabled, true);
*/
        return embedBuilder.build();
    }


}
