package net.purplegoose.didnb.commands.channel;

import lombok.extern.slf4j.Slf4j;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.NotificationChannel;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;

import static net.purplegoose.didnb.utils.CommandsUtil.ROLE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to remove or change the mention role.
 * Command: /mentionrole [Not required: role] [Not required: targetchannel]
 */
@Slf4j
public class MentionRoleCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public MentionRoleCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());

        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        String guildName = event.getGuild().getName();

        TextChannel targetTextChannel = getTargetTextChannel(event);
        String targetTextChannelName = targetTextChannel.getName();
        String targetTextChannelID = targetTextChannel.getId();

        Language language = guildsCache.getGuildLanguage(guildID);

        if (!isTextChannelRegistered(guildID, targetTextChannelID)) {
            log.info("{} executed /mentionrole in an unregistered channel. Guild: {}({}), Channel: {}({})",
                    executingUser, guildName, guildID, targetTextChannelName, targetTextChannelID);
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        Role mentionRole = getMentionRole(event);
        log.info("{} executed /mentionrole. Changed the mention role to {}. Guild: {}({}), Channel: {}({})",
                executingUser, (mentionRole != null ? mentionRole.getId() : null), guildName, guildID, targetTextChannelName, targetTextChannelID);

        updateChannel(guildID, targetTextChannelID, mentionRole);
        if (mentionRole == null) {
            replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "MENTION-ROLE-DISABLED"), targetTextChannel.getAsMention()));
        } else {
            replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANGED-MENTION-ROLE"), mentionRole, targetTextChannel.getAsMention()));
        }
    }

    private void updateChannel(String guildID, String targetTextChannelID, Role mentionRole) {
        NotificationChannel notificationChannel = guildsCache.getClientGuildByID(guildID).getNotificationChannel(targetTextChannelID);
        notificationChannel.setRoleID(mentionRole != null ? mentionRole.getId() : null);
        databaseRequests.updateNotificationChannel(notificationChannel);
    }

    private boolean isTextChannelRegistered(String guildID, String textChannelID) {
        return guildsCache.getClientGuildByID(guildID).isChannelRegistered(textChannelID);
    }

    private Role getMentionRole(SlashCommandInteractionEvent event) {
        OptionMapping mentionRoleOption = event.getOption(ROLE_OPTION_NAME);
        return mentionRoleOption != null ? mentionRoleOption.getAsRole() : null;
    }
}
