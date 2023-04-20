package net.purplegoose.didnb.commands.server;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.enums.ServerSetting;
import net.purplegoose.didnb.languages.LanguageController;

import static net.purplegoose.didnb.utils.CommandsUtil.*;

/**
 * @author Umbreon Majora
 * <p>
 * Allow's user to enable or disable head up / event messages server wide.
 * <p>
 * Command: /server [Required: serversetting] [Required: servervalue]
 */
@Slf4j
@AllArgsConstructor
public class ServerCommand implements IClientCommand {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getGuildLanguage(guildID);

        String serverSettingRawName = getServerSetting(event);
        ServerSetting serverSetting = ServerSetting.findServerSettingByRawName(serverSettingRawName);

        if (serverSetting == null) {
            log.error("{} used /server. Error: Server setting null. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), guildID, logInfo.getChannelName(), logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INVALID-SERVER-SETTING"));
            return;
        }

        boolean serverSettingValue = getServerSettingValue(event);
        updateClientGuild(serverSetting, guildID, serverSettingValue);

        log.info("{} used /server. {} is now {} Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), serverSettingRawName, serverSettingValue, logInfo.getGuildName(),
                guildID, logInfo.getChannelName(), logInfo.getChannelID());

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
        OptionMapping serverSettingValue = event.getOption(BOOL_OPTION_NAME);
        return serverSettingValue != null && serverSettingValue.getAsBoolean();
    }
}
