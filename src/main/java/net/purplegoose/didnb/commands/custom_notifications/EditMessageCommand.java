package net.purplegoose.didnb.commands.custom_notifications;

import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.IClientCommand;
import net.purplegoose.didnb.data.CustomNotification;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.enums.Weekday;
import net.purplegoose.didnb.languages.LanguageController;
import net.purplegoose.didnb.utils.StringUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static net.purplegoose.didnb.utils.CommandsUtil.CUSTOM_MESSAGE_ID_OPTION_NAME;

/**
 * @author Umbreon Majora
 * Allow's users to edit their custom notifications.
 * Command: /editmessage [Required: custommessageid] [Required: what2change] [Required: newvalue]
 */

public class EditMessageCommand implements IClientCommand {

    private final Logger logger = LoggerFactory.getLogger(EditMessageCommand.class);

    private final DatabaseRequests databaseRequests;
    private final GuildsCache guildsCache;

    public EditMessageCommand(DatabaseRequests databaseRequests, GuildsCache guildsCache) {
        this.databaseRequests = databaseRequests;
        this.guildsCache = guildsCache;
    }

    @Override
    public void runCommand(SlashCommandInteractionEvent event) {
        String guildID = Objects.requireNonNull(event.getGuild()).getId();
        Language language = guildsCache.getGuildLanguage(guildID);
        String executingUser = getFullUsernameWithDiscriminator(event.getUser());

        int customMessageID = getCustomMessageID(event);
        if (customMessageID == -1) {
            createLog(logger, executingUser + " tried to edit a custom notification but the id was invalid.");
            replyEphemeralToUser(event, LanguageController.getMessage(language, "INFO-CUSTOM-MESSAGE-FAILED-ID-NULL"));
            return;
        }

        String valueToChange = getToChangeValue(event);
        String newValue = getChangedValue(event);
        CustomNotification customNotification = guildsCache.getClientGuildByID(guildID).getCustomNotificationByID(customMessageID);

        if (newValue == null || valueToChange == null) {
            replyEphemeralToUser(event, LanguageController.getMessage(language, "EDIT-MESSAGE-FAILED"));
            createLog(logger, executingUser + " tried to change a custom notification but it failed because " +
                    "a value was null.");
            return;
        }

        switch (valueToChange.toLowerCase()) {
            case "time":
                if (StringUtil.isStringNotInTimePattern(newValue)) {
                    replyEphemeralToUser(event, LanguageController.getMessage(language, "EDIT-MESSAGE-FAILED") + " " + LanguageController.getMessage(language, "TIME-INVALID"));
                    createLog(logger, executingUser + " tried to edit a custom notification but it failed because the time was not in pattern.");
                    return;
                }

                customNotification.setTime(newValue);
                createLog(logger, executingUser + " changed the time of custom notification with id " +
                        customMessageID + " to " + valueToChange);
                replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "MESSAGE-EDITED-TIME"), newValue));
                break;
            case "message":
                customNotification.setMessage(newValue);
                createLog(logger, executingUser + " changed the message of custom notificaiton with id " +
                        customMessageID + " to " + valueToChange);
                replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "MESSAGE-EDITED-MESSAGE"), newValue));
                break;
            case "weekday":
                Weekday weekday = Weekday.findWeekdayByRawName(newValue);
                if (weekday == null) {
                    replyEphemeralToUser(event, LanguageController.getMessage(language, "EDIT-MESSAGE-FAILED") + " " + LanguageController.getMessage(language, "WEEKDAY-INVALID"));
                    createLog(logger, executingUser + " tried to edit a custom notification but it failed because the weekday was invalid.");
                    return;
                }

                customNotification.setWeekday(weekday.rawName);
                createLog(logger, executingUser + " changed to weekday of custom notification with id " +
                        customMessageID + " to " + valueToChange);
                replyEphemeralToUser(event, String.format(LanguageController.getMessage(language, "MESSAGE-EDITED-WEEKDAY"), newValue));
                break;
            default:
                createLog(logger, executingUser + " tried to change a custom notification but it failed " +
                        "because the value to change was invalid.");
                replyEphemeralToUser(event, LanguageController.getMessage(language, "EDIT-MESSAGE-FAILED"));
                return;
        }
        databaseRequests.updateCustomNotification(customNotification);
    }

    private int getCustomMessageID(SlashCommandInteractionEvent event) {
        OptionMapping customMessageIdOption = event.getOption(CUSTOM_MESSAGE_ID_OPTION_NAME);
        return customMessageIdOption != null ? customMessageIdOption.getAsInt() : -1;
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
