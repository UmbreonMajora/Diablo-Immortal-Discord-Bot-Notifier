package me.umbreon.didn.commands.channel;

import me.umbreon.didn.cache.GuildsCache;
import me.umbreon.didn.commands.IClientCommand;
import me.umbreon.didn.data.NotificationChannel;
import me.umbreon.didn.database.DatabaseRequests;
import me.umbreon.didn.enums.Language;
import me.umbreon.didn.languages.LanguageController;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.umbreon.didn.utils.CommandsUtil.ROLE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to remove or change the mention role.
 * Command: /mentionrole [Not required: role] [Not required: targetchannel]
 */
public class MentionRoleCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(MentionRoleCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public MentionRoleCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        TextChannel targetTextChannel = getTargetTextChannel(event);
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        String targetTextChannelID = targetTextChannel.getId();
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        if (!isTextChannelRegistered(guildID, targetTextChannelID)) {
            createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " tried to use /mentionrole on an unregistered channel.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANNEL-NOT-REGISTERED"));
            return;
        }

        Role mentionRole = getMentionRole(event);
        createLog(LOGGER, guildID, getFullUsernameWithDiscriminator(user) + " changed the mention role of " + targetTextChannelID +
                ", the new value is " + (mentionRole != null ? mentionRole.getName() : null));

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
