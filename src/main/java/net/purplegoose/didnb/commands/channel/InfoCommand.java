package net.purplegoose.didnb.commands.channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.annotations.GameEvent;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;
import net.purplegoose.didnb.utils.TimeUtil;

import java.lang.reflect.Field;

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
        StringBuilder sb = new StringBuilder(100);
        for (Field f : channel.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(GameEvent.class)) {
                GameEvent gameEvent = f.getAnnotation(GameEvent.class);
                f.setAccessible(true);
                try {
                    sb.append(gameEvent.eventName()).append(": ");
                    sb.append(f.getBoolean(channel) ? ENABLED_MESSAGE : DISABLE_MESSAGE);
                    sb.append(StringUtil.NEW_LINE);
                } catch (IllegalAccessException e) {
                    log.error("Failed to generate content for info command.", e);
                    return new MessageEmbed.Field("Events", "Failed to generate! Please report.", false);
                }
            }
        }
        return new MessageEmbed.Field("Events", sb.toString(), false);
    }

}
