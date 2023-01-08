package net.purplegoose.didnb.commands.server;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.enums.ServerSetting;
import net.purplegoose.didnb.languages.LanguageController;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static net.purplegoose.didnb.utils.CommandsUtil.SERVER_SETTING_OPTION_NAME;
import static net.purplegoose.didnb.utils.CommandsUtil.SERVER_SETTING_VALUE_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to enable or disable head up / event messages server wide.
 * Command: /server [Required: serversetting] [Required: servervalue]
 */
public class ServerCommand implements IClientCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(ServerCommand.class);

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    public ServerCommand(GuildsCache guildsCache, DatabaseRequests databaseRequests) {
        this.guildsCache = guildsCache;
        this.databaseRequests = databaseRequests;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        User user = event.getUser();
        Language language = guildsCache.getGuildLanguage(guildID);

        String serverSettingRawName = getServerSetting(event);
        ServerSetting serverSetting = ServerSetting.findServerSettingByRawName(serverSettingRawName);

        String executingUser = getFullUsernameWithDiscriminator(user);

        if (serverSetting == null) {
            createLog(LOGGER, executingUser + " tried to use /server but the setting is null.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INVALID-SERVER-SETTING"));
            return;
        }

        boolean serverSettingValue = getServerSettingValue(event);
        updateClientGuild(serverSetting, guildID, serverSettingValue);

        createLog(LOGGER, executingUser + " set serversetting " + serverSettingRawName + " to " +
                serverSettingValue);

        String replyMessage = getReplyMessage(serverSetting, serverSettingValue, language);
        replyEphemeralToUser(event, replyMessage);
    }

    private String getReplyMessage(ServerSetting serverSetting, boolean serverSettingValue, Language language) {
        if (serverSettingValue) {
            return String.format(LanguageController.getMessage(language, "ENABLED-SERVER-SETTING"), serverSetting.rawName);
        } else {
            return String.format(LanguageController.getMessage(language, "DISABLED-SERVER-SETTING"), serverSetting.rawName);
        }
    }

    private void updateClientGuild(ServerSetting serverSetting, String guildID, boolean serverSettingValue) {
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        switch (serverSetting) {
            case EVENT_MESSAGES:
                clientGuild.setEventMessageEnabled(serverSettingValue);
                break;
            case WARN_MESSAGES:
                clientGuild.setWarnMessagesEnabled(serverSettingValue);
                break;
        }
        databaseRequests.updateGuild(clientGuild);
    }

    private String getServerSetting(SlashCommandInteractionEvent event) {
        OptionMapping serverSettingOption = event.getOption(SERVER_SETTING_OPTION_NAME);
        return serverSettingOption != null ? serverSettingOption.getAsString() : null;
    }

    private boolean getServerSettingValue(SlashCommandInteractionEvent event) {
        OptionMapping serverSettingValue = event.getOption(SERVER_SETTING_VALUE_OPTION_NAME);
        return serverSettingValue != null && serverSettingValue.getAsBoolean();
    }
}
