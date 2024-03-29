package net.purplegoose.didnb.commands.custom_notifications;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.CustomMessagesCache;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;

import java.util.Objects;

import static net.purplegoose.didnb.utils.CommandsUtil.CUSTOM_MESSAGE_ID_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to delete their custom notification by the given id.
 * Command: /deletemessage [Required: custommessageid]
 */
@Slf4j
@AllArgsConstructor
public class DeleteMessageCommand implements IClientCommand {

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;
    private final CustomMessagesCache customMessagesCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getLanguageByGuildID(guildID);

        String customMessageID = getCustomMessageID(event);
        if (Objects.equals(customMessageID, StringUtil.FAILED_MESSAGE)) {
            log.error("{} used /deletemessage. Error: ID invalid. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INFO-CUSTOM-MESSAGE-FAILED-ID-NULL"));
            return;
        }

        if (!isCustomMessageGuildIdCurrentGuildId(guildID, customMessageID)) {
            log.error("{} used /deletemessage. Error: Not theirs. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "DELETE-CUSTOM-MESSAGE-FAILED-NOT-PERMITTED"));
            return;
        }

        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        if (!clientGuild.doCustomMessageExists(customMessageID)) {
            log.error("{} used /deletemessage. Error: Custom message with id does not exist. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CM-NOT-EXISTS"));
            return;
        }

        databaseRequests.deleteCustomNotificationByID(customMessageID);
        clientGuild.deleteCustomNotificationByID(customMessageID);
        log.info("{} used /deletemessage. ID: {}. Guild: {}({}). Channel: {}({})",
                logInfo.getExecutor(), customMessageID, logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
        customMessagesCache.removeIdentifier(customMessageID);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "DELETED-CUSTOM-MESSAGE"), customMessageID));
    }

    private String getCustomMessageID(SlashCommandInteractionEvent event) {
        OptionMapping customMessageIdOption = event.getOption(CUSTOM_MESSAGE_ID_OPTION_NAME);
        return customMessageIdOption != null ? customMessageIdOption.getAsString() : StringUtil.FAILED_MESSAGE;
    }

    private boolean isCustomMessageGuildIdCurrentGuildId(String guildID, String customMessageID) {
        CustomNotification customNotification = guildsCache.getClientGuildByID(guildID).getCustomNotificationByID(customMessageID);
        if (customNotification == null) return false;
        String targetGuildID = customNotification.getGuildID();
        return Objects.equals(guildID, targetGuildID);
    }
}
