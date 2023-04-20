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
import net.purplegoose.didnb.enums.Weekday;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;

import java.util.Objects;

import static net.purplegoose.didnb.utils.CommandsUtil.CUSTOM_MESSAGE_ID_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's users to edit their custom notifications.
 * Command: /editmessage [Required: custommessageid] [Required: what2change] [Required: newvalue]
 */
@Slf4j
@AllArgsConstructor
public class EditMessageCommand implements IClientCommand {

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    @Override
    public void runCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getGuildLanguage(guildID);

        String customMessageID = getCustomMessageID(event);
        if (Objects.equals(customMessageID, StringUtil.FAILED_MESSAGE)) {
            log.error("{} used /editmessage. Error: ID invalid. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INFO-CUSTOM-MESSAGE-FAILED-ID-NULL"));
            return;
        }

        String valueToChange = getToChangeValue(event);
        String newValue = getChangedValue(event);
        CustomNotification customNotification = guildsCache.getClientGuildByID(guildID).getCustomNotificationByID(customMessageID);

        if (newValue == null || valueToChange == null) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "EDIT-MESSAGE-FAILED"));
            log.error("{} used /editmessage. Error: Null value. Guild: {}({}). Channel: {}({})",
                    logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
            return;
        }

        switch (valueToChange.toLowerCase()) {
            case "time":
                if (StringUtil.isStringNotInTimePattern(newValue)) {
                    replyEphemeralToUser(event, LanguageController.getMessage(language, "EDIT-MESSAGE-FAILED") + " " + LanguageController.getMessage(language, "TIME-INVALID"));
                    log.error("{} used /editmessage. Error: Time invalid. Guild: {}({}). Channel: {}({})",
                            logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
                    return;
                }
                String oldTime = customNotification.getTime();
                customNotification.setTime(newValue);
                log.info("{} used /editmessage. Time {} -> {}. Guild: {}({}). Channel: {}({})",
                        logInfo.getExecutor(), oldTime, newValue, logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());

                replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "MESSAGE-EDITED-TIME"), newValue));
                break;
            case "message":
                customNotification.setMessage(newValue);
                log.info("{} used /editmessage. Message changed. Guild: {}({}). Channel: {}({})",
                        logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
                replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "MESSAGE-EDITED-MESSAGE"), newValue));
                break;
            case "weekday":
                Weekday weekday = Weekday.findWeekdayByRawName(newValue);
                if (weekday == null) {
                    replyEphemeralToUser(event, LanguageController.getMessage(language, "EDIT-MESSAGE-FAILED") + " " + LanguageController.getMessage(language, "WEEKDAY-INVALID"));
                    log.error("{} used /editmessage. Weekday invalid. Guild: {}({}). Channel: {}({})",
                            logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
                    return;
                }
                String oldWeekday = customNotification.getWeekday();
                customNotification.setWeekday(weekday.rawName);
                log.info("{} used /editmessage. Weekday {} -> {}. Guild: {}({}). Channel: {}({})",
                        logInfo.getExecutor(), oldWeekday, weekday.rawName, logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
                replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "MESSAGE-EDITED-WEEKDAY"), newValue));
                break;
            default:
                log.info("{} used /editmessage. Value2Change invalid. Guild: {}({}). Channel: {}({})",
                        logInfo.getExecutor(), logInfo.getGuildName(), logInfo.getGuildID(), logInfo.getChannelName(), logInfo.getChannelID());
                replyEphemeralToUser(event, LanguageController.getMessage(language, "EDIT-MESSAGE-FAILED"));
                return;
        }
        databaseRequests.updateCustomNotification(customNotification);
    }

    private String getCustomMessageID(SlashCommandInteractionEvent event) {
        OptionMapping customMessageIdOption = event.getOption(CUSTOM_MESSAGE_ID_OPTION_NAME);
        return customMessageIdOption != null ? customMessageIdOption.getAsString() : StringUtil.FAILED_MESSAGE;
    }

    private String getToChangeValue(SlashCommandInteractionEvent event) {
        OptionMapping changeValueOption = event.getOption("editmessage");
        return changeValueOption != null ? changeValueOption.getAsString() : null;
    }

    private String getChangedValue(SlashCommandInteractionEvent event) {
        OptionMapping changedValueOption = event.getOption("editmessagevalue");
        return changedValueOption != null ? changedValueOption.getAsString() : null;
    }
}
