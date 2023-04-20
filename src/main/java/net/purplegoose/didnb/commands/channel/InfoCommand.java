package net.purplegoose.didnb.commands.channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;
import net.purplegoose.didnb.utils.TimeUtil;

import static net.purplegoose.didnb.utils.StringUtil.DISABLE_MESSAGE;
import static net.purplegoose.didnb.utils.StringUtil.ENABLED_MESSAGE;

@Slf4j
@AllArgsConstructor
@CommandAnnotation(
        name = "info",
        usage = "/info [Not Required: channel]",
        description = "This command show's a user all information about a registered channel."
)
public class InfoCommand implements IClientCommand {

    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        TextChannel channel = getTargetTextChannel(event);
        Language language = guildsCache.getGuildLanguage(logInfo.getGuildID());

        if (!isTextChannelRegistered(logInfo.getGuildID(), channel.getId())) {
            log.error("{} used /{}. Error: Channel not registered. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), getClass().getAnnotation(CommandAnnotation.class).name(),
                    logInfo.getGuildName(), logInfo.getGuildID(), channel.getName(), channel.getId());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        log.info("{} used /{}. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), getClass().getAnnotation(CommandAnnotation.class).name(),
                logInfo.getGuildName(), logInfo.getGuildID(), channel.getName(), channel.getId());
        String mentionRoleID = getMentionRoleID(logInfo.getGuildID(), channel.getId());
        replyEphemeralToUser(event, buildInfoEmbed(channel, mentionRoleID, logInfo.getGuildID()));
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
        String context = "Event Messages Enabled: " +
                (channel.isEventMessageEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) + StringUtil.NEW_LINE +
                "Warn Messages Enabled: " + (channel.isEventWarnMessage() ? ENABLED_MESSAGE : DISABLE_MESSAGE);
        return new MessageEmbed.Field("General", context, false);
    }

    private String getMentionRoleContext(TextChannel textChannel, String mentionRoleID) {
        if (StringUtil.isStringOnlyContainingNumbers(mentionRoleID)) {
            Role mentionRole = textChannel.getGuild().getRoleById(mentionRoleID);
            if (mentionRole == null) {
                return "None" + StringUtil.NEW_LINE;
            }
            return mentionRole.getName() + " - " + mentionRoleID + StringUtil.NEW_LINE;
        }
        return mentionRoleID + StringUtil.NEW_LINE;
    }

    private MessageEmbed.Field getNotificationsField(NotificationChannel channel) {
        String content = "Ancient Arena: " + (channel.isAncientArenaMessageEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) +
                StringUtil.NEW_LINE +
                "Ancient Nigtmare: " + (channel.isAncientNightmareMessageEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) +
                StringUtil.NEW_LINE +
                "Assembly: " + (channel.isAssemblyMessageEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) +
                StringUtil.NEW_LINE +
                "Battleground: " + (channel.isBattlegroundsMessageEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) +
                StringUtil.NEW_LINE +
                "Vault: " + (channel.isVaultMessageEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) +
                StringUtil.NEW_LINE +
                "Demon Gates: " + (channel.isDemonGatesMessageEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) +
                StringUtil.NEW_LINE +
                "Shadow Lottery: " + (channel.isShadowLotteryMessageEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) +
                StringUtil.NEW_LINE +
                "Haunted Carriage: " + (channel.isHauntedCarriageMessageEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) +
                StringUtil.NEW_LINE +
                "Wrathborne Invasion: " + (channel.isWrathborneInvasionEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE) +
                StringUtil.NEW_LINE +
                "Onslaught: " + (channel.isOnSlaughtMessagesEnabled() ? ENABLED_MESSAGE : DISABLE_MESSAGE);
        return new MessageEmbed.Field("Events", content, false);
    }

}
