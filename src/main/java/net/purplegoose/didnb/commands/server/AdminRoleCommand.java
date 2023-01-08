package net.purplegoose.didnb.commands.server;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.purplegoose.didnb.utils.CommandsUtil.ROLE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to change the bot admin role for their server.
 * Command: /adminrole [Required: role]
 */
public class AdminRoleCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(AdminRoleCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public AdminRoleCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = event.getGuild().getId(); //Can't be null since it's caught in SlashCommandInteraction.java
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        Role newBotAdminRole = getNewBotAdminRole(event);
        if (newBotAdminRole == null) {
            createLog(LOGGER, getFullUsernameWithDiscriminator(user) + " tried to change the bot admin " +
                    "role of " + guildID + " but it failed because the new role was null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CHANGE-BOT-ADMIN-ROLE-FAILED-ROLE-NULL"));
            return;
        }

        String newBotAdminRoleID = newBotAdminRole.getId();
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        clientGuild.setGuildAdminRoleID(newBotAdminRoleID);
        databaseRequests.updateGuild(clientGuild);

        createLog(LOGGER, getFullUsernameWithDiscriminator(user) + " changed the bot admin role of " +
                guildID + " to " + newBotAdminRoleID);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CHANGED-BOT-ADMIN-ROLE"), newBotAdminRole.getAsMention()));
    }

    private Role getNewBotAdminRole(SlashCommandInteractionEvent event) {
        OptionMapping roleOption = event.getOption(ROLE_OPTION_NAME);
        return roleOption != null ? roleOption.getAsRole() : null;
    }
}
