package net.purplegoose.didnb.commands.channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;
import net.purplegoose.didnb.utils.TimeUtil;

/**
 * @author Umbreon Majora
 * <p>
 * @Command: /info [Not required: targetchannel]
 * <p>
 * @Description:
 * This command show's a user all information about a registered channel.
 * The command without a following parameter will print the information about the channel the command is sent in.
 * With a following text channel parameter it will print the information about the given channel.
 */
@Slf4j
@AllArgsConstructor
public class InfoCommand implements IClientCommand {

    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        TextChannel targetTextChannel = getTargetTextChannel(event);
        String channelName = targetTextChannel.getName();
        String channelID = targetTextChannel.getId();
        String guildID = logInfo.getGuildID();

        Language language = guildsCache.getGuildLanguage(guildID);
        if (!isTextChannelRegistered(guildID, channelID)) {
            log.error("{} used /info. Error: Channel not registered. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, channelName, channelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        log.info("{} used /info. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), logInfo.getGuildName(), guildID, targetTextChannel.getName(), channelID);
        String mentionRoleID = getMentionRoleID(guildID, channelID);
        replyEphemeralToUser(event, buildInfoEmbed(targetTextChannel, mentionRoleID, guildID));
    }

    private String getMentionRoleID(String guildID, String targetTextChannelID) {
        return guildsCache.getClientGuildByID(guildID)
                .getNotificationChannel(targetTextChannelID)
                .getMentionRoleID();
    }

    private boolean isTextChannelRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID)
                .isChannelRegistered(textChannelID);
    }

    private MessageEmbed buildInfoEmbed(TextChannel textChannel, String mentionRoleID, String guildID) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String textChannelID = textChannel.getId();
        NotificationChannel notificationChannel = guildsCache.getClientGuildByID(guildID).getNotificationChannel(textChannelID);

        String timezone = guildsCache.getGuildTimeZone(notificationChannel.getGuildID());
        String textChannelName = textChannel.getName();

        embedBuilder.setTitle("Info about " + textChannelName);
        String description = "Server timezone: " + timezone + StringUtil.NEW_LINE +
                "Server time: " + TimeUtil.getTimeWithWeekday(timezone) + StringUtil.NEW_LINE +
                "TextChannel ID: " + textChannelID + StringUtil.NEW_LINE +
                "Mention Role: " + getMentionRoleContext(textChannel, mentionRoleID);
        embedBuilder.setDescription(description);
        embedBuilder.addField(getGeneralField(notificationChannel));
        embedBuilder.addField(getNotificationsField(notificationChannel));
        embedBuilder.setFooter("purplegoose.net");

        return embedBuilder.build();
    }

    private MessageEmbed.Field getGeneralField(NotificationChannel channel) {
        String context = "Event Messages Enabled: " + (channel.isEventMessageEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Warn Messages Enabled: " + (channel.isEventWarnMessage() ? "Enabled" : "Disabled");
        return new MessageEmbed.Field("General", context, false);
    }

    private String getMentionRoleContext(TextChannel textChannel, String mentionRoleID) {
        if (StringUtil.isStringOnlyContainingNumbers(mentionRoleID)) {
            Role mentionRole = textChannel.getGuild().getRoleById(mentionRoleID);
            if (mentionRole == null) {
                return "None" + StringUtil.NEW_LINE;
            } else {
                return mentionRole.getName() + " - " + mentionRoleID + StringUtil.NEW_LINE;
            }
        }
        return mentionRoleID + StringUtil.NEW_LINE;
    }

    private MessageEmbed.Field getNotificationsField(NotificationChannel channel) {
        String content = "Ancient Arena: " + (channel.isAncientArenaMessageEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Ancient Nigtmare: " + (channel.isAncientNightmareMessageEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Assembly: " + (channel.isAssemblyMessageEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Battleground: " + (channel.isBattlegroundsMessageEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Vault: " + (channel.isVaultMessageEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Demon Gates: " + (channel.isDemonGatesMessageEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Shadow Lottery: " + (channel.isShadowLotteryMessageEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Haunted Carriage: " + (channel.isHauntedCarriageMessageEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Wrathborne Invasion: " + (channel.isWrathborneInvasionEnabled() ? "Enabled" : "Disabled") + StringUtil.NEW_LINE +
                "Onslaught: " + (channel.isOnSlaughtMessagesEnabled() ? "Enabled" : "Disabled");
        return new MessageEmbed.Field("Events", content, false);
    }

}
