package net.purplegoose.didnb.commands.custom_notifications;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;

import java.util.Objects;

import static net.purplegoose.didnb.utils.CommandsUtil.ACTIVATION_BOOL;
import static net.purplegoose.didnb.utils.CommandsUtil.MESSAGE_ID_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's user to enable or disable custom notification messages.
 * Command: /message [Required: messagevalue] [Required: messageid]
 */
@Slf4j
@AllArgsConstructor
public class MessageCommand implements IClientCommand {

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getGuildLanguage(guildID);

        String messageID = getMessageID(event);
        if (Objects.equals(messageID, StringUtil.FAILED_MESSAGE)) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INVALID-ID"));
            log.error("{} used /message. Error: ID invalid. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            return;
        }

        CustomNotification customNotification = guildsCache.getClientGuildByID(guildID).getCustomNotificationByID(messageID);
        if (customNotification == null) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "CM-NOT-EXISTS"));
            log.error("{} used /editmessage. Error: ID does not exist. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            return;
        }

        boolean messageValue = getMessageValue(event);
        customNotification.setEnabled(messageValue);
        databaseRequests.updateCustomNotification(customNotification);
        replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "CM-ENABLED-DISABLED"), customNotification.getCustomMessageID(),
                messageValue ? "enabled" : "disabled"));
    }

    private boolean getMessageValue(SlashCommandInteractionEvent event) {
        OptionMapping messageValueOption = event.getOption(ACTIVATION_BOOL);
        return messageValueOption != null && messageValueOption.getAsBoolean();
    }

    private String getMessageID(SlashCommandInteractionEvent event) {
        OptionMapping messageIdOption = event.getOption(MESSAGE_ID_OPTION_NAME);
        return messageIdOption != null ? messageIdOption.getAsString() : StringUtil.FAILED_MESSAGE;
    }

}
