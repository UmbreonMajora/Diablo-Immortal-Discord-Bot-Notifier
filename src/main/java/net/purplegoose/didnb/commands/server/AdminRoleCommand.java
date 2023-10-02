package net.purplegoose.didnb.commands.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;

import static net.purplegoose.didnb.utils.CommandsUtil.ADMIN_ROLE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * <p>
 * @Description: Allow's user to change the bot admin role for their server.
 * <p>
 * @Command: /adminrole [Not Required: role]
 */
@Slf4j
@AllArgsConstructor
public class AdminRoleCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        Language language = clientGuild.getLanguage();
        Role newBotAdminRole = getNewBotAdminRole(event);

        String oldBotAdminRoleID = clientGuild.getGuildAdminRoleID();
        String newBotAdminRoleID = newBotAdminRole != null ? newBotAdminRole.getId() : null;

        updateAdminRoleID(clientGuild, newBotAdminRoleID);

        log.info("{} used /adminrole. Old ID: {}. New ID: {}. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), oldBotAdminRoleID, newBotAdminRoleID, logInfo.getGuildName(), guildID,
                logInfo.getChannelName(), logInfo.getChannelID());

        String newBotAdminRoleAsString = newBotAdminRole != null ? newBotAdminRole.getAsMention() : "Default";
        String response = String.format(LanguageController.getMessage(language, "CHANGED-BOT-ADMIN-ROLE"), newBotAdminRoleAsString);
        replyEphemeralToUser(event, response);
    }

    private void updateAdminRoleID(ClientGuild clientGuild, String adminRoleID) {
        clientGuild.setGuildAdminRoleID(adminRoleID);
        databaseRequests.updateGuild(clientGuild);
    }

    private Role getNewBotAdminRole(SlashCommandInteractionEvent event) {
        OptionMapping roleOption = event.getOption(ADMIN_ROLE_OPTION_NAME);
        return roleOption != null ? roleOption.getAsRole() : null;
    }
}
