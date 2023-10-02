package net.purplegoose.didnb.commands.channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;

import static net.purplegoose.didnb.utils.CommandsUtil.COMMAND_MENTION_ROLE;

@Slf4j
@AllArgsConstructor
@CommandAnnotation(
        name = "mentionrole",
        usage = "/mentionrole [Not required: role] [Not required: targetchannel]",
        description = "Allow's user to remove or change the mention role."
)
public class MentionRoleCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        TextChannel channel = getTargetTextChannel(event);
        Language language = guildsCache.getLanguageByGuildID(logInfo.getGuildID());

        if (!isTextChannelRegistered(logInfo.getGuildID(), channel.getId())) {
            log.error("{} used /{}. Error: Channel not registered. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), getClass().getAnnotation(CommandAnnotation.class).name(),
                    logInfo.getGuildName(), logInfo.getGuildID(), channel.getName(), channel.getId());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        Role mentionRole = getMentionRole(event);

        log.info("{} used /{}. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), getClass().getAnnotation(CommandAnnotation.class).name(),
                logInfo.getGuildName(), logInfo.getGuildID(), channel.getName(), channel.getId());

        updateChannel(logInfo.getGuildID(), channel.getId(), mentionRole);
        if (mentionRole == null) {
            replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "MENTION-ROLE-DISABLED"), channel.getAsMention()));
        } else {
            replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANGED-MENTION-ROLE"), mentionRole, channel.getAsMention()));
        }
    }

    private void updateChannel(String guildID, String targetTextChannelID, Role mentionRole) {
        NotificationChannel notificationChannel = guildsCache.getClientGuildByID(guildID).getNotificationChannel(targetTextChannelID);
        notificationChannel.setMentionRoleID(mentionRole != null ? mentionRole.getId() : null);
        databaseRequests.updateNotificationChannel(notificationChannel);
    }

    private boolean isTextChannelRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }

    private Role getMentionRole(SlashCommandInteractionEvent event) {
        OptionMapping mentionRoleOption = event.getOption(COMMAND_MENTION_ROLE);
        return mentionRoleOption != null ? mentionRoleOption.getAsRole() : null;
    }
}
